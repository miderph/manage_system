package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Module;
import com.onewave.backstage.model.Operator;
import com.onewave.common.dao.IBaseDao;

public interface ModuleDao extends IBaseDao<Module, String> {
	public List<Module> queryWithAuth(Operator operator);
}
