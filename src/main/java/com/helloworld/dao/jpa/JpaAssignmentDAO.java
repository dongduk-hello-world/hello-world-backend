package com.helloworld.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.helloworld.dao.AssignmentDAO;
import com.helloworld.domain.Assignment;

@Repository
@Transactional
public class JpaAssignmentDAO implements AssignmentDAO {
	@PersistenceContext
    private EntityManager em;
	
	public void insertAssignment(Assignment assignment) throws DataAccessException {
		em.persist(assignment);
	}

	public void updateAssignment(Assignment assignment) throws DataAccessException {
        em.merge(assignment);
	}

	public void deleteAssignment(Assignment assignment) throws DataAccessException {
        em.remove(assignment);
	}

	public long insertAssignmentAndId(Assignment assignment) throws DataAccessException {
		em.persist(assignment);
      	Assignment result = em.find(Assignment.class, assignment);
      	return result.getAssignment_id();
	}
	
	public Assignment getAssignment(long assignment_id) throws DataAccessException {
		return em.find(Assignment.class, assignment_id);
	}

	public List<Assignment> findByLectureId(long lecture_id) throws DataAccessException {
		Query query = em.createQuery(
                "select a from Assignment a where a.lecture_id = ?1", Assignment.class);
        query.setParameter(1, lecture_id);
        List<Assignment> assignments = query.getResultList();
        
        return assignments;
	}

}
