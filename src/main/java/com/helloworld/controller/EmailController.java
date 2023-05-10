package com.helloworld.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.service.MailService;

@RestController
@RequestMapping("/email")
public class EmailController {
	
	private final MailService mailService;
	
	public EmailController(MailService mailService) {
		this.mailService = mailService;
	}
	
	@GetMapping("/password/{email}") // 비밀번호 변경을 위한 인증 메일 전송
    public EmailAuthResponse sendAuthMailToUser(@PathVariable String email) {
		// 해당 이메일을 가진 유저를 찾고, 존재한다면 인증 메일을 전송.
		return new EmailAuthResponse(mailService.sendAuthMail(email));
    }
	
	@GetMapping("/check/{email}") // 회원 가입을 위한 이메일 인증 메일 전송
    public EmailAuthResponse sendAuthMailForSignUp(@PathVariable String email) {
		// 해당 이메일을 가진 유저를 찾고, 존재하지 않는다면 인증 메일을 전송.
		return new EmailAuthResponse(mailService.sendAuthMail(email));
    }
}

class EmailAuthRequest{
	String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}

class EmailAuthResponse {
	long id;
	String code;
	
	EmailAuthResponse(String code) {
		this.code = code;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
