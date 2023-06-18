package com.helloworld.controller;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Map<String, String> get(@PathVariable long classId) {
		// class의 상세 정보
		Map<String, String> classInfo = new HashMap<>();
		
		classInfo.put("classId", "0");
		classInfo.put("className", "소프트웨어시스템개발");
		classInfo.put("professor", "박창섭");
		classInfo.put("period", "2023년 1학기");
		classInfo.put("divide", "1");
		
		return classInfo;
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
	public List<List<String>> getStudentList(@PathVariable long classId) { 
		// class를 수강하고 있는 student list
		List<List<String>> studentList = new ArrayList<List<String>>();
		
		//임시
		List<String> student1 = new ArrayList<String>();
		List<String> student2 = new ArrayList<String>();
		List<String> student3 = new ArrayList<String>();
		List<String> student4 = new ArrayList<String>();
		
		student1.add("전유영");
		student1.add("20201015");
		studentList.add(student1);
		student2.add("전유영");
		student2.add("20201012");
		studentList.add(student2);
		student3.add("전유영");
		student3.add("20201013");
		studentList.add(student3);
		student4.add("전유영");
		student4.add("20201014");
		studentList.add(student4);
		
		return studentList;
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
