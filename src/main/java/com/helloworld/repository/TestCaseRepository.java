package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.TestCase;

public interface TestCaseRepository extends CrudRepository<TestCase, Long> {
	List<TestCase> findByTestId(Long testId);
}