package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.Test;

public interface TestRepository extends CrudRepository<Test, Long> {
	List<Test> findByAssignmentId(Long assignmentId);
	List<Test> findByWriterId(String writerId);
}
