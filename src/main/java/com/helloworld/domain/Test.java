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
	@Column(name="test_id")
	private long testId;
	@Column(name="assignment_id")
	private long assignmentId;
	@Column(name="writer_id")
	private String writerId;
	@Column(name="file_id")
	private long fileId;
	
	private String name;
	private String description;
	private float score;
	
	@OneToMany
	@JoinColumn(name="test_id")
	private List<TestCase> testCaseList;

	public Test() {}

	public long getTestId() {
		return testId;
	}
	public void setTestId(long testId) {
		this.testId = testId;
	}
	public long getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}
	public String getWriterId() {
		return writerId;
	}
	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
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
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public List<TestCase> getTestCaseList() {
		return testCaseList;
	}
	public void setTestCaseList(List<TestCase> testCaseList) {
		this.testCaseList = testCaseList;
	}
}
