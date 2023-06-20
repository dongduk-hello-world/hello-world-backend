package com.helloworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.dao.UserDAO;
import com.helloworld.domain.User;
import com.helloworld.domain.Lecture;

@Service
public class UserService {

	@Autowired
	private UserDAO userDao;
	
	public void insertUser(User user) {
		userDao.insertUser(user);
	}
	
	public User getUser(long user_id) {
		return userDao.getUser(user_id);
	}
	
	public void updateUser(User user) {
		userDao.updateUser(user);
	}
	
	public void UpdatePassword(long user_id, String password) {
		userDao.updatePassword(user_id, password);
	}
	
	// 자신이 개설한 강좌(객체)
	public List<Lecture> getMyLectureByProfessor(long user_id)  {
		return userDao.getProfessorLectureList(user_id);
	}
	// 자신이 수강중인 강좌(id)
	public List<String> getMyLectureByStudent(long user_id) {
		return userDao.getStudentLectureList(user_id);
	}
}
