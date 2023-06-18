package com.helloworld.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="FILE")
public class File implements Serializable {
	@Id
	private long fileId;
	private String name;
	private String path;
}
