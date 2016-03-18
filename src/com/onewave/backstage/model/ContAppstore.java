package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import net.zhilink.tools.ApkTools;
import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;

import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * Menu of the concrete class
 * 
 * @author liuhaidi
 * 
 */
@SuppressWarnings("serial")
@ITable(name = "zl_cont_appstore")
public class ContAppstore implements Serializable {

	@IId
	private String c_id;
	private String staff;
	private String version;
	private String capacity;
	private Date add_time;
	
	private String download_url;
	private String package_name;
	private String app_name;
	private String md5sum;
	private String provider_id;
	private String tags;
	private String version_code;

	public String getVersion_code() {
		return version_code;
	}

	public void setVersion_code(String versionCode) {
		version_code = versionCode;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getC_id() {
		return c_id;
	}

	public void setC_id(String c_id) {
		this.c_id = c_id;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public Date getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getMd5sum() {
		return md5sum;
	}

	public void setMd5sum(String md5sum) {
		this.md5sum = md5sum;
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}
	
	public void updateApkInfo(){
		//是本地下载地址时，能获取apkinfo
		if(!StringUtils.isBlank(this.getDownload_url()) && !this.getDownload_url().toLowerCase().startsWith("http")){ 
			Map<String, String> info = ApkTools.getApkInfo(InitManager.getRootLocalPath()
					+System.getProperties().getProperty("file.separator")+this.getDownload_url());
			this.setVersion(info.get("version"));
			this.setVersion_code(info.get("versionCode"));
			this.setCapacity(info.get("file_size"));
			this.setPackage_name(info.get("package_name"));
			this.setMd5sum(info.get("md5sum"));
		}
	}
	
	public static void main(String[] args) {
		ContAppstore a = new ContAppstore();
		a.setDownload_url("\\upload\\201405\\215451698.apk");
		a.updateApkInfo();
		System.out.println(a.package_name);
	}

}
