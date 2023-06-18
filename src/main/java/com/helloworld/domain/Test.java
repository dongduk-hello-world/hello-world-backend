package com.helloworld.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TEST")
public class Test implements Serializable {
	@Id
	private long testId;
	@Column(name="assignment_id")
	private long assignmentId;
	@Column(name="writer_id")
	private long writerId;
	@Column(name="file_id")
	private long fileId;
	
	private String name;
	private String description;
	private float score;
	
	@OneToMany
	@JoinColumn(name="test_id")
	private List<TestCase> testCaseList;
}
