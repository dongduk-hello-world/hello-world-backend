package com.helloworld.domain;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="SUBMIT")
public class Submit implements Serializable, Comparable<Submit> {
	@Id
	@Column(name="submit_id")
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
	private float runtime;
	private float score;
	
	@OneToOne
	@JoinColumn(name="code_id", referencedColumnName="file_id")
	private File file;
	
	@Override
	public int compareTo(Submit o) {
		if(o.score < score) {
			return 1;
		} else if (o.score > score) {
			return -1;
		}
		return 0;
	}
	
	public Submit() {}
	
	public long getSubmitId() {
		return submitId;
	}
	public void setSubmitId(long submitId) {
		this.submitId = submitId;
	}
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
	public long getSubmitorId() {
		return submitorId;
	}
	public void setSubmitorId(long submitorId) {
		this.submitorId = submitorId;
	}
	public String getLanguageType() {
		return languageType;
	}
	public void setLanguageType(String languageType) {
		this.languageType = languageType;
	}
	public float getRuntime() {
		return runtime;
	}
	public void setRuntime(float runtime) {
		this.runtime = runtime;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
}
