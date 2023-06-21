package com.helloworld.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="SIGNUP")
@IdClass(SignUp.class)
public class SignUp implements Serializable {
	@Id
	@Column(name = "user_id")
	private long user_id;
	@Id
	@Column(name = "lecture_id")
	private long lecture_id;
	
	@ManyToMany(mappedBy="signUps")
	private Set<User> users;

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public long getLecture_id() {
		return lecture_id;
	}

	public void setLecture_id(long lecture_id) {
		this.lecture_id = lecture_id;
	}
	
	
}
