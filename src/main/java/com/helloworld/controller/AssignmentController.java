package com.helloworld.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.helloworld.dao.jpa.JpaAssignmentDAO;
import com.helloworld.dao.jpa.JpaUserDAO;
import com.helloworld.domain.Assignment;
import com.helloworld.domain.Submit;
import com.helloworld.domain.Test;
import com.helloworld.domain.TestCase;
import com.helloworld.service.SubmitService;
import com.helloworld.service.TestService;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

	@Autowired JpaUserDAO userDAO;
	@Autowired JpaAssignmentDAO assignmentDAO;
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
	public ResponseEntity<Assignment> get(@PathVariable long assignmentId) {
		return ResponseEntity.ok(assignmentDAO.getAssignment(assignmentId));
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
		assignmentDAO.deleteAssignment(data);
	}
	
	// assignment에 있는 test return
	@GetMapping("/{assignmentId}/tests")
	public ResponseEntity<List<Test>> getTestList(@PathVariable long assignmentId) {
		return ResponseEntity.ok(testService.getTestListByAssignmentId(assignmentId));
	}

	// assignment의 시험 결과들 return
	@GetMapping("/{assignmentId}/results")
	public ResponseEntity<Map<String, Object>> getResultList(@PathVariable long assignmentId, Map<String, Object> model) {
		List<ResultAllResponse> responses = new ArrayList<>();
		List<Submit> tests = submitService.getSubmitListByAssignmentId(assignmentId);
		int totalScore = 0;
		int sumScore = 0;
		int totalStudentNum = tests.size();
		for(Submit s: tests) {
			ResultAllResponse res = new ResultAllResponse();
			com.helloworld.domain.User u = userDAO.getUser(s.getSubmitorId());
			res.setStudentId(u.getUser_id());
			res.setStudentName(u.getName());
			res.setStudentNumber(u.getEmail().split("@")[0]);
			res.setScore(res.getScore() + s.getScore());
			responses.add(res);
		}
		model.put("tests", tests);
		return ResponseEntity.ok(model);
	}
	
	// 시험 최종 제출 완료 후 할 일을 이 곳에 작성
	// ex. session 초기화	
	@PostMapping("/{assignmentId}/results/{userId}")
	public List<String> submit(@PathVariable long assignmentId, @PathVariable long userId) {

		return null;
	}
	
	// 학생 한 명의 상세 결과를 조회
	@GetMapping("/{assignmentId}/results/{userId}")
	public List<String> addResult(@PathVariable long assignmentId, @PathVariable long userId) {

		return null;
	}
	
	// user가 제출한 코드를 return
	@GetMapping("/{assignmentId}/results/{userId}/code/{testId}/")
	public List<String> getResultCode(@PathVariable long assignmentId, @PathVariable long userId, @PathVariable long testId) {

		return null;
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