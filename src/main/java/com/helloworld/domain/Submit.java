package com.helloworld.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="SUBMIT")
public class Submit implements Serializable {
	@Id
	private long submitId;
	@Column(name="test_id")
	private long testId;
	@Column(name="assignment_id")
	private long assignmentId;
	@Column(name="submitor_id")
	private long submitorId;

	@Column(name="language_type")
	private String languageType;
	@Column(name="run_time")
	private String runtime;
	private float score;
	
	@OneToOne
	@JoinColumn(name="code_id")
	private File code;
}
