package com.helloworld.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.helloworld.dao.LectureDAO;
import com.helloworld.domain.Lecture;
import com.helloworld.domain.SignUp;

@Repository
@Transactional
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

	public void signUpLecture(long lecture_id, long user_id) throws DataAccessException {
		try {
			Query query = em.createQuery("insert into SignUp values(?2, ?1)");
			query.setParameter(1, lecture_id);
			query.setParameter(2, user_id);
			
			SignUp signUp = (SignUp)query.getSingleResult();
		} catch(NoResultException ex) {
		    	return;
		}
	}

	// 포함 검색(쿼리문이 틀릴 가능성이 높음..)
	public List<Lecture> getLectureByName(String name) throws DataAccessException {
		TypedQuery<Lecture> query = em.createQuery(
                "select l from Lecture l "
                + "where l.name LIKE '?1?'",
                Lecture.class);
		query.setParameter(1, name);
        List<Lecture> lectures = query.getResultList();

        return lectures;
	}

	public void leaveLecture(SignUp signup) throws DataAccessException {
		em.remove(signup);
	}

	public List<Long> getStudent(long lecture_id) throws DataAccessException {
		try {
			Query query = em.createQuery("select user_id from SignUp where lecture_id = ?1");
			query.setParameter(1, lecture_id);
			List<Long> studentId = query.getResultList();
			
			return studentId;
		} catch(NoResultException ex) {
			return null;
		}
	}

	public void quickStudent(long user_id, long lecture_id) throws DataAccessException {
		Query query = em.createQuery("delete SignUp WHERE user_id = ?1 and lecture_id = ?2");
		query.setParameter(1, user_id);
		query.setParameter(2, lecture_id);

	}

}
