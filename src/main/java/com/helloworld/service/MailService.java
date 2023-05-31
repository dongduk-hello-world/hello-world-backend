package com.helloworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {
	
	private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
	private String generateAuthCode() {
		char[] characterTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
				                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
				                'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		String authCode = "";
		for(int i = 0; i < 8; i++) {
			authCode += characterTable[(int) (Math.random()*characterTable.length)];
		}
		return authCode;
	}
	
	private void send(String email, String authCode) {
		SimpleMailMessage message = new SimpleMailMessage(); 
		message.setFrom("ddwu.helloworld@gmail.com");
		message.setTo(email); 
		message.setSubject("Hello World 인증 메일입니다."); 
        message.setText("인증 코드는 [" + authCode + "] 입니다.");
        
        mailSender.send(message);
	}
	
	public String sendAuthMail(String email) {
		String authCode = generateAuthCode();
		
		send(email, authCode);
		
		return authCode;
	}
}
