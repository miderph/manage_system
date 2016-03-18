package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.StbModel;


/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface StbModelService {
	
	public boolean save(StbModel stbModel);
	
	public boolean update(StbModel stbModel);
	
	public boolean delete(StbModel stbModel);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public StbModel findById(String id);
	
	public List<StbModel> findAll();
	
	public List<StbModel> findAll(int firstResult, int maxResults);
	public List<StbModel> findAll(String where, String order);
}
