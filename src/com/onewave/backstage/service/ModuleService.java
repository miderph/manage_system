package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Module;
import com.onewave.backstage.model.Operator;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface ModuleService {
	
	public List<Module> queryWithAuth(Operator operator);
	
	public List<Module> findAll();
	
}
