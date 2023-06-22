package com.helloworld.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(
		name = "FILES_SEQ_GENERATOR"
		, sequenceName = "FILES_SEQ"
		, initialValue = 1
		, allocationSize = 1
	)
@Table(name="FILES")
public class File implements Serializable {
	@Id
    @GeneratedValue(
    		strategy = GenerationType.SEQUENCE
    		, generator = "FILES_SEQ_GENERATOR"
    )
	@Column(name="file_id")
	private long fileId;
	private String name;
	private String path;
	
	public File() {}
	
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
