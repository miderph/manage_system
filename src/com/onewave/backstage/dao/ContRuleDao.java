package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContRule;
import com.onewave.common.dao.IBaseDao;

public interface ContRuleDao extends IBaseDao<ContRule, String> {
	
	public List<ContRule> findAllByMenuId(String menuId);
	
}
