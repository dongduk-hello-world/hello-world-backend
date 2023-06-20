package com.helloworld.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.helloworld.domain.Lecture;


public interface LectureDAO {
	public void insertLecture(Lecture lecture) throws DataAccessException;
	
	public void updateLecture(Lecture lecture) throws DataAccessException;
	
	public void deleteLecture(Lecture lecture)throws DataAccessException;

	Lecture getLecture(long lecture_id) throws DataAccessException;
	
	List<Lecture> getLectureByName(String name) throws DataAccessException;

	public void signUpLecture(long lecture_id, long user_id) throws DataAccessException;
}
