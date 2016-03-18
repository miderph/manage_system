package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * 
 * 
 * @author cuihehui
 * 
 */
@SuppressWarnings("serial")
@ITable(name = "zl_app_download_url")
public class AppDownloadUrl implements Serializable {

	@IId
	private String id;
	private String c_id;
	private String app_name;
	private String c_type;
	private String package_name;
	private String provider_id;
	private String version;
	private String version_code;
	private String site;
	private String capacity;
	private String md5sum;
	private Date add_time;
	@IField(update=false)
	private Date create_time;
	private Date modify_time;
	private String download_url;
	private String url_type;
	private String upgrade_temp_url;
	private Date temp_url_expire_time;
	private String share_password;
	
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String cId) {
		c_id = cId;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String appName) {
		app_name = appName;
	}
	public String getC_type() {
		return c_type;
	}
	public void setC_type(String cType) {
		c_type = cType;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String packageName) {
		package_name = packageName;
	}
	public String getProvider_id() {
		return provider_id;
	}
	public void setProvider_id(String providerId) {
		provider_id = providerId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersion_code() {
		return version_code;
	}
	public void setVersion_code(String versionCode) {
		version_code = versionCode;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
   public String getMd5sum() {
      return md5sum;
   }
   public void setMd5sum(String md5sum) {
      this.md5sum = md5sum;
   }
	public Date getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Date addTime) {
		add_time = addTime;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modifyTime) {
		modify_time = modifyTime;
	}
	public String getDownload_url() {
		return download_url;
	}
	public void setDownload_url(String downloadUrl) {
		download_url = downloadUrl;
	}
	public String getUrl_type() {
		return url_type;
	}
	public void setUrl_type(String urlType) {
		url_type = urlType;
	}
	public String getUpgrade_temp_url() {
		return upgrade_temp_url;
	}
	public void setUpgrade_temp_url(String upgradeTempUrl) {
		upgrade_temp_url = upgradeTempUrl;
	}
	public Date getTemp_url_expire_time() {
		return temp_url_expire_time;
	}
	public void setTemp_url_expire_time(Date tempUrlExpireTime) {
		temp_url_expire_time = tempUrlExpireTime;
	}
	public String getShare_password() {
		return share_password;
	}
	public void setShare_password(String sharePassword) {
		share_password = sharePassword;
	}

	

}
