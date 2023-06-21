package com.helloworld.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.domain.User;
import com.helloworld.service.UserService;

@RestController
@RequestMapping("/login")
public class LoginController {
	private final UserService userService;
	
	public LoginController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
    public long login(@RequestBody LoginRequest request) {
		String email = request.getEmail();
		String password = request.getPassword();
		
		User user = userService.getUser(email, password);
		
		if(user == null) {
			throw new UserNotFoundException("user not found");
		}
		return user.getUser_id();
    }
}

class LoginRequest {
	String email;
	String password;
	
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
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {
	
    public UserNotFoundException(String message) {
        super(message);
    }
}