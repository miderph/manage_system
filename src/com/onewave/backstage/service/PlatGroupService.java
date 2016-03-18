package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.PlatGroup;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface PlatGroupService {
	
	public boolean save(PlatGroup platGroup);
	
	public boolean update(PlatGroup platGroup);
	
	public boolean delete(PlatGroup platGroup);
	
	public boolean deleteById(String id);
	
	public PlatGroup findById(String id);
	
	public int countAll();
	
	public int countAll(String where);
	
	public int countBySql(String sql);
	
	public List<PlatGroup> findAll();
	
	public List<PlatGroup> findAll(String where);
	
	public List<PlatGroup> findAllBySql(String sql);
	
	public List<PlatGroup> findAllByPage(int pageNum, int pageSize);
	
	public List<PlatGroup> findAllByPage(int pageNum, int pageSize, String where);
	
	public List<PlatGroup> findAll(int firstResult, int maxResults);
	
	public List<PlatGroup> findAll(int firstResult, int maxResults, String where);
	
}
