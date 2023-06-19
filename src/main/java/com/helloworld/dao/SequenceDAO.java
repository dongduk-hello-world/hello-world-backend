package com.helloworld.dao;

import org.springframework.dao.DataAccessException;

public interface SequenceDAO {
	public int getNextId(String name) throws DataAccessException;
}