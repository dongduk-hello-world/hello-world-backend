package com.helloworld.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TESTCASE")
public class TestCase implements Serializable {
	@Id
	private long testCaseId;
	@Column(name="test_id")
	private long testId;
	
	private String input;
	private String output;
}
