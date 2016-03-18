package com.zhilink.tv.model;

import java.util.Date;

//import com.thoughtworks.xstream.annotations.XStreamAlias;
//import com.thoughtworks.xstream.annotations.XStreamOmitField;


public class CheckUpdate {
	//最新客户端信息                 

   private String id;
	//是否需要升级
//	@XStreamAlias("needupdate")
   private int enforce_flag;

   //最新版本号
//	@XStreamAlias("updateversion")
	private String version_num;
	//最新版本说明
//	@XStreamAlias("updateinfo")
	private String software_info;
	//最新版本升级路径
//	@XStreamAlias("updateurl")
	private String update_url;
	
//	@XStreamOmitField
	private String url_Type;
	
//	@XStreamOmitField
	private String share_password;
	private Date tempUrlExpireTime;

	//	@XStreamOmitField
	private String version_id;
	
//	@XStreamOmitField
	private String url_id;
	
	private String md5;

	//@XStreamOmitField
	private String usergroup_ids_mac;
	//@XStreamOmitField
	private String usergroup_ids_zone;
	//@XStreamOmitField
	private String usergroup_ids_model;
	
	public String getUsergroup_ids_mac() {
		return usergroup_ids_mac;
	}

	public void setUsergroup_ids_mac(String usergroup_ids_mac) {
		this.usergroup_ids_mac = usergroup_ids_mac;
	}

	public String getUsergroup_ids_zone() {
		return usergroup_ids_zone;
	}

	public void setUsergroup_ids_zone(String usergroup_ids_zone) {
		this.usergroup_ids_zone = usergroup_ids_zone;
	}

	public String getUsergroup_ids_model() {
		return usergroup_ids_model;
	}

	public void setUsergroup_ids_model(String usergroup_ids_model) {
		this.usergroup_ids_model = usergroup_ids_model;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getVersion_id() {
		return version_id;
	}
	public void setVersion_id(String versionId) {
		version_id = versionId;
	}
	public String getUrl_id() {
		return url_id;
	}
	public void setUrl_id(String urlId) {
		url_id = urlId;
	}
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }
	public int getEnforce_flag() {
		return enforce_flag;
	}
	public void setEnforce_flag(int enforce_flag) {
		this.enforce_flag = enforce_flag;
	}
	public String getVersion_num() {
		return version_num;
	}
	public void setVersion_num(String version_num) {
		this.version_num = version_num;
	}
	public String getSoftware_info() {
		return software_info;
	}
	public void setSoftware_info(String software_info) {
		this.software_info = software_info;
	}
	public String getUpdate_url() {
		return update_url;
	}
	public void setUpdate_url(String update_url) {
		this.update_url = update_url;
	}
	public String getUrl_Type() {
		return url_Type;
	}
	public void setUrl_Type(String urlType) {
		url_Type = urlType;
	}
	public String getShare_password() {
		return share_password;
	}
	public void setShare_password(String sharePassword) {
		share_password = sharePassword;
	}

	public Date getTempUrlExpireTime() {
		return tempUrlExpireTime;
	}

	public void setTempUrlExpireTime(Date tempUrlExpireTime) {
		this.tempUrlExpireTime = tempUrlExpireTime;
	}
}
