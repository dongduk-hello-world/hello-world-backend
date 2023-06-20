package com.helloworld.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
	
	public String readFileCode(File file) {
		String result = "";
		String path = file.getPath();
		try {
			List<String> lines = Files.readAllLines(Paths.get(path));
			for(String l: lines) {
				result += l + "\r\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
