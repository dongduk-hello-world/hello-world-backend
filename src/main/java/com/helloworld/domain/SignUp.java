package com.helloworld.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="SIGNUP")
public class SignUp {
	@Id
	private String user_id;
	@Id
	private String lecture_id;
	
	@ManyToMany(mappedBy="signUps")
	private Set<User> users;
}
