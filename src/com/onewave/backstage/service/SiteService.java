package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Site;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface SiteService {
	
	public boolean save(Site site);
	
	public boolean update(Site site);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public Site findById(String id);
	
	public List<Site> findAll();
	
	public List<Site> findAll(int firstResult, int maxResults);
	
	public List<Site> findWithAuth(Operator operator);
	
	public String findNames(String ids);
	public List<Site> queryWithAuth(Operator operator);
	public String queryIdsWithAuth(Operator operator);
	public String updateIndex();
}
