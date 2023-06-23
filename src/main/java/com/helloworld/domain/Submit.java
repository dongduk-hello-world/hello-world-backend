package com.helloworld.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(
		name = "SUBMIT_SEQ_GENERATOR"
		, sequenceName = "SUBMIT_SEQ"
		, initialValue = 1
		, allocationSize = 1
	)
@Table(name="SUBMIT")
public class Submit implements Serializable, Comparable<Submit> {
	@Id
    @GeneratedValue(
    		strategy = GenerationType.SEQUENCE
    		, generator = "SUBMIT_SEQ_GENERATOR"
    )
	@Column(name="submit_id")
	private long submitId;
	@Column(name="test_id")
	private long testId;
	@Column(name="assignment_id")
	private long assignmentId;
	@Column(name="submitor_id")
	private long submitorId;
	@Column(name="container_id")
	private Long containerId;
	
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
		} else {
			if(o.containerId < containerId) {
				return 1;
			} else if(o.containerId > containerId) {
				return -1;
			}
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
	public long getContainerId() {
		return containerId;
	}
	public void setContainerId(Long containerId) {
		if(containerId == null) {
			this.containerId = Long.parseLong("0");
		} else {
			this.containerId = (Long) containerId;
		}
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
