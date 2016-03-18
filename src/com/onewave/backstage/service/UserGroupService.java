package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.UserGroup;


/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface UserGroupService {
	
	public boolean save(UserGroup menu);
	
	public boolean update(UserGroup menu);
	
	public boolean delete(UserGroup menu);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public UserGroup findById(String id);
	
	public List<UserGroup> findAll();
	
	public List<UserGroup> findAll(String ug_type);

	public List<UserGroup> findAll(int start, int end, String name, String type, String value);
	
	public List<UserGroup> findAll(String ug_type, String name, int firstResult, int maxResult);
	
	public int countAll(String ug_type, String name);

}
