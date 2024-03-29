package com.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.domain.User;
import com.helloworld.service.MailService;
import com.helloworld.service.UserService;

@RestController
@RequestMapping("/email")
public class EmailController {
	
	private final MailService mailService;
	private final UserService userService;
	
	public EmailController(MailService mailService, UserService userService) {
		this.mailService = mailService;
		this.userService = userService;
	}
	
	@GetMapping("/{email}") // 해당 email을 가진 유저
    public long findUserByEmail(@PathVariable String email) {
		return userService.getUserByEmail(email);
    }
		
	@GetMapping("/{email}/auth") // 인증 메일 전송
    public String sendAuthMail(@PathVariable String email) {
		return mailService.sendAuthMail(email);
    }
	
}