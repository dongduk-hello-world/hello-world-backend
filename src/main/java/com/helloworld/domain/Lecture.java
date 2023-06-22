package com.helloworld.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

	@Column(name="filter_professor")
	private String filterprofessor;
	@Column(name="filter_term")
	private String filterterm;
	@Column(name="filter_language")
	private String filterlanguage;
	
	@OneToMany(mappedBy="assignment")
	private List<Assignment> assignmentList;
	
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
	public String getFilterprofessor() {
		return filterprofessor;
	}
	public void setFilterprofessor(String filterprofessor) {
		this.filterprofessor = filterprofessor;
	}
	public String getFilterterm() {
		return filterterm;
	}
	public void setFilterterm(String filterterm) {
		this.filterterm = filterterm;
	}
	public String getFilterlanguage() {
		return filterlanguage;
	}
	public void setFilterlanguage(String filterlanguage) {
		this.filterlanguage = filterlanguage;
	}
}
