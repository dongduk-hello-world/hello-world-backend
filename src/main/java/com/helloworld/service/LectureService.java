package com.helloworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.dao.LectureDAO;
import com.helloworld.domain.Lecture;
import com.helloworld.domain.SignUp;

@Service
public class LectureService {

	@Autowired
	LectureDAO lectureDao;
	
	public void createLecture(Lecture lecture) {
		lectureDao.insertLecture(lecture);
	}
	
	public void updateLecture(Lecture lecture) {
		lectureDao.updateLecture(lecture);
	}
	
	public void deleteLecture(Lecture lecture) {
		lectureDao.deleteLecture(lecture);
	}
	
	// 강의 수강
	public void signUpLecture(String user_id, long lecture_id) {
		lectureDao.signUpLecture(lecture_id, lecture_id);
	}
	
	// 강의 탈퇴
	public void leaveLecture(SignUp signup) {
		lectureDao.leaveLecture(signup);
	}
	
	public List<Lecture> findLectureByName(String name) {
		return lectureDao.getLectureByName(name);
	}
	
	public Lecture getLecture(long lecture_id) {
		return lectureDao.getLecture(lecture_id);
	}
}
