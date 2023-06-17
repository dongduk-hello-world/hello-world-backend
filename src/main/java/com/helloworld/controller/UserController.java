package com.helloworld.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@PostMapping
    public User add(@RequestBody User user) {
		// request body에 있는 정보로 user 등록
		return user;
    }

	@PutMapping("/{userId}/password")
    public boolean changePassword(@RequestBody String password) {
		// request body에 있는 password로 변경
		System.out.println("password를 " + password + "로 변경했습니다~~!!");
		return true;
    }

	@GetMapping("/{userId}/subjects")
    public Map<Integer, Map<String, String>> getSubjectList(@PathVariable long userId) {
		Map<String, String> classInfo = new HashMap<>();
		Map<Integer, Map<String, String>> classList = new HashMap<>();
//		List<String> classList = new ArrayList<>();
		
		classInfo.put("classId", "1");
		classInfo.put("className", "소프트웨어시스템개발");
		classInfo.put("professor", "박창섭");
		classInfo.put("period", "2023년 1학기");
		classInfo.put("divide", "1");
		
		classList.put(1, classInfo);
		
		classInfo.clear();
		
		classInfo.put("classId", "2");
		classInfo.put("className", "문제해결기법");
		classInfo.put("professor", "한혁");
		classInfo.put("period", "2023년 1학기");
		classInfo.put("divide", "3");
		
		classList.put(2, classInfo);
		
		return classList;
    }
	
}

class User {
	String email;
	String password;
	String name;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}