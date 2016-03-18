package com.onewave.backstage.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.StatusDictDao;
import com.onewave.backstage.model.StatusDict;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("statusDictDao")
public class StatusDictDaoImpl extends BaseDaoImpl<StatusDict, String> implements StatusDictDao {
	
	public List<StatusDict> queryStatusDict(String table_name, String field_name) {
		
		String where = " table_name='" + table_name + "' and field_name='" + field_name + "' ";
		
		return this.findAll(where, " order by status");
	}
	
}
