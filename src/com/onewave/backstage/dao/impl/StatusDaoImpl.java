package com.onewave.backstage.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.StatusDao;
import com.onewave.backstage.model.Status;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("statusDao")
public class StatusDaoImpl extends BaseDaoImpl<Status, String> implements StatusDao {
	
	public List<Status> findStatus(String table_name, String field_name, String exceptValues) {
		
		String where = " table_name='" + table_name + "' and field_name='" + field_name + "' ";
		if (StringUtils.isNotBlank(exceptValues)){
			where += " and STATUS not in (" + exceptValues + ")";
		}
		
		return this.findAll(where, "order by status");
	}
	
}
