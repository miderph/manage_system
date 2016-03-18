package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ImgDao;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.service.ImgService;

@Service("imgService")
public class ImgServiceImpl implements ImgService {
	
	@Autowired
	@Qualifier("imgDao")
	private ImgDao imgDao;

	public boolean save(Img img) {
		img.setCreate_time(new Date());
		img.setModify_time(new Date());
		return imgDao.save(img);
	}

	public boolean update(Img img) {
		img.setModify_time(new Date());
		return imgDao.update(img);
	}

	public boolean delete(Img img) {
		return imgDao.delete(img);
	}
	

	public boolean deleteAll(String targetId, String useType) {
		return imgDao.deleteAll(targetId,useType);
	}

	public boolean deleteById(String id) {
		return imgDao.deleteById(id);
	}

	public Img findById(String id) {
		return imgDao.findById(id);
	}

	public int countAll() {
		return imgDao.countAll();
	}

	public int countAll(String where) {
		return imgDao.countAll(where);
	}

	public int countBySql(String sql) {
		return imgDao.countBySql(sql);
	}

	public List<Img> findAll() {
		return imgDao.findAll();
	}

	public List<Img> findAll(String where) {
		return imgDao.findAll(where, null);
	}

	public List<Img> findAllBySql(String sql) {
		return imgDao.findAllBySql(sql);
	}

	public List<Img> findAllByPage(int pageNum, int pageSize) {
		return imgDao.findAllByPage(pageNum, pageSize);
	}

	public List<Img> findAllByPage(int pageNum, int pageSize, String where) {
		return imgDao.findAllByPage(pageNum, pageSize, where);
	}

	public List<Img> findAll(int firstResult, int maxResults) {
		return imgDao.findAll(firstResult, maxResults);
	}

	public List<Img> findAll(int firstResult, int maxResults, String where) {
		return imgDao.findAll(firstResult, maxResults, where);
	}
	
	public List<Img> findAll(String targetId, String useType) {
		return imgDao.findAll(targetId, useType);
	}
	
	public String saveAndReturnId(final Img img) {
		img.setCreate_time(new Date());
		img.setModify_time(new Date());
		return imgDao.saveAndReturnId(img);
	}

	@Override
	public Img findByTargetId(String id) {
		return imgDao.findByTargetId(id);
	}

	@Override
	public List<Img> findByIds(String ids) {
		// TODO Auto-generated method stub
		return imgDao.findByIds(ids);
	}
	public boolean updatelocked(String targetId,String useType,String locked) {
		return imgDao.updatelocked(targetId, useType, locked);
	}
	

}
