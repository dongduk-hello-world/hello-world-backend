package com.helloworld.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classes")
public class ClassController {
	
	@GetMapping
    public List<String> getClassList() { // query string 추가 필요
		// class list 반환
		// 기본적으로 전체, query string에 따라 검색해서 
		return null;
    }
	
	@PostMapping
    public List<String> add() {
		// request body에 있는 정보로 class 등록
		return null;
    }
	
	@GetMapping("/{classId}")
    public List<String> get(@PathVariable long classId) {
		// class의 상세 정보
		return null;
    }
	@PutMapping("/{classId}")
	public List<String> update(@PathVariable long classId) {
		// class 정보 변경
		return null;
    }
	@DeleteMapping("/{classId}")
	public List<String> delete(@PathVariable long classId) { 
		// class 삭제
		return null;
    }
	
	@GetMapping("/{classId}/assignments")
	public List<String> getAssignmentList(@PathVariable long classId) { 
		// class에 개설된 assignment list
		return null;
    }
	
	@GetMapping("/{classId}/students")
	public List<String> getStudentList(@PathVariable long classId) { 
		// class를 수강하고 있는 student list
		return null;
    }
	@PostMapping("/{classId}/students/{userId}/{code}")
	public List<String> join(@PathVariable long classId, @PathVariable long userId, @PathVariable String code) { 
		// class 수강 등록
		return null;
    }
	@DeleteMapping("/{classId}/students/{userId}")
	public List<String> withdraw(@PathVariable long classId, @PathVariable long userId) { 
		// class 수강 탈퇴
		return null;
    }
	
	
}