package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.StatusDictDao;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.service.StatusDictService;

@Service("statusDictService")
public class StatusDictServiceImpl implements StatusDictService {
	
	@Autowired
	@Qualifier("statusDictDao")
	private  StatusDictDao statusDictDao;
	
	public boolean save( StatusDict statusDict) {
		statusDict.setCreate_time(new Date());
		statusDict.setModify_time(new Date());
		return statusDictDao.save(statusDict);
	}
	
	public boolean update( StatusDict statusDict) {
		statusDict.setModify_time(new Date());
		return statusDictDao.update(statusDict);
	}
	
	public boolean delete( StatusDict statusDict) {
		return statusDictDao.delete(statusDict);
	}
	
	public boolean delete(String id) {
		return statusDictDao.deleteById(id);
	}
	
	public int countAll() {
		
		return statusDictDao.countAll();
	}
	
	public  StatusDict findById(String id) {
		return statusDictDao.findById(id);
	}
	
	public List< StatusDict> findAll() {
		return statusDictDao.findAll();
	}
	
	public List< StatusDict> findAll(int firstResult, int maxResults) {
		return statusDictDao.findAll(firstResult, maxResults);
	}

	public List<StatusDict> queryStatusDict(String table_name, String field_name) {
		return statusDictDao.queryStatusDict(table_name, field_name);
	}

}
