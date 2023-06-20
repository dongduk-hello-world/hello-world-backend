package com.helloworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.domain.Submit;
import com.helloworld.repository.SubmitRepository;

@Service
public class SubmitService {
	@Autowired
	private SubmitRepository submitRepo;
	
	public void setSubmitRepo(SubmitRepository repo) {
		this.submitRepo = repo;
	}

	public List<Submit> getSubmitListByAssignmentId(long assignmentId) {
		return submitRepo.findByAssignmentId(assignmentId);
	}
	public List<Submit> getSubmitListByTestIdAndUserId(long testId, long userId) {
		return submitRepo.findByTestIdAndSubmitorId(testId, userId);
	}
	public List<Submit> getSubmitListByAssignmentIdAndUserId(long assignmentId, long userId) {
		return submitRepo.findByAssignmentIdAndSubmitorId(assignmentId, userId);
	}
	public List<Submit> getSubmitListByAssignmentIdAndUserIdAndTestId(long assignmentId, long userId, long testId) {
		return submitRepo.findByAssignmentIdAndSubmitorIdAndTestId(assignmentId, userId, testId);
	}
	
	public void insert(Submit submit) {
		submitRepo.save(submit);
	}
}
