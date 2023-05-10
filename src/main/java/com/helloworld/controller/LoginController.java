package com.helloworld.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@PostMapping
    public String login(@RequestBody LoginRequest request) {
		String email = request.getEmail();
		String password = request.getPassword();
		
		if(!(email.equals("1234@dongduk.ac.kr") && password.equals("1234"))) {
			throw new UserNotFoundException("user not found");
		}
		return "성공";
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