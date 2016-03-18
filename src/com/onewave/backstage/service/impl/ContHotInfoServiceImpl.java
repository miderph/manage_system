package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContHotInfoDao;
import com.onewave.backstage.model.ContHotInfo;
import com.onewave.backstage.service.ContHotInfoService;

@Service("contHotInfoService")
public class ContHotInfoServiceImpl implements ContHotInfoService {

	@Autowired
	private ContHotInfoDao contHotInfoDao;


	public boolean save(ContHotInfo area) {
		// area.setCreate_time(new Date());
		// area.setModify_time(new Date());
		return contHotInfoDao.save(area);
	}

	public boolean update(ContHotInfo area) {
		// area.setModify_time(new Date());
		return contHotInfoDao.update(area);
	}

	public boolean delete(ContHotInfo area) {
		return contHotInfoDao.delete(area);
	}

	public boolean delete(String id) {
		return contHotInfoDao.deleteById(id);
	}

	public int countAll() {
		return contHotInfoDao.countAll();
	}

	public ContHotInfo findById(String id) {
		return contHotInfoDao.findById(id);
	}

	public List<ContHotInfo> findAll() {
		return contHotInfoDao.findAll();
	}

	public List<ContHotInfo> findAll(int firstResult, int maxResults) {
		return contHotInfoDao.findAll(firstResult, maxResults);
	}
	@Override
	public int countHotInfos(String c_id, String channel) {
		return contHotInfoDao.countHotInfos(c_id,channel);
	}
	@Override
	public List<ContHotInfo> findHotInfos(String c_id, String channel) {
		return contHotInfoDao.findHotInfos(c_id,channel);
	}
}



