package com.helloworld.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.helloworld.domain.Lecture;
import com.helloworld.domain.SignUp;
import com.helloworld.domain.User;


public interface LectureDAO {
	public void insertLecture(Lecture lecture) throws DataAccessException;
	
	public void updateLecture(Lecture lecture) throws DataAccessException;
	
	public void deleteLecture(Lecture lecture)throws DataAccessException;

	Lecture getLecture(long lecture_id) throws DataAccessException;
	
	List<Lecture> getLectureByName(String name) throws DataAccessException;

	public void signUpLecture(long lecture_id, long user_id) throws DataAccessException;
	
	public void leaveLecture(SignUp signup) throws DataAccessException;
	
	// 수강생 리스트
	List<Long> getStudent(long lecture_id) throws DataAccessException;
	
	// 수강생 퇴출
	public void quickStudent(long user_id, long lecture_id) throws DataAccessException;
}
