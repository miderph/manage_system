package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Status;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface StatusService {
	
	public List<Status> findAll(String table_name, String field_name, String exceptValues);
	
}
