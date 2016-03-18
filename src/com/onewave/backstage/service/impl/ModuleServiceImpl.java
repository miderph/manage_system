package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ModuleDao;
import com.onewave.backstage.model.Module;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.service.ModuleService;

@Service("moduleService")
public class ModuleServiceImpl implements ModuleService {
	
	@Autowired
	@Qualifier("moduleDao")
	private ModuleDao moduleDao;

	public List<Module> queryWithAuth(Operator operator) {
		return moduleDao.queryWithAuth(operator);
	}

	@Override
	public List<Module> findAll() {
		return moduleDao.findAll(null, "order by id asc");
	}

}
