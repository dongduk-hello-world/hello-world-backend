package com.helloworld.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

	@GetMapping("/password/{email}") // 비밀번호 변경을 위한 인증 메일 전송
    public List<String> sendAuthMailToUser(@PathVariable String email) {
		// 해당 이메일을 가진 유저를 찾고, 존재한다면 인증 메일을 전송.
		return null;
    }
	
	@GetMapping("/check/{email}") // 회원 가입을 위한 이메일 인증 메일 전송
    public List<String> sendAuthMailForSignUp(@PathVariable String email) {
		// 해당 이메일을 가진 유저를 찾고, 존재하지 않는다면 인증 메일을 전송.
		return null;
    }
}
