package com.helloworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.dao.LectureDAO;
import com.helloworld.dao.UserDAO;
import com.helloworld.domain.Lecture;
import com.helloworld.domain.SignUp;
import com.helloworld.domain.User;
import com.helloworld.repository.LectureRepository;

@Service
public class LectureService {

	@Autowired
	LectureDAO lectureDao;
	@Autowired
	UserDAO userDao;
	@Autowired
	LectureRepository lectureRepo;
	
	public void createLecture(Lecture lecture) {
		lectureDao.insertLecture(lecture);
	}
	
	public long insertLectureAndId(Lecture lecture) {
		return lectureDao.insertLectureAndId(lecture);
	}
	
	public void updateLecture(Lecture lecture) {
		lectureDao.updateLecture(lecture);
	}
	
	public void deleteLecture(Lecture lecture) {
		lectureDao.deleteLecture(lecture);
	}
	
	// 강의 수강
	public void signUpLecture(long user_id, long lecture_id) {
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
	
	public List<User> getStudent(long lecture_id) {
		List<User> student = null;
		List<Long> studentId = lectureDao.getStudent(lecture_id);
		
		for (int i = 0; i < studentId.size(); i++) {
			student.add(i, userDao.getUser(studentId.get(i)));
		}
			
		return student;
	}
	
	public List<Lecture> findByFilter(String term, String professor, String language) {
		return lectureRepo.findByFiltertermStartingWithOrFilterprofessorStartingWithOrFilterlanguageStartingWith(term, professor, language);
	}
	
	public List<Lecture> findByFilterterm(String term) {
		return lectureRepo.findByFiltertermStartingWith(term);
	}
	
	public List<Lecture> findByFilterprofessor(String professor) {
		return lectureRepo.findByFilterprofessorStartingWith(professor);
	}
	
	public List<Lecture> findByFilterlanguage(String language) {
		return lectureRepo.findByFilterlanguageStartingWith(language);
	}

	public Iterable<Lecture> findAll() {
		return lectureRepo.findAll();
	}
	
	public void withdrawStudent(long student_id, long lecture_id) {
		lectureDao.quickStudent(student_id, lecture_id);
	}
}
