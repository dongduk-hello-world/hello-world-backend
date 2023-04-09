package com.helloworld.global.auth;

public class AuthCode {
	String code;
	
	public AuthCode() {
		code = generateAuthCode();
	}
	String generateAuthCode() {
		return String.valueOf((int)(Math.random()*10000));
	}
	public String toString() {
		return code;
	}
}
