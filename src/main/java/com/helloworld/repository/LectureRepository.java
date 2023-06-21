package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.Lecture;

public interface LectureRepository extends CrudRepository<Lecture, Long> {
	List<Lecture> findByFiltertermStartingWith(String filterTerm);
	List<Lecture> findByFilterprofessorStartingWith(String professor);
	List<Lecture> findByFilterlanguageStartingWith(String language);
	List<Lecture> findByFiltertermStartingWithOrFilterprofessorStartingWithOrFilterlanguageStartingWith(String filterTerm, String filterProfessor, String filterLanguage);	
}
