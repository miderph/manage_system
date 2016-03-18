package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Operator;
import com.onewave.common.dao.IBaseDao;

public interface OperatorDao extends IBaseDao<Operator, String> {
	
	public Operator findByName(String name);
	
	public List<Operator> findAll(int firstResult, int maxResults);
	
}
