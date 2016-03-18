package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.UserGroup;
import com.onewave.common.dao.IBaseDao;

public interface UserGroupDao extends IBaseDao<UserGroup, String> {
	
   public List<UserGroup> findAll(String ug_type);
	public List<UserGroup> findAll(int start, int end, String name, String type, String value);
	public List<UserGroup> findAll(String ug_type, String name, int firstResult, int maxResult);
	public int countAll(String ug_type, String name);

}
