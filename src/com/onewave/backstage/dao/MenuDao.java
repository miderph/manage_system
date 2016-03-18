package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.MenuStructType;
import com.onewave.common.dao.IBaseDao;

public interface MenuDao extends IBaseDao<Menu, String> {
	
	public List<Menu> findMenu(String site_id, long parent_id, int status);
	
	public List<Menu> findMenu(String site_ids, int parent_id, String name);
	public List<Menu> findMenu(String site_ids, int parent_id, int status, String name,String ignore_id);
	
	public List<MenuStructType> querySiteStatus();
	
	public List<ContProvider> queryContProviderBySiteId(String siteId);
	
	public long findMaxSerialBySite_idAndParent_id(String site_id, String parent_id);
	
	public long modifyBetweenSerialByParentId(String site_id, String parentId, long smallSerial, long largeSerial, String sign);

	public List<Menu> findByShortCutId(String id);

	/**
	 * @param sourceMenuId
	 * @param targetMenuId
	 * @return targetMenuId  是否 sourceMenuId的子栏目或者本身 
	 */
	public boolean isChild(String sourceMenuId,String targetMenuId);
   public List<Menu> querySubMenuForMar(long id);
}
