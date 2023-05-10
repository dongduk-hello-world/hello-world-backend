package com.helloworld.controller;

import java.util.List;

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
	
	@PutMapping("/users/{userId}/password")
    public List<String> changePassword(@PathVariable long userId) {
		// request body에 있는 password로 변경
		return null;
    }

	@GetMapping("/users/{userId}/subjects")
    public List<String> getSubjectList(@PathVariable long userId) {
		// 해당 user가 수강하고 있는 과목의 리스트를 반환
		return null;
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