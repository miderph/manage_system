package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.StbPrefix;


/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface StbPrefixService {
	
	public boolean save(StbPrefix stbPrefix);
	
	public boolean update(StbPrefix stbPrefix);
	
//	public boolean update(StbPrefix stbPrefix, String old_stbPrefix, String old_provider_id);
	
	public boolean deleteAndRemove(StbPrefix stbPrefix);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public StbPrefix findById(String id);
	
	public List<StbPrefix> findAll();
	
	public List<StbPrefix> findAll(int firstResult, int maxResults);

	public int countAllUnbound(String providerId);
	
	public List<StbPrefix> findAllUnbound(String providerId);
	
	public boolean isExistPrefix(StbPrefix stbPrefix);
	
	
}
