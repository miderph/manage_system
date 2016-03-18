package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.MenuStructType;
import com.onewave.backstage.model.Operator;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface MenuService {
	
	public boolean save(Menu menu);
	
	public boolean update(Menu menu);
	
	public boolean delete(Menu menu);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public Menu findById(String id);
	
	public List<Menu> findAll();
	
	public List<Menu> findAll(int firstResult, int maxResults);
	
	public List<Menu> findMenu(String site_id, long parent_id, int status);
	
	public List<Menu> findMenu(String site_ids, int parent_id, String name);
	public List<Menu> findMenu(String site_ids, int parent_id, int status, String name,String ignore_id);
	
	public List<MenuStructType> querySiteStatus();
	
	public List<ContProvider> queryContProviderBySiteId(String siteId);
	
	public long findMaxSerialBySite_idAndParent_id(String site_id, String parent_id);
	
	public long modifyBetweenSerialByParentId(String site_id, String parentId, long smallSerial, long largeSerial, String sign);
	
	public boolean copySite(String sourceSiteId,String targetSiteId);
	
	public boolean copymenu(String sourceMenuId,String targetSiteId,String targetParentId,String targetMenuName);

	public List<Menu> findByShortCutId(String id);
	public List<Menu> querySubMenuForMar(long id);
	
}
