package com.onewave.backstage.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.onewave.backstage.model.AppDownloadUrl;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface AppDownloadUrlService {
	
	public boolean save(AppDownloadUrl appDownloadUrl);
	
	public boolean update(AppDownloadUrl appDownloadUrl);
	
	public boolean delete(AppDownloadUrl appDownloadUrl);
	
	public boolean delete(String id);
	
	public boolean deletebycid(String cid);
	
	public int countAll();
	
	public AppDownloadUrl findById(String id);
	
	public AppDownloadUrl findByCid(String cid,String ctype);
	
	public List<AppDownloadUrl> findByCid(String cid);
	
	public List<AppDownloadUrl> findAll();
	
	public List<AppDownloadUrl> findAll(int firstResult, int maxResults);
	
	public String saveDownloadUrl(String c_id, String urlType, String appDownloadUrl, String sharePasswd, String tempDownloadUrl, Date tempUrlExpireTime, Map<String,String > apkInfo);

	
}
