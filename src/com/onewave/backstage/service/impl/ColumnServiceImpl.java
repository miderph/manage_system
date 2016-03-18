package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ColumnDao;
import com.onewave.backstage.model.Column;
import com.onewave.backstage.service.ColumnService;

@Service("columnService")
public class ColumnServiceImpl implements ColumnService {
	
	@Autowired
	@Qualifier("columnDao")
	private ColumnDao columnDao;
	
	@Override
	public Column findById(String id) {
		return columnDao.findById(id);
	}
	
	@Override
	public boolean save(Column column) {
		column.setCreate_time(new Date());
		column.setModify_time(new Date());
		String id = columnDao.saveAndReturnId(column);
		column.setId(id);
		
		return true;
	}
	
	@Override
	public boolean update(Column column) {
		column.setModify_time(new Date());
		return columnDao.update(column);
	}
	
	@Override
	public boolean delete(String id) {
		return columnDao.deleteById(id);
	}

	@Override
	public List<Column> findAll(long pid, long status, String site_id) {
		return columnDao.findAll(pid, status, site_id);
	}

	@Override
	public int countAll(long pid, long status, String site_id) {
		return columnDao.countAll(pid, status, site_id);
	}

	@Override
	public List<Column> findAll(long pid, String site_ids, String title,
			String ignore_id, String menuIds) {
		return columnDao.findAll(pid, site_ids, title, ignore_id, menuIds);
	}
	
	
}
