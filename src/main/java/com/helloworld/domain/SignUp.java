package com.helloworld.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="SIGNUP")
public class SignUp implements Serializable {
	@Id
	private long user_id;
	@Id
	private String lecture_id;
	
	@ManyToMany(mappedBy="signUps")
	private Set<User> users;

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getLecture_id() {
		return lecture_id;
	}

	public void setLecture_id(String lecture_id) {
		this.lecture_id = lecture_id;
	}
	
	
}
