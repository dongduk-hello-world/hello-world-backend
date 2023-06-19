package com.helloworld.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;

import com.helloworld.domain.Lecture;
import com.helloworld.domain.User;

public interface UserDAO {
	/* user_id로 user 가져오기 */
	User getUser(long user_id) throws DataAccessException;
	/* user_id와 password로 user 가져오기 */
	User getUser(long user_id, String password) throws DataAccessException;
	/* User Create */
	void insertUser(User user) throws DataAccessException;
	/* User Update */
	void updateUser(User user) throws DataAccessException;
	/* password Update */
	void updatePassword(long user_id, String password) throws DataAccessException;
	/* 개설한 강좌 목록 */
	List<Lecture> getProfessorLectureList(long user_id) throws DataAccessException;
	/* 수강중인 강좌 목록 */
	List<Lecture> getStudentLectureList(long user_id) throws DataAccessException;
}
