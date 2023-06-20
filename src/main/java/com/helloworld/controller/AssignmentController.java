package com.helloworld.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

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
	public void insert(@RequestBody AssignmentRequest req) {
		assignmentDAO.createAssignment(req.getAssignment());
		List<Test> tests = req.getTests();
		for(Test test: tests) {
			List<TestCase> testcases = test.getTestCaseList();
			for(TestCase testcase: testcases) {
				testService.insert(testcase);
			}
			testService.insert(test);
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
	public void update(@RequestBody AssignmentRequest req, @PathVariable long assignmentId) {
		assignmentDAO.updateAssignment(req.getAssignment());
		List<Test> tests = req.getTests();
		for(Test test: tests) {
			List<TestCase> testcases = test.getTestCaseList();
			for(TestCase testcase: testcases) {
				testService.update(testcase);
			}
			testService.update(test);
		}
	}

	// assignment 삭제
	@DeleteMapping("/{assignmentId}")
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
			List<TestResponse> testResponses = res.getTestList();
			if(testResponses == null) {
				testResponses = new ArrayList<>();
			}
			Test test = testService.getTest(s.getTestId());
			TestResponse testResponse = new TestResponse();
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
				List<TestResponse> testList = res.getTestList();
				for(TestResponse t: testList) {
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
	public void submit(HttpServletRequest request, @PathVariable long assignmentId, @PathVariable long userId, SessionStatus status) {
		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("email");
		status.setComplete();
		session.setAttribute("user_id", userId);
		session.setAttribute("email", email);
	}
	
	// 학생 한 명의 상세 결과를 조회
	@GetMapping("/{assignmentId}/results/{userId}")
	public ResponseEntity<Map<String, Object>> getResult(@PathVariable long assignmentId, @PathVariable long userId, Map<String, Object> model) {
		Map<Long, TestResponse> testMap = new HashMap<>();
		List<Submit> submits = submitService.getSubmitListByAssignmentIdAndUserId(assignmentId, userId);
		List<Test> tests = testService.getTestListByAssignmentId(assignmentId);
		float score = 0;
		float maxScore = 0;
		for(Submit submit: submits) {
			TestResponse testRes = new TestResponse();
			score += submit.getScore();
			testRes.setScore(submit.getScore());
			testMap.put(submit.getTestId(), testRes);
		}
		for(Test test: tests) {
			TestResponse testRes = testMap.get(test.getTestId());
			maxScore += test.getScore();
			testRes.setMaxScore(test.getScore());
			testRes.setTestName(test.getName());
			testMap.put(test.getTestId(), testRes);
		}
		List<TestResponse> list = new ArrayList<>(testMap.values());
		model.put("totalScore", maxScore);
		model.put("score", score);
		model.put("tests", list);
		return ResponseEntity.ok(model);
	}
	
	// user가 제출한 코드를 return
	@GetMapping("/{assignmentId}/results/{userId}/code/{testId}/")
	public ResponseEntity<Map<String, Object>> getResultCode(@PathVariable long assignmentId, @PathVariable long userId, @PathVariable long testId, Map<String, Object> model) {
		List<Submit> submits = submitService.getSubmitListByAssignmentIdAndUserIdAndTestId(assignmentId, userId, testId);
		Collections.sort(submits);
		String code = fileService.readFileCode(submits.get(0).getFile());
		model.put("code", code);
		return ResponseEntity.ok(model);
	}
}

class ResultStudentResponse {
	private float score, totalScore;
	private List<TestResponse> tests;
	
	public ResultStudentResponse() {}
	
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
	public List<TestResponse> getTests() {
		return tests;
	}
	public void setTests(List<TestResponse> tests) {
		this.tests = tests;
	}
}

class ResultAllResponse {
	private long studentId;
	private String studentName, studentNumber;
	private float score;
	private List<TestResponse> testList;
	
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
	public List<TestResponse> getTestList() {
		return testList;
	}
	public void setTestList(List<TestResponse> testList) {
		this.testList = testList;
	}
}

class TestResponse {
	private long testId;
	private String testName, code;
	private float score, maxScore;
	
	public TestResponse() {}
	
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
	private Assignment assignment;
	private List<Test> tests;
	
	public AssignmentRequest() {}
	
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	public List<Test> getTests() {
		return tests;
	}
	public void setTests(List<Test> tests) {
		this.tests = tests;
	}
}