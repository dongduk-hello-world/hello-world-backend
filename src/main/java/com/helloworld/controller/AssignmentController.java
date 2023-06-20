package com.helloworld.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloworld.dao.jpa.JpaAssignmentDAO;
import com.helloworld.dao.jpa.JpaUserDAO;
import com.helloworld.domain.Assignment;
import com.helloworld.domain.Submit;
import com.helloworld.domain.Test;
import com.helloworld.domain.TestCase;
import com.helloworld.service.FileService;
import com.helloworld.service.SubmitService;
import com.helloworld.service.TestService;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

	@Autowired JpaUserDAO userDAO;
	@Autowired JpaAssignmentDAO assignmentDAO;
	@Autowired FileService fileService;
	@Autowired TestService testService;
	@Autowired SubmitService submitService;
	
	// assignment 등록
	@Transactional
	@PostMapping
    @ResponseStatus(HttpStatus.OK)
	public void insert(HttpServletRequest request, @RequestBody AssignmentRequest req) {
		HttpSession session = request.getSession();
		long userId = (long) session.getAttribute("user_id");
		
		Assignment assignment = new Assignment();
		assignment.setLecture_id(req.getClassId());
		assignment.setName(req.getName());
		assignment.setWriter_id(userId);
		assignment.setStart_time(req.getStart_time());
		assignment.setTest_time(req.getTest_time());
		assignment.setEnd_time(req.getEnd_time());
		long assignmentId = assignmentDAO.insertAssignmentAndId(assignment);
		
		List<TestRequestByAssignment> testReqs = req.getTests();
		for(TestRequestByAssignment testReq: testReqs) {
			Test test = new Test();
			test.setAssignmentId(assignmentId);
			test.setName(testReq.getName());
			test.setDescription(testReq.getDescription());
			test.setScore(testReq.getScore());
			test.setWriterId(userId);
			long testId = testService.insert(test);
			
			List<TestCaseRequestByAssignment> testcaseReqs = testReq.getTestcases();
			for(TestCaseRequestByAssignment testcaseReq: testcaseReqs) {
				TestCase testcase = new TestCase();
				testcase.setTestId(testId);
				testcase.setInput(testcaseReq.getInput());
				testcase.setOutput(testcaseReq.getOutput());
				testService.insert(testcase);
			}
		}
    }
	
	// assignment 정보 return
	@GetMapping("/{assignmentId}")
	public ResponseEntity<Map<String, Object>> get(@PathVariable long assignmentId, Map<String, Object> model) {
		Assignment result = assignmentDAO.getAssignment(assignmentId);
		model.put("assignment", result);
		return ResponseEntity.ok(model);
	}

	// assignment 정보 수정
	@Transactional
	@PutMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody AssignmentRequest req, @PathVariable long assignmentId) {
		Assignment assignment = assignmentDAO.getAssignment(assignmentId);
		assignment.setLecture_id(req.getClassId());
		assignment.setName(req.getName());
		assignment.setStart_time(req.getStart_time());
		assignment.setTest_time(req.getTest_time());
		assignment.setEnd_time(req.getEnd_time());
		assignmentDAO.updateAssignment(assignment);

		List<TestRequestByAssignment> testReqs = req.getTests();
		List<Test> tests = testService.getTestListByAssignmentId(assignment.getAssignment_id());
		int index = 0;
		for(TestRequestByAssignment testReq: testReqs) {
			Test test = tests.get(index++);
			if(index >= testReqs.size() || index >= tests.size()) {
				break;
			}
			test.setName(testReq.getName());
			test.setDescription(testReq.getDescription());
			test.setScore(testReq.getScore());
			testService.update(test);
			
			List<TestCaseRequestByAssignment> testcaseReqs = testReq.getTestcases();
			for(TestCaseRequestByAssignment testcaseReq: testcaseReqs) {
				TestCase testcase = new TestCase();
				testcase.setTestId(test.getTestId());
				testcase.setInput(testcaseReq.getInput());
				testcase.setOutput(testcaseReq.getOutput());
				testService.update(testcase);
			}
		}
	}

	// assignment 삭제
	@DeleteMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable long assignmentId) {
		Assignment data = assignmentDAO.getAssignment(assignmentId);
		List<Test> tests = testService.getTestListByAssignmentId(assignmentId);
		for(Test test: tests) {
			List<TestCase> testcases = test.getTestCaseList();
			for(TestCase testcase: testcases) {
				testService.delete(testcase);
			}
			testService.delete(test);
		}
		assignmentDAO.deleteAssignment(data);
	}
	
	// assignment에 있는 test return
	@GetMapping("/{assignmentId}/tests")
	public ResponseEntity<Map<String, Object>> getTestList(@PathVariable long assignmentId, Map<String, Object> model) {
		List<Test> list = testService.getTestListByAssignmentId(assignmentId);
		model.put("tests", list);
		return ResponseEntity.ok(model);
	}

	// assignment의 시험 결과들 return
	@GetMapping("/{assignmentId}/results")
	public ResponseEntity<Map<String, Object>> getResultList(@PathVariable long assignmentId, Map<String, Object> model) {
		List<Submit> tests = submitService.getSubmitListByAssignmentId(assignmentId);
		Map<Long, ResultAllResponse> resMap = new HashMap<>();
		float totalScore = 0;
		float sumScore = 0;
		int totalStudentNum = 0;
		for(Submit s: tests) {
			ResultAllResponse res = resMap.get(s.getSubmitorId());
			if(res == null) {
				res = new ResultAllResponse();
			}
			com.helloworld.domain.User u = userDAO.getUser(s.getSubmitorId());
			res.setStudentId(u.getUser_id());
			res.setStudentName(u.getName());
			res.setStudentNumber(u.getEmail().split("@")[0]);
			res.setScore(res.getScore() + s.getScore());
			List<TestResponseByAssignment> testResponses = res.getTestList();
			if(testResponses == null) {
				testResponses = new ArrayList<>();
			}
			Test test = testService.getTest(s.getTestId());
			TestResponseByAssignment testResponse = new TestResponseByAssignment();
			testResponse.setTestId(test.getTestId());
			testResponse.setTestName(test.getName());
			testResponse.setScore(s.getScore());
			testResponse.setMaxScore(test.getScore());
			testResponse.setCode(fileService.readFileCode(s.getFile()));
			testResponses.add(testResponse);
			resMap.put(u.getUser_id(), res);
			sumScore += s.getScore();
		}
		for(long key: resMap.keySet()) {
			if(totalStudentNum == 0) {
				ResultAllResponse res = resMap.get(key);
				List<TestResponseByAssignment> testList = res.getTestList();
				for(TestResponseByAssignment t: testList) {
					totalScore += t.getMaxScore();
				}
			}
			totalStudentNum++;
		}
		List<ResultAllResponse> list = new ArrayList<>(resMap.values());
		model.put("totalScore", totalScore);
		model.put("sumScore", sumScore);
		model.put("totalStudentNum", totalStudentNum);
		model.put("list", list);
		return ResponseEntity.ok(model);
	}
	
	// 시험 최종 제출 완료 후 할 일을 이 곳에 작성
	// ex. session 초기화	
	@PostMapping("/{assignmentId}/results/{userId}")
    @ResponseStatus(HttpStatus.OK)
	public void submit(HttpServletRequest request, @PathVariable long assignmentId, @PathVariable long userId, SessionStatus status) {
		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("email");
		status.setComplete();
		session.setAttribute("user_id", userId);
		session.setAttribute("email", email);
		session.setAttribute("uid", email.split("@")[0]);
	}
	
	// 학생 한 명의 상세 결과를 조회
	@GetMapping("/{assignmentId}/results/{userId}")
	public ResponseEntity<Map<String, Object>> getResult(@PathVariable long assignmentId, @PathVariable long userId, Map<String, Object> model) {
		Map<Long, TestResponseByAssignment> testMap = new HashMap<>();
		List<Submit> submits = submitService.getSubmitListByAssignmentIdAndUserId(assignmentId, userId);
		List<Test> tests = testService.getTestListByAssignmentId(assignmentId);
		float score = 0;
		float maxScore = 0;
		for(Submit submit: submits) {
			TestResponseByAssignment testRes = new TestResponseByAssignment();
			score += submit.getScore();
			testRes.setScore(submit.getScore());
			testMap.put(submit.getTestId(), testRes);
		}
		for(Test test: tests) {
			TestResponseByAssignment testRes = testMap.get(test.getTestId());
			maxScore += test.getScore();
			testRes.setMaxScore(test.getScore());
			testRes.setTestName(test.getName());
			testMap.put(test.getTestId(), testRes);
		}
		List<TestResponseByAssignment> list = new ArrayList<>(testMap.values());
		model.put("totalScore", maxScore);
		model.put("score", score);
		model.put("tests", list);
		return ResponseEntity.ok(model);
	}
	
	// user가 제출한 코드를 return
	@GetMapping("/{assignmentId}/results/{userId}/code/{testId}")
	public ResponseEntity<Map<String, Object>> getResultCode(@PathVariable long assignmentId, @PathVariable long userId, @PathVariable long testId, Map<String, Object> model) {
		List<Submit> submits = submitService.getSubmitListByAssignmentIdAndUserIdAndTestId(assignmentId, userId, testId);
		String code = "";
		if(submits != null && submits.size() > 0) {
			Collections.sort(submits);
			code = fileService.readFileCode(submits.get(0).getFile());
		}
		model.put("code", code);
		return ResponseEntity.ok(model);
	}
}

class Assignment_ResultStudentResponse {
	private float score, totalScore;
	private List<TestResponseByAssignment> tests;
	
	public Assignment_ResultStudentResponse() {}
	
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public float getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}
	public List<TestResponseByAssignment> getTests() {
		return tests;
	}
	public void setTests(List<TestResponseByAssignment> tests) {
		this.tests = tests;
	}
}

class ResultAllResponse {
	private long studentId;
	private String studentName, studentNumber;
	private float score;
	private List<TestResponseByAssignment> testList;
	
	public ResultAllResponse() {}
	
	public long getStudentId() {
		return studentId;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public List<TestResponseByAssignment> getTestList() {
		return testList;
	}
	public void setTestList(List<TestResponseByAssignment> testList) {
		this.testList = testList;
	}
}

class TestResponseByAssignment {
	private long testId;
	private String testName, code;
	private float score, maxScore;
	
	public TestResponseByAssignment() {}
	
	public long getTestId() {
		return testId;
	}
	public void setTestId(long testId) {
		this.testId = testId;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public float getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(float maxScore) {
		this.maxScore = maxScore;
	}
}

class AssignmentRequest {
	private String classId, name;
	@JsonFormat(pattern="YYYY-MM-DD HH:mm", timezone="GMT+9")
	private Date start_time;
	@JsonFormat(pattern="YYYY-MM-DD HH:mm", timezone="GMT+9")
	private Date end_time;
	@JsonFormat(pattern="HH:mm", timezone="GMT+9")
	private Date test_time;
	private List<TestRequestByAssignment> tests;
	
	public AssignmentRequest() {}
	
	public long getClassId() {
		return Long.parseLong(classId);
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public Date getTest_time() {
		return test_time;
	}
	public void setTest_time(Date test_time) {
		this.test_time = test_time;
	}
	public List<TestRequestByAssignment> getTests() {
		return tests;
	}
	public void setTests(List<TestRequestByAssignment> tests) {
		this.tests = tests;
	}
}

class TestRequestByAssignment {
	private String name, description, score;
	private List<TestCaseRequestByAssignment> testcases;
	
	public TestRequestByAssignment() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getScore() {
		return Float.parseFloat(score);
	}
	public void setScore(String score) {
		this.score = score;
	}
	public List<TestCaseRequestByAssignment> getTestcases() {
		return testcases;
	}
	public void setTestcases(List<TestCaseRequestByAssignment> testcases) {
		this.testcases = testcases;
	}	
}

class TestCaseRequestByAssignment {
	private String input, output;
	
	public TestCaseRequestByAssignment() {}
	
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
}