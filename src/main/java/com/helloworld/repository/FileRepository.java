package com.helloworld.repository;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.domain.File;

public interface FileRepository extends CrudRepository<File, Long> {
	File findByName(String name);
	File findByPath(String path);
	File findByPathStartingWith(String str);
}
