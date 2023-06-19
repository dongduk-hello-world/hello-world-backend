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
	public List<Submit> getSubmitListByTestIdAndUserId(Long testId, String userId) {
		return submitRepo.findByTestIdAndSubmitorId(testId, userId);
	}
	public List<Submit> getSubmitListByAssignmentId(Long assignmentId) {
		return submitRepo.findDistinctSubmitorIdTopOrderByScoreAndAssignmentId(assignmentId);
	}
	public void insert(Submit submit) {
		submitRepo.save(submit);
	}
}
