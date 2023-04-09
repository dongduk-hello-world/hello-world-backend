package com.helloworld.domain.users.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.global.infra.Mailer;

@RestController
@RequestMapping("/email")
public class EmailController {

	@GetMapping("/password/{email}")
    public List<String> findUserandAuthMailSend(@PathVariable("email") String email) {
		// 이메일로 유저 찾고 존재하면
		if (true) {
			Mailer mailer = new Mailer();
			String generatedAuthCode = mailer.sendAuthMail(email);
		
			List<String> message = new ArrayList<>();
			message.add(email);
			message.add(generatedAuthCode);
			
	        return message;
		}
		else return null;
			
    }
	
	@GetMapping("/check/{email}")
    public List<String> duplicateCheckandAuthMailSend(@PathVariable("email") String email) {
		// 이메일로 유저 찾고 존재하지 않으면
		if (true) {
			Mailer mailer = new Mailer();
			String generatedAuthCode = mailer.sendAuthMail(email);
		
			List<String> message = new ArrayList<>();
			message.add(email);
			message.add(generatedAuthCode);
			
	        return message;
		}
		else return null;
    }
}
