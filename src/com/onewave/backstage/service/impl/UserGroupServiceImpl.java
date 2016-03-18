package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.UserGroupDao;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.UserGroupService;

@Service("userGroupService")
public class UserGroupServiceImpl implements UserGroupService {
	
	@Autowired
	@Qualifier("userGroupDao")
	private UserGroupDao userGroupDao;
	
	@Override
	public boolean save(UserGroup menu) {
		menu.setCreate_time(new Date());
		menu.setModify_time(new Date());
		return userGroupDao.save(menu);
	}
	
	@Override
	public boolean update(UserGroup menu) {
		menu.setModify_time(new Date());
		return userGroupDao.update(menu);
	}
	
	@Override
	public boolean delete(UserGroup menu) {
		return userGroupDao.delete(menu);
	}
	
	@Override
	public boolean delete(String id) {
		return userGroupDao.deleteById(id);
	}
	
	@Override
	public int countAll() {
		return userGroupDao.countAll();
	}
	
	@Override
	public UserGroup findById(String id) {
		return userGroupDao.findById(id);
	}
	
	@Override
	public List<UserGroup> findAll() {
		return userGroupDao.findAll();
	}
	
	@Override
	public List<UserGroup> findAll(String ug_type) {
      return userGroupDao.findAll(ug_type);
	}
	
	@Override
	public List<UserGroup> findAll(int start, int end, String name, String type, String value){
		return userGroupDao.findAll(start, end, name, type, value);
	}

	@Override
	public List<UserGroup> findAll(String ug_type, String name,
			int firstResult, int maxResult) {
		return userGroupDao.findAll(ug_type, name, firstResult, maxResult);
	}

	@Override
	public int countAll(String ug_type, String name) {
		return userGroupDao.countAll(ug_type, name);
	}
}
