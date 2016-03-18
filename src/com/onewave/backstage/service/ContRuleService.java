package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Column;
import com.onewave.backstage.model.ContRule;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface ContRuleService {
	
	public ContRule findById(String id);
	
	public boolean save(ContRule contRule);
	
	public boolean saveAndRef(ContRule contRule,Column column);
	
	public boolean update(ContRule contRule);
	
	public boolean delete(String id);
	
	public boolean deleteAndRef(String id,String menu_id);
	
	public List<ContRule> findAllByMenuId(String rule_ids);
	
}
