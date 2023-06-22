package com.helloworld.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@SequenceGenerator(
		name = "ASSIGNMENT_SEQ_GENERATOR"
	    , sequenceName = "ASSIGNMENT_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
@Table(name="ASSIGNMENT")
public class Assignment implements Serializable {
	@Id
    @GeneratedValue(
        	strategy = GenerationType.SEQUENCE
        	, generator = "ASSIGNMENT_SEQ_GENERATOR"
        )
	private long assignment_id;
	private long writer_id;
	
	@ManyToOne
	@JoinColumn(name="lecture_id")
	private Lecture lecture;
	
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/mm/dd`T`hh:mm:ss", timezone="GMT+9")
	private Date start_time;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/mm/dd`T`hh:mm:ss", timezone="GMT+9")
	private Date end_time;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm", timezone="GMT+9")
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
