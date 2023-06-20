package com.helloworld.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="ASSIGNMENT")
public class Assignment implements Serializable {
	@Id
	private long assignment_id;
	
	@ManyToOne
	@JoinColumn(name="lecture_id")
	private Lecture lecture;
	
	@Transient
	private long lecture_id;
	private long writer_id;
	private String name;
	@JsonFormat(pattern="YYYY-MM-DD HH:mm", timezone="GMT+9")
	private Date start_time;
	@JsonFormat(pattern="YYYY-MM-DD HH:mm", timezone="GMT+9")
	private Date end_time;
	@JsonFormat(pattern="HH:mm", timezone="GMT+9")
	private Date test_time;
	
	public Assignment() {}
	
	public long getAssignment_id() {
		return assignment_id;
	}
	public void setAssignment_id(long assignment_id) {
		this.assignment_id = assignment_id;
	}
	public Lecture getLecture() {
		return lecture;
	}
	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}
	public long getLecture_id() {
		return lecture_id;
	}
	public void setLecture_id(long lecture_id) {
		this.lecture_id = lecture_id;
	}
	public long getWriter_id() {
		return writer_id;
	}
	public void setWriter_id(long writer_id) {
		this.writer_id = writer_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public Date getTest_time() {
		return test_time;
	}
	public void setTest_time(Date test_time) {
		this.test_time = test_time;
	}	
}
