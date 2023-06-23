package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.Assignment;

public interface AssignmentRepository extends CrudRepository<Assignment, Long> {
	List<Assignment> findByLectureId(long lecture_id);
}
