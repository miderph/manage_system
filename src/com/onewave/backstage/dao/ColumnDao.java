package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Column;
import com.onewave.common.dao.IBaseDao;

public interface ColumnDao extends IBaseDao<Column, String> {
	
	public int countAll(long pid, long status, String site_id);
	
	public List<Column> findAll(long pid, long status, String site_id);
	
	public List<Column> findAll(long pid, String site_ids, String title, String ignore_id, String menuIds);
	
}
