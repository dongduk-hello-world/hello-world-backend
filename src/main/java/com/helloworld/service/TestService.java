package com.helloworld.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.domain.Test;
import com.helloworld.repository.TestRepository;

@Service
public class TestService {
	@Autowired
	private TestRepository testRepo;
	public void setTestRepo(TestRepository testRepo) {
		this.testRepo = testRepo;
	}
	public Test getTest(Long testId) {
		Optional<Test> result = testRepo.findById(testId);
		if(result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}
}
