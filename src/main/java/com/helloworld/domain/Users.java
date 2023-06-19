package com.helloworld.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class Users implements Serializable {
	@Id
	private String user_id;
	
	private String password;
	private String name;
	private String email;
	private String type;
	
	@OneToMany
	@JoinColumn(name="asssignment_id")
	private List<Assignment> assignmentList;
	
	@ManyToMany
	@JoinTable()
	private Set<SignUp> signUps;
}
