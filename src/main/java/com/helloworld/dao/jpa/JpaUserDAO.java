package com.helloworld.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.helloworld.dao.UserDAO;
import com.helloworld.domain.Lecture;
import com.helloworld.domain.User;

@Repository
public class JpaUserDAO implements UserDAO {
	@PersistenceContext
    private EntityManager em;
	
	public User getUser(long user_id) throws DataAccessException {
        return em.find(User.class, user_id);         
	}

	public User getUser(long user_id, String password) 
			throws DataAccessException {
		TypedQuery<User> query = em.createQuery(
                                "select u from USERS u "
                                + "where u.user_id=:id and u.password=:pw",
                                User.class);
        query.setParameter("id", user_id);
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
		// 추가
		return null;
	}

	public List<Lecture> getStudentLectureList(long user_id) throws DataAccessException {
		// 추가
		return null;
	}


}