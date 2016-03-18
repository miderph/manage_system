package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Role;
import com.onewave.common.dao.IBaseDao;


/**
 * The site and database interaction of various interfaces
 * @author liuhaidi
 * @category Dao
 */
public interface RoleDao extends IBaseDao<Role, String> {
	public List<Role> findAll(int firstResult, int maxResults);
	
	public Role findByName(String name);
}
