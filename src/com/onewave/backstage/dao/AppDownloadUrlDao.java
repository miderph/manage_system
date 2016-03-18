package com.onewave.backstage.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.onewave.backstage.model.AppDownloadUrl;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContVideo;
import com.onewave.common.dao.IBaseDao;

public interface AppDownloadUrlDao extends IBaseDao<AppDownloadUrl, String> {
	
	public AppDownloadUrl findByCid(String cid,String ctype);
	
	public List<AppDownloadUrl> findByCid(String cid);
	
	public boolean deleteBycid(String cid);

   public String saveDownloadUrl(String c_id, String urlType, String appDownloadUrl, String sharePasswd, String tempDownloadUrl, Date tempUrlExpireTime, Map<String,String > apkInfo);

}
