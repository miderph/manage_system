package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContAppstore;
import com.onewave.common.dao.IBaseDao;

public interface ContAppstoreDao extends IBaseDao<ContAppstore, String> {
	
	public int countAll(String type, String status, String name);
	
	public List<ContAppstore> findAll(int firstResult, int maxResults, String type,
			String status, String name);
	
}
