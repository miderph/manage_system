package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.ContVideo;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface ContVideoService {
	
	public boolean save(ContVideo contVideo);
	
	public boolean update(ContVideo contVideo);
	
	public boolean delete(ContVideo contVideo);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public ContVideo findById(String id);
	
	public List<ContVideo> findAll();
	
	public List<ContVideo> findAll(int firstResult, int maxResults);

	public boolean saveSuperscript(String ids, String contId);

	public List<ContVideo> findByIds(String ids);
	
	public List<ContVideo> findbyid(String id);
	
	
}
