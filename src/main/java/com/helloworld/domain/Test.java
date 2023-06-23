package com.helloworld.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(
	name = "TESTS_SEQ_GENERATOR"
	, sequenceName = "TESTS_SEQ"
	, initialValue = 1
	, allocationSize = 1
)
@Table(name="TESTS")
public class Test implements Serializable {
	@Id
    @GeneratedValue(
    		strategy = GenerationType.SEQUENCE
    		, generator = "TESTS_SEQ_GENERATOR"
    )
	@Column(name="test_id")
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
	public long getWriterId() {
		return writerId;
	}
	public void setWriterId(long writerId) {
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
