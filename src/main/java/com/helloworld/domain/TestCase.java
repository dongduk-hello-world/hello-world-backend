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
	@Column(name="testcase_id")
	private long testCaseId;
	@Column(name="test_id")
	private long testId;
	
	private String input;
	private String output;
	
	public TestCase() {}
	
	public long getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(long testCaseId) {
		this.testCaseId = testCaseId;
	}
	public long getTestId() {
		return testId;
	}
	public void setTestId(long testId) {
		this.testId = testId;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
}
