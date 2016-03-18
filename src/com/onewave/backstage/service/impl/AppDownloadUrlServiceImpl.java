package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.AppDownloadUrlDao;
import com.onewave.backstage.model.AppDownloadUrl;
import com.onewave.backstage.service.AppDownloadUrlService;

@Service("appDownloadUrlService")
public class AppDownloadUrlServiceImpl implements AppDownloadUrlService {
	
	@Autowired
	@Qualifier("appDownloadUrlDao")
	private AppDownloadUrlDao appDownloadUrlDao;
	
	public AppDownloadUrlDao getAppDownloadUrlDao() {
      return appDownloadUrlDao;
   }

   public void setAppDownloadUrlDao(AppDownloadUrlDao appDownloadUrlDao) {
      this.appDownloadUrlDao = appDownloadUrlDao;
   }

   public boolean save(AppDownloadUrl appDownloadUrl) {
		return appDownloadUrlDao.save(appDownloadUrl);
	}
	
	public boolean update(AppDownloadUrl appDownloadUrl) {
		return appDownloadUrlDao.update(appDownloadUrl);
	}
	
	public boolean delete(AppDownloadUrl appDownloadUrl) {
		return appDownloadUrlDao.delete(appDownloadUrl);
	}
	
	public boolean delete(String id) {
		return appDownloadUrlDao.deleteById(id);
	}
	
	public int countAll() {
		
		return appDownloadUrlDao.countAll();
	}
	
	public AppDownloadUrl findById(String id) {
		return appDownloadUrlDao.findById(id);
	}
	
	public List<AppDownloadUrl> findAll() {
		return appDownloadUrlDao.findAll();
	}
	
	public List<AppDownloadUrl> findAll(int firstResult, int maxResults) {
		return appDownloadUrlDao.findAll(firstResult, maxResults);
	}

	@Override
	public AppDownloadUrl findByCid(String cid,String ctype) {
		// TODO Auto-generated method stub
		return appDownloadUrlDao.findByCid(cid,ctype);
	}

	@Override
	public boolean deletebycid(String cid) {
		// TODO Auto-generated method stub
		return appDownloadUrlDao.deleteBycid(cid);
	}

	@Override
	public List<AppDownloadUrl> findByCid(String cid) {
		// TODO Auto-generated method stub
		return appDownloadUrlDao.findByCid(cid);
	}
   @Override
   public String saveDownloadUrl(String c_id, String urlType, String appDownloadUrl, String sharePasswd, String tempDownloadUrl, Date tempUrlExpireTime, Map<String,String > apkInfo){
      return appDownloadUrlDao.saveDownloadUrl(c_id, urlType, appDownloadUrl, sharePasswd, tempDownloadUrl, tempUrlExpireTime, apkInfo);
   }

}
