package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.PlatGroupDao;
import com.onewave.backstage.model.PlatGroup;
import com.onewave.backstage.service.PlatGroupService;

@Service("platGroupService")
public class PlatGroupServiceImpl implements PlatGroupService {
	
	@Autowired
	@Qualifier("platGroupDao")
	private PlatGroupDao platGroupDao;

	public boolean save(PlatGroup platGroup) {
		return platGroupDao.save(platGroup);
	}

	public boolean update(PlatGroup platGroup) {
		return platGroupDao.update(platGroup);
	}

	public boolean delete(PlatGroup platGroup) {
		return platGroupDao.delete(platGroup);
	}

	public boolean deleteById(String id) {
		return platGroupDao.deleteById(id);
	}

	public PlatGroup findById(String id) {
		return platGroupDao.findById(id);
	}

	public int countAll() {
		return platGroupDao.countAll();
	}

	public int countAll(String where) {
		return platGroupDao.countAll(where);
	}

	public int countBySql(String sql) {
		return platGroupDao.countBySql(sql);
	}

	public List<PlatGroup> findAll() {
		return platGroupDao.findAll();
	}

	public List<PlatGroup> findAll(String where) {
		return platGroupDao.findAll(where, null);
	}

	public List<PlatGroup> findAllBySql(String sql) {
		return platGroupDao.findAllBySql(sql);
	}

	public List<PlatGroup> findAllByPage(int pageNum, int pageSize) {
		return platGroupDao.findAllByPage(pageNum, pageSize);
	}

	public List<PlatGroup> findAllByPage(int pageNum, int pageSize, String where) {
		return platGroupDao.findAllByPage(pageNum, pageSize, where);
	}

	public List<PlatGroup> findAll(int firstResult, int maxResults) {
		return platGroupDao.findAll(firstResult, maxResults);
	}

	public List<PlatGroup> findAll(int firstResult, int maxResults, String where) {
		return platGroupDao.findAll(firstResult, maxResults, where);
	}

}
