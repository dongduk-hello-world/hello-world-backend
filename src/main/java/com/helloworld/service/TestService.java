package com.helloworld.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.domain.Test;
import com.helloworld.domain.TestCase;
import com.helloworld.repository.TestCaseRepository;
import com.helloworld.repository.TestRepository;

@Service
public class TestService {
	@Autowired private TestRepository testRepo;
	@Autowired private TestCaseRepository testCaseRepo;
	
	public void setTestRepo(TestRepository testRepo) {
		this.testRepo = testRepo;
	}
	public void setTestCaseRepo(TestCaseRepository testCaseRepo) {
		this.testCaseRepo = testCaseRepo;
	}
	
	public Test getTest(Long testId) {
		Optional<Test> result = testRepo.findById(testId);
		if(result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}
	public List<Test> getTestListByAssignmentId(Long assignmentId) {
		return testRepo.findByAssignmentId(assignmentId);
	}
	public Long insert(Test test) {
		Test result = testRepo.save(test);
		return result.getTestId();
	}
	public Test update(Test test) {
		Test result = testRepo.save(test);
		return result;
	}
	public void delete(Test test) {
		testRepo.delete(test);
	}
	
	public List<TestCase> getTestCaseList(Long testId) {
		return testCaseRepo.findByTestId(testId);
	}
	public Long insert(TestCase testcase) {
		TestCase result = testCaseRepo.save(testcase);
		return result.getTestCaseId();
	}
	public TestCase update(TestCase testcase) {
		TestCase result = testCaseRepo.save(testcase);
		return result;
	}
	public void delete(TestCase testcase) {
		testCaseRepo.delete(testcase);
	}
}
