package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.Submit;

public interface SubmitRepository extends CrudRepository<Submit, Long> {
	List<Submit> findByTestIdAndSubmitorId(Long testId, String submitorId);
	List<Submit> findDistinctSubmitorIdTopOrderByScoreAndAssignmentId(Long assignmentId);
}
