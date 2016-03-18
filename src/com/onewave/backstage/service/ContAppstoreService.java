package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.ContAppstore;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface ContAppstoreService {
	
	public boolean save(ContAppstore contAppstore);
	
	public boolean update(ContAppstore contAppstore);
	
	public boolean delete(ContAppstore contAppstore);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public ContAppstore findById(String id);
	public List<ContAppstore> findAllByIds(String ids);
	
	public List<ContAppstore> findAll();
	
	public List<ContAppstore> findAll(int firstResult, int maxResults);
	
	public List<ContAppstore> findAll(int firstResult, int maxResults, String type,
			String status, String name);
	
}
