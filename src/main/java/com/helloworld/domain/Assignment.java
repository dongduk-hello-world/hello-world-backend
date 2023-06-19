package com.helloworld.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ASSIGNMENT")
public class Assignment implements Serializable {
	@Id
	private String assignment_id;
	@ManyToOne
	@JoinColumn(name="lecture_id")
	private Lecture lecture;

	@Column
	private String writer_id;
	
	String name;
	String start_time;
	String end_time;
	String test_time;
}
