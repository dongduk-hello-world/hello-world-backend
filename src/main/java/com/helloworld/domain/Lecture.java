package com.helloworld.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(
		name = "LECTURE_SEQ_GENERATOR"
	    , sequenceName = "LECTURE_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
@Table(name="LECTURE")
public class Lecture implements Serializable {
	@Id
    @GeneratedValue(
        	strategy = GenerationType.SEQUENCE
        	, generator = "LECTURE_SEQ_GENERATOR"
        )
	private long lecture_id;
	@Column
	private long professor_id;
	
	private int divide;
	private String invite_code;
	private String name;
	private String description;
	private String period;
	private String filter_professor;
	private String filter_term;
	private String filter_language;
	public long getLecture_id() {
		return lecture_id;
	}
	public void setLecture_id(long lecture_id) {
		this.lecture_id = lecture_id;
	}
	public long getProfessor_id() {
		return professor_id;
	}
	public void setProfessor_id(long professor_id) {
		this.professor_id = professor_id;
	}
	public int getDivide() {
		return divide;
	}
	public void setDivide(int divide) {
		this.divide = divide;
	}
	public String getInvite_code() {
		return invite_code;
	}
	public void setInvite_code(String invite_code) {
		this.invite_code = invite_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getFilter_professor() {
		return filter_professor;
	}
	public void setFilter_professor(String filter_professor) {
		this.filter_professor = filter_professor;
	}
	public String getFilter_term() {
		return filter_term;
	}
	public void setFilter_term(String filter_term) {
		this.filter_term = filter_term;
	}
	public String getFilter_language() {
		return filter_language;
	}
	public void setFilter_language(String filter_language) {
		this.filter_language = filter_language;
	}
	
	
}
