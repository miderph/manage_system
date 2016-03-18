package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Operator;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface OperatorService {
	
	public boolean save(Operator operator);
	
	public boolean update(Operator operator);
	
	public boolean delete(String id);
	
	public Operator findById(String id);
	
	public Operator findByName(String name);
	
	public int countAll();
	
	public List<Operator> findAll(int firstResult, int maxResults);
	
	public boolean isExist(String name);
	
	public boolean roleIsUsed(String id);
	
}
