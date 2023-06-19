package com.helloworld.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.helloworld.dao.LectureDAO;
import com.helloworld.domain.Assignment;
import com.helloworld.domain.Lecture;

@Repository
public class JpaLectureDAO implements LectureDAO {
	@PersistenceContext
    private EntityManager em;
	
	public void insertLecture(Lecture lecture) throws DataAccessException {
        em.persist(lecture);
	}

	public void updateLecture(Lecture lecture) throws DataAccessException {
		em.merge(lecture);
	}

	public void deleteLecture(Lecture lecture) throws DataAccessException {
		em.remove(lecture);
	}

	public Lecture getLecture(long lecture_id) throws DataAccessException {
		return em.find(Lecture.class, lecture_id);
	}

}
