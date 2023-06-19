package com.helloworld.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LECTURE")
public class Lecture {
	@Id
	private long lecture_id;
	@Column
	private String professor_id;
	
	private int divide;
	private String invite_code;
	private String name;
	private String description;
	private String period;
	private String filter_professor;
	private String filter_term;
	private String filter_language;
}
