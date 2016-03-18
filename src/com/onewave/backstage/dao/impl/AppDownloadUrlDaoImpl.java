package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.AppDownloadUrlDao;
import com.onewave.backstage.model.AppDownloadUrl;
import com.onewave.backstage.model.ContAppstore;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.service.ContAppstoreService;
import com.onewave.backstage.service.ContVideoService;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("appDownloadUrlDao")
public class AppDownloadUrlDaoImpl extends BaseDaoImpl<AppDownloadUrl, String> implements AppDownloadUrlDao {
   @Autowired
   @Qualifier("contAppstoreService")
   private ContAppstoreService contAppstoreService;
   @Autowired
   @Qualifier("contVideoService")
   private ContVideoService contVideoService;
   

   public ContVideoService getContVideoService() {
      return contVideoService;
   }
   public void setContVideoService(ContVideoService contVideoService) {
      this.contVideoService = contVideoService;
   }
   public ContAppstoreService getContAppstoreService() {
      return contAppstoreService;
   }
   public void setContAppstoreService(ContAppstoreService contAppstoreService) {
      this.contAppstoreService = contAppstoreService;
   }

	@Override
	public AppDownloadUrl findByCid(String cid,String ctype) {
		List<AppDownloadUrl> appDownloadUrl = new ArrayList<AppDownloadUrl>();
		if(cid!=null && cid.trim().length()>0) {
			String sql = "select * from ZL_APP_DOWNLOAD_URL c  where c.url_type=0 and c.C_ID = " + cid +" and c.C_TYPE = "+ctype;
			logger.info("------------sql:" + sql);
			appDownloadUrl = findAllBySql(sql);
		}
		if(appDownloadUrl!=null&&appDownloadUrl.size()>0){
			return appDownloadUrl.get(0);
		}else{
			return null;	
		}
	}

	@Override
	public List<AppDownloadUrl> findByCid(String cid) {
		List<AppDownloadUrl> appDownloadUrl = new ArrayList<AppDownloadUrl>();
		if(cid!=null && cid.trim().length()>0) {
			String sql = "select * from ZL_APP_DOWNLOAD_URL c  where c.C_TYPE = 1 and c.C_ID = " + cid;
			logger.info("------------sql:" + sql);
			appDownloadUrl = findAllBySql(sql);
		}
		if(appDownloadUrl!=null&&appDownloadUrl.size()>0){
			return appDownloadUrl;
		}else{
			return null;	
		}
	}
	
	@Override
	public boolean deleteBycid(String cid) {
		boolean result = false;
		if(cid!=null && cid.trim().length()>0) {
			String sql = "delete from ZL_APP_DOWNLOAD_URL t where t.c_id = "+cid;
			result = updateBySql(sql);
		}
		return result;
	}

	public String saveDownloadUrl(String c_id, String urlType, String appDownloadUrl, String sharePasswd, String tempDownloadUrl, Date tempUrlExpireTime, Map<String,String > apkInfo){
		if (appDownloadUrl == null){
			return "地址不能为空！";
		}
		ContAppstore contAppstore = contAppstoreService.findById(c_id);
		if (contAppstore.getPackage_name() != null && !contAppstore.getPackage_name().equals(apkInfo.get("package_name"))){
			return "包名必须相同！原包名="+contAppstore.getPackage_name()+"，新包名="+apkInfo.get("package_name");
		}
		AppDownloadUrl objAppDownloadUrl = null;
		List<AppDownloadUrl> list = findByCid(c_id);
		if (list != null) for (AppDownloadUrl temp : list){
			if ((""+appDownloadUrl).equals(temp.getDownload_url())
					||((""+urlType).equals(temp.getUrl_type())
							&& (""+contAppstore.getProvider_id()).equals(temp.getProvider_id())//后台上传供应商统一用90播亦乐1
							&& (temp.getPackage_name()==null || temp.getPackage_name().equals(apkInfo.get("package_name")))
							&& (temp.getVersion_code()==null || temp.getVersion_code().equals(apkInfo.get("versionCode")))
							&& (temp.getVersion()==null || temp.getVersion().equals(apkInfo.get("version"))))
					){
				objAppDownloadUrl = temp;//有则替换，无则新增
				break;
			}
		}

		if (objAppDownloadUrl == null){
			objAppDownloadUrl = new AppDownloadUrl();
			objAppDownloadUrl.setCreate_time(new Date());
		}

		objAppDownloadUrl.setC_id(c_id);
		objAppDownloadUrl.setC_type("1");
		objAppDownloadUrl.setApp_name(contAppstore.getApp_name());
		objAppDownloadUrl.setProvider_id(contAppstore.getProvider_id());
		objAppDownloadUrl.setPackage_name(apkInfo.get("package_name"));
		objAppDownloadUrl.setVersion(apkInfo.get("version"));
		objAppDownloadUrl.setVersion_code(apkInfo.get("versionCode"));
		objAppDownloadUrl.setCapacity(apkInfo.get("file_size"));
		objAppDownloadUrl.setMd5sum(apkInfo.get("md5sum"));
		objAppDownloadUrl.setAdd_time(new Date());
		objAppDownloadUrl.setModify_time(new Date());
		objAppDownloadUrl.setUrl_type(urlType);
		objAppDownloadUrl.setDownload_url(appDownloadUrl);
		objAppDownloadUrl.setShare_password(sharePasswd);

		objAppDownloadUrl.setUpgrade_temp_url(tempDownloadUrl);
		objAppDownloadUrl.setTemp_url_expire_time(tempUrlExpireTime);

		if (objAppDownloadUrl.getId() == null){
			save(objAppDownloadUrl);
		}
		else{
			update(objAppDownloadUrl);
		}

		contAppstore.setPackage_name(objAppDownloadUrl.getPackage_name());
		contAppstore.setVersion(objAppDownloadUrl.getVersion());
		contAppstore.setVersion_code(objAppDownloadUrl.getVersion_code());
		contAppstore.setCapacity(objAppDownloadUrl.getCapacity());
		contAppstore.setMd5sum(objAppDownloadUrl.getMd5sum());
		//contAppstore.setDownload_url(appDownloadUrl);
		//contAppstore.setApp_name(appName);
		contAppstoreService.update(contAppstore);
		
      ContVideo contVideo = contVideoService.findById(c_id);
      contVideo.setPackage_name(contAppstore.getPackage_name());
      contVideoService.update(contVideo);

		return null;
   }

}
