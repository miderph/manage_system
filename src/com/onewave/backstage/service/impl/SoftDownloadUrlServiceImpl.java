package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.SoftDownloadUrlDao;
import com.onewave.backstage.model.SoftDownloadUrl;
import com.onewave.backstage.service.SoftDownlaodUrlService;

@Service("softDownloadUrlService")
public class SoftDownloadUrlServiceImpl implements SoftDownlaodUrlService {
	@Autowired
	@Qualifier("softDownloadUrlDao")
	private SoftDownloadUrlDao softDownloadUrlDao;

	@Override
	public boolean delete(SoftDownloadUrl downloadUrl) {
		return this.softDownloadUrlDao.delete(downloadUrl);
	}

	@Override
	public boolean delete(String id) {
		return this.softDownloadUrlDao.deleteById(id);
	}

	@Override
	public List<SoftDownloadUrl> findByCId(String cIds, String cType) {
		return this.softDownloadUrlDao.findByCId(cIds,cType);
	}

	@Override
	public Map<String, SoftDownloadUrl> findMapByCId(String cIds, String cType) {
		List<SoftDownloadUrl> list = findByCId(cIds, cType);
		Map<String,SoftDownloadUrl> map = new HashMap<String,SoftDownloadUrl>();
		for(SoftDownloadUrl url : list){
			String key = url.getC_type()+"_"+url.getC_id()+"_"+url.getUrl_type();
			map.put(key, url);
		}
		return map;
	}

	@Override
	public SoftDownloadUrl findById(String id) {
		return this.softDownloadUrlDao.findById(id);
	}

	@Override
	public boolean save(SoftDownloadUrl downloadUrl) {
		downloadUrl.setCreate_time(new Date());
		downloadUrl.setModify_time(new Date());
		return this.softDownloadUrlDao.save(downloadUrl);
	}

	@Override
	public boolean update(SoftDownloadUrl downloadUrl) {
		downloadUrl.setModify_time(new Date());
		return this.softDownloadUrlDao.update(downloadUrl);
	}
	
	
}
