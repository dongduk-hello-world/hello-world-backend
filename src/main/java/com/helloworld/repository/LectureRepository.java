package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.Lecture;

public interface LectureRepository extends CrudRepository<Lecture, Long> {
	List<Lecture> findByFilter_termStartingWithOrFilter_professorStartingWithOrFilter_languageStartingWith(String filterTerm, String filterProfessor, String filterLanguage);	
}
