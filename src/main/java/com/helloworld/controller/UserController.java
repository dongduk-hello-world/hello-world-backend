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
    public void changePassword(@PathVariable long userId, @RequestBody String password) {
		// request body에 있는 password로 변경
		userService.UpdatePassword(userId, password);
    }

	@GetMapping("/{userId}/subjects")
    public Map<Integer, Map<String, String>> getSubjectList(@PathVariable long userId) {
		Map<String, String> classInfo1 = new HashMap<>();
		Map<Integer, Map<String, String>> classList = new HashMap<>();
//		List<String> classList = new ArrayList<>();
		
		classInfo1.put("classId", "0");
		classInfo1.put("className", "소프트웨어시스템개발");
		classInfo1.put("professor", "박창섭");
		classInfo1.put("period", "2023년 1학기");
		classInfo1.put("divide", "1");
		
		classList.put(0, classInfo1);
		
		Map<String, String> classInfo2 = new HashMap<>();
		
		classInfo2.put("classId", "1");
		classInfo2.put("className", "문제해결기법");
		classInfo2.put("professor", "한혁");
		classInfo2.put("period", "2023년 1학기");
		classInfo2.put("divide", "3");
		
		classList.put(1, classInfo2);
		
		return classList;
    }
	
}