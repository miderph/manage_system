package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ModuleDao;
import com.onewave.backstage.model.Module;
import com.onewave.backstage.model.Operator;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("moduleDao")
public class ModuleDaoImpl extends BaseDaoImpl<Module, String> implements ModuleDao {

	@Override
	public List<Module> queryWithAuth(Operator operator) {
		List<Module> moduleList = new ArrayList<Module>();

		if (operator == null)
			return moduleList;

		String sql = "select distinct m.* from zl_operators u,zl_roles r,ZL_MODULES m where u.id='" + operator.getId()
				+ "' and ','||u.role_ids||',' like '%,'||r.id||',%' and ','||r.module_ids||',' like '%,'||m.id||',%' order by m.id asc";
		moduleList = findAllBySql(sql);

		return moduleList;
	}
	
}
