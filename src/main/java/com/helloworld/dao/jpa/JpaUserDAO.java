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

import com.helloworld.dao.UserDAO;
import com.helloworld.domain.Lecture;
import com.helloworld.domain.User;

@Repository
@Transactional
public class JpaUserDAO implements UserDAO {
	@PersistenceContext
    private EntityManager em;
	
	public User getUser(long user_id) throws DataAccessException {
        return em.find(User.class, user_id);         
	}

	public User getUser(String email, String password) 
			throws DataAccessException {
		TypedQuery<User> query = em.createQuery(
                                "select u from USERS u "
                                + "where u.email=:email and u.password=:pw",
                                User.class);
        query.setParameter("id", email);
        query.setParameter("pw", password);
        User user = null;
        try {
        	user = query.getSingleResult();
        } catch(NoResultException ex) {
        	return null;
        }
        return user;		
	}

	public void insertUser(User user) throws DataAccessException {
		// 일단 시퀀스 적용 X
        em.persist(user);
	}

	public void updateUser(User user) throws DataAccessException {
        em.merge(user);
	}

	public void updatePassword(long user_id, String password) throws DataAccessException {
		try {
			Query query = em.createQuery("update USERS u SET u.password = ?1 WHERE u.user_id = ?2");
			query.setParameter(1, password);
			query.setParameter(2, user_id);
			
			User u = (User)query.getSingleResult();
		} catch(NoResultException ex) {
        	return;
		}
	}

	public List<Lecture> getProfessorLectureList(long user_id) throws DataAccessException {
		TypedQuery<Lecture> query = em.createQuery(
                "select l from LECTURE l "
                + "where l.professor_id=:id",
                Lecture.class);
		query.setParameter("id", user_id);
        List<Lecture> lectures = query.getResultList();

        return lectures;
	}

	public List<String> getStudentLectureList(long user_id) throws DataAccessException {
		Query query = em.createQuery("select SIGNUP lecture_id WHERE user_id = ?1");
		query.setParameter(1, user_id);
		List<String> lectures_id = query.getResultList();
		
		return lectures_id;
	}

	public long getUserByEmail(String email) throws DataAccessException {
        try {
    		User result = em.find(User.class, email);
    		return result.getUser_id();
        } catch(NoResultException ex) {
        	return 0;
        }
	}


}