package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContAppstoreDao;
import com.onewave.backstage.model.ContAppstore;
import com.onewave.backstage.service.ContAppstoreService;

@Service("contAppstoreService")
public class ContAppstoreServiceImpl implements ContAppstoreService {
	
	@Autowired
	@Qualifier("contAppstoreDao")
	private ContAppstoreDao contAppstoreDao;
	
	public boolean save(ContAppstore contAppstore) {
		return contAppstoreDao.save(contAppstore);
	}
	
	public boolean update(ContAppstore contAppstore) {
		return contAppstoreDao.update(contAppstore);
	}
	
	public boolean delete(ContAppstore contAppstore) {
		return contAppstoreDao.delete(contAppstore);
	}
	
	public boolean delete(String id) {
		return contAppstoreDao.deleteById(id);
	}
	
	public int countAll() {
		
		return contAppstoreDao.countAll();
	}
	
	public ContAppstore findById(String id) {
		return contAppstoreDao.findById(id);
	}
	
	public List<ContAppstore> findAll() {
		return contAppstoreDao.findAll();
	}
	
	public List<ContAppstore> findAll(int firstResult, int maxResults) {
		return contAppstoreDao.findAll(firstResult, maxResults);
	}
	
	public List<ContAppstore> findAll(int firstResult, int maxResults, String type,
			String status, String name) {
		return contAppstoreDao.findAll(firstResult, maxResults, type, status, name);
	}

	@Override
	public List<ContAppstore> findAllByIds(String ids) {
		return contAppstoreDao.findAll(" c_id in ( "+ids+" )", "");
	}
	
}
