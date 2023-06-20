package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.Submit;

public interface SubmitRepository extends CrudRepository<Submit, Long> {
	List<Submit> findByAssignmentId(long assignmentId);
	List<Submit> findByTestIdAndSubmitorId(long testId, long submitorId);
	List<Submit> findByAssignmentIdAndSubmitorId(long assignmentId, long submitorId);
	List<Submit> findByAssignmentIdAndSubmitorIdAndTestId(long assignmentId, long submitorId, long testId);
	// List<Submit> findDistinctSubmitorIdTopOrderByScoreAndAssignmentId(long assignmentId);
}
