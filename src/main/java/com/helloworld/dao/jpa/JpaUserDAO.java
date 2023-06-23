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
                                "select u from User u "
                                + "where u.email=:email and u.password=:pw",
                                User.class);
        query.setParameter("email", email);
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
		em.persist(user);
	}

	public void updateUser(User user) throws DataAccessException {
        em.merge(user);
	}

	public void updatePassword(long user_id, String password) throws DataAccessException {
		User u = em.find(User.class, user_id);
		u.setPassword(password);
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

	public List<Long> getStudentLectureList(long user_id) throws DataAccessException {
		List<Long> lectures_id = null;
		try {
			Query query = em.createQuery("select s.lecture_id from SignUp s WHERE s.user_id = ?1");
			query.setParameter(1, user_id);
			lectures_id = query.getResultList();
			
			return lectures_id;
		} catch (NoResultException ex) {
			return lectures_id;
		}
	}

	public long getUserByEmail(String email) throws DataAccessException {
        try {
    		Query query = em.createQuery("select u From User u WHERE email = ?1");
    		query.setParameter(1, email);
    		
    		User u = (User)query.getSingleResult();
    		return u.getUser_id();
        } catch(NoResultException ex) {
        	return 0;
        }
	}


}