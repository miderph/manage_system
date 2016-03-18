package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.StbModelDao;
import com.onewave.backstage.model.StbModel;
import com.onewave.backstage.service.StbModelService;

@Service("stbModelService")
public class StbModelServiceImpl implements StbModelService {

	@Autowired
	@Qualifier("stbModelDao")
	private StbModelDao stbModelDao;

	public StbModelDao getStbModelDao() {
		return stbModelDao;
	}

	public void setStbModelDao(StbModelDao stbModelDao) {
		this.stbModelDao = stbModelDao;
	}

	public boolean save(StbModel stbModel) {
		stbModel.setCreate_time(new Date());
		stbModel.setModify_time(new Date());
		return stbModelDao.save(stbModel);
	}

	public boolean update(StbModel stbModel) {
		stbModel.setModify_time(new Date());
		return stbModelDao.update(stbModel);
	}

	public boolean delete(StbModel stbModel) {
		return stbModelDao.delete(stbModel);
	}

	public boolean delete(String id) {
		return stbModelDao.deleteById(id);
	}

	public int countAll() {

		return stbModelDao.countAll();
	}

	public StbModel findById(String id) {
		return stbModelDao.findById(id);
	}

	public List<StbModel> findAll() {
		return stbModelDao.findAll();
	}

	public List<StbModel> findAll(int firstResult, int maxResults) {
		return stbModelDao.findAll(firstResult, maxResults);
	}

	public List<StbModel> findAll(String where, String order) {
		return stbModelDao.findAll(where, order);
	}

}
