package com.helloworld.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.helloworld.domain.Assignment;

public interface AssignmentDAO {
	public void createAssignment(Assignment assignment)throws DataAccessException;
	
	public void updateAssignment(Assignment assignment)throws DataAccessException;
	
	public void deleteAssignment(Assignment assignment)throws DataAccessException;

	Assignment getAssignment(long assignment_id) throws DataAccessException;
	
	List<Assignment> findByLectureId(long lecture_id) throws DataAccessException;
}
