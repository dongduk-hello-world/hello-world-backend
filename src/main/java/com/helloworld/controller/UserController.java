package com.helloworld.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.domain.Lecture;
import com.helloworld.domain.User;
import com.helloworld.service.UserService;

@CrossOrigin(origins = "http://localhost:3000") 
@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
    public void add(@RequestBody User user) {
		// request body에 있는 정보로 user 등록
		userService.insertUser(user);
    }

	@PutMapping("/{userId}/password")
    public void changePassword(@PathVariable long userId, @RequestBody ChangePasswordRequest request) {
		// request body에 있는 password로 변경
		System.out.println("Sssssss " + request.getPassword());
		userService.UpdatePassword(userId, request.getPassword());
    }

	@GetMapping("/{userId}/subjects")
    public Map<String, List<Lecture>> getSubjectList(@PathVariable long userId) {
		Map<String, List<Lecture>> data = new HashMap<>();
		data.put("classes", userService.getMyLectureByStudent(userId));
		
		return data;
    }
	
}

class ChangePasswordRequest {
	String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}