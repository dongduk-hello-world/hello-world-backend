package com.helloworld.dao;

import org.springframework.dao.DataAccessException;

import com.helloworld.domain.Lecture;


public interface LectureDAO {
	void insertLecture(Lecture lecture) throws DataAccessException;
	
	void updateLecture(Lecture lecture) throws DataAccessException;
	
	public void deleteLecture(Lecture lecture)throws DataAccessException;

	Lecture getLecture(long lecture_id) throws DataAccessException;
	
	// 추가 : 과목 리스트 (query string으로 검색)

}
