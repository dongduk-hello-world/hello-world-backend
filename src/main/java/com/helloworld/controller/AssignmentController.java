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
@RequestMapping("/assignments")
public class AssignmentController {
	
	@PostMapping
    public List<String> add() {
		// assignment 등록
		return null;
    }
	
	@GetMapping("/{assignmentId}")
	public List<String> get(@PathVariable long assignmentId) {
		// assignment 정보 return
		return null;
	}
	@PutMapping("/{assignmentId}")
	public List<String> update(@PathVariable long assignmentId) {
		// assignment 정보 수정
		return null;
	}
	@DeleteMapping("/{assignmentId}")
	public List<String> delete(@PathVariable long assignmentId) {
		// assignment 삭제
		return null;
	}
	
	@GetMapping("/{assignmentId}/tests")
	public List<String> getTestList(@PathVariable long assignmentId) {
		// assignment에 있는 test return
		return null;
	}
	@GetMapping("/{assignmentId}/results")
	public List<String> getResultList(@PathVariable long assignmentId) {
		// assignment의 시험 결과들 return
		return null;
	}
	@PostMapping("/{assignmentId}/results/{userId}")
	public List<String> submit(@PathVariable long assignmentId, @PathVariable long userId) {
		// 시험 최종 제출 완료 후 할 일을 이 곳에 작성
		// ex. session 초기화
		return null;
	}
	@GetMapping("/{assignmentId}/results/{userId}")
	public List<String> addResult(@PathVariable long assignmentId, @PathVariable long userId) {
		// 학생 한 명의 상세 결과를 조회
		return null;
	}
	@GetMapping("/{assignmentId}/results/{userId}/code/{testId}/")
	public List<String> getResultCode(@PathVariable long assignmentId, @PathVariable long userId, @PathVariable long testId) {
		// user가 제출한 코드를 return
		return null;
	}
}

class Test {
	String name;
	String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
