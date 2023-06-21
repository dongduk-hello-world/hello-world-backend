package com.helloworld.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(
		name = "USER_SEQ_GENERATOR"
	    , sequenceName = "USER_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
@Table(name="USERS")
public class User implements Serializable {
	@Id
    @GeneratedValue(
        	strategy = GenerationType.SEQUENCE
        	, generator = "USER_SEQ_GENERATOR"
        )

	private long user_id;
	
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

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Assignment> getAssignmentList() {
		return assignmentList;
	}

	public void setAssignmentList(List<Assignment> assignmentList) {
		this.assignmentList = assignmentList;
	}
	
	
}
