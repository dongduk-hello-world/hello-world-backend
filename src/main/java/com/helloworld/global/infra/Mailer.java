package com.helloworld.global.infra;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.helloworld.global.auth.AuthCode;

@Configuration
@PropertySource("classpath:mailSender.properties")
public class Mailer {
	@Autowired
	private Environment environment;
	
	JavaMailSender mailSender;

	public Mailer() {
		mailSender = getJavaMailSender();
	}
	
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(environment.getProperty("spring.mail.host"));
	    mailSender.setPort(Integer.valueOf(environment.getProperty("spring.mail.port")));
	    
	    mailSender.setUsername(environment.getProperty("spring.mail.username"));
	    mailSender.setPassword(environment.getProperty("spring.mail.password"));
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
	
	public String sendAuthMail(String email) {
		String authCode = new AuthCode().toString();
		
		JavaMailSender mailSender = getJavaMailSender();
		
		SimpleMailMessage message = new SimpleMailMessage(); 
		message.setFrom(environment.getProperty("spring.mail.username"));
		message.setTo(email); 
		message.setSubject("Hello World 인증 메일입니다."); 
        message.setText("인증 번호는 [" + authCode + "] 입니다.");
        mailSender.send(message);
        
        return authCode;
        
	}
	
}
