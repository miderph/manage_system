package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.StatusDict;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface StatusDictService {
	
	public boolean save(StatusDict statusDict);
	
	public boolean update(StatusDict statusDict);
	
	public boolean delete(StatusDict statusDict);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public StatusDict findById(String id);
	
	public List<StatusDict> findAll();
	
	public List<StatusDict> findAll(int firstResult, int maxResults);
	
	public List<StatusDict> queryStatusDict(String table_name, String field_name);
	
}
