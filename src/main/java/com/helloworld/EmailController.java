package com.helloworld;

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

@RestController
public class EmailController {
	
	public int sendAuthMail(String email) {
		int authCode = (int)(Math.random()*10000);
		
		JavaMailSender mailSender = getJavaMailSender();
		
		SimpleMailMessage message = new SimpleMailMessage(); 
		message.setFrom("이메일");
		message.setTo(email); 
		message.setSubject("Hello World 인증 메일입니다."); 
        message.setText("인증 번호는 [" + authCode + "] 입니다.");
        mailSender.send(message);
		
		return (int)(Math.random()*10000);
	}
	
	@GetMapping("email/password/{email}")
    public List<String> duplicateCheck(@PathVariable("email") String email) {
		// 이메일로 유저 찾기

		int generatedAuthCode = sendAuthMail(email);
		
		List<String> message = new ArrayList<>();
		message.add(email);
		message.add(String.valueOf(generatedAuthCode));
		
        return message;
    }
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.naver.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("id");
	    mailSender.setPassword("password");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
}
