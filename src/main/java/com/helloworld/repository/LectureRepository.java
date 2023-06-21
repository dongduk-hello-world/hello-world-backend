package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.Lecture;

public interface LectureRepository extends CrudRepository<Lecture, Long> {
	List<Lecture> findByFiltertermStartingWithOrFilterprofessorStartingWithOrFilterlanguageStartingWith(String filterTerm, String filterProfessor, String filterLanguage);	
}
