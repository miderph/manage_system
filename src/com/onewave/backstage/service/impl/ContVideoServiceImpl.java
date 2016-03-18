package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContVideoDao;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.service.ContVideoService;

@Service("contVideoService")
public class ContVideoServiceImpl implements ContVideoService {
	
	@Autowired
	@Qualifier("contVideoDao")
	private ContVideoDao contVideoDao;
	
	public boolean save(ContVideo contVideo) {
		return contVideoDao.save(contVideo);
	}
	
	public boolean update(ContVideo contVideo) {
		return contVideoDao.update(contVideo);
	}
	
	public boolean delete(ContVideo contVideo) {
		return contVideoDao.delete(contVideo);
	}
	
	public boolean delete(String id) {
		return contVideoDao.deleteById(id);
	}
	
	public int countAll() {
		
		return contVideoDao.countAll();
	}
	
	public ContVideo findById(String id) {
		return contVideoDao.findById(id);
	}
	
	public List<ContVideo> findAll() {
		return contVideoDao.findAll();
	}
	
	public List<ContVideo> findAll(int firstResult, int maxResults) {
		return contVideoDao.findAll(firstResult, maxResults);
	}

	@Override
	public boolean saveSuperscript(String ids, String contId) {
		// TODO Auto-generated method stub
		return contVideoDao.saveSuperscript(ids,contId);
	}

	@Override
	public List<ContVideo> findByIds(String ids) {
		// TODO Auto-generated method stub
		return contVideoDao.findByIds(ids);
	}

	@Override
	public List<ContVideo> findbyid(String id) {
		// TODO Auto-generated method stub
		return contVideoDao.findbyid(id);
	}

}
