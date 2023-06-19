package com.helloworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.domain.File;
import com.helloworld.repository.FileRepository;

@Service
public class FileService {
	@Autowired
	private FileRepository fileRepo;
	public void setFileRepo(FileRepository repo) {
		this.fileRepo = repo;
	}
	public long insert(File file) {
		File res = fileRepo.save(file);
		return res.getFileId();
	}
}
