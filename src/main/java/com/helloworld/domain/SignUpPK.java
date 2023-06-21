package com.helloworld.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class SignUpPK implements Serializable {
	private long user_id;
	private long lecture_id;
}
