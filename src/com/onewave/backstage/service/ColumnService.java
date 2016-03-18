package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Column;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface ColumnService {
	
	public Column findById(String id);
	
	public boolean save(Column column);
	
	public boolean update(Column column);
	
	public boolean delete(String id);
	
	public int countAll(long pid, long status, String site_id);
	
	public List<Column> findAll(long pid, long status, String site_id);
	
	public List<Column> findAll(long pid, String site_ids, String title, String ignore_id, String menuIds);
}
