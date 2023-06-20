package com.helloworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.dao.AssignmentDAO;
import com.helloworld.domain.Assignment;

@Service
public class AssignmentService {

	@Autowired
	AssignmentDAO assignmentDao;
	
	public void insertAssignment(Assignment assignment) {
		assignmentDao.insertAssignment(assignment);
	}
	
	public void updateAssignment(Assignment assignment) {
		assignmentDao.updateAssignment(assignment);
	}
	
	public void deleteAssignment(Assignment assignment) {
		assignmentDao.deleteAssignment(assignment);
	}
	
	public Assignment getAssignment(long assignment_id) {
		return assignmentDao.getAssignment(assignment_id);
	}
	
	public List<Assignment> fingAssignmentByLectureId(long lecture_id) {
		return assignmentDao.findByLectureId(lecture_id);
	}
}
