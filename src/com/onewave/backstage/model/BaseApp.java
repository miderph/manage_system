package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

//批量导入解析用
public class BaseApp implements Serializable {

	private static final long serialVersionUID = -7249316072030706699L;
	private String app_name;
	private String app_alias;
	private String package_name;
	private String download_url;
	private String version;
	private String staff;
	private String capacity;
	
	private String descrption;
	private String modifyTime;
	private List<String> iconUrls;
	private List<String> screenShots;
	private Map<String,String> iconUrlsMap;
	private Map<String,String> screenShotsMap;
	private Map<String,String> downloadUrlMap;
	private String tags;
	private String rating;
	
	private int downloaded;  // 0 未下载   1 成功  -1 失败
	
	private String md5sum;
	private String provider_id;

	public String getApp_alias() {
		return app_alias;
	}

	public void setApp_alias(String appAlias) {
		app_alias = appAlias;
	}

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<String> getIconUrls() {
		return iconUrls;
	}

	public void setIconUrls(List<String> iconUrls) {
		this.iconUrls = iconUrls;
	}

	public List<String> getScreenShots() {
		return screenShots;
	}

	public void setScreenShots(List<String> screenShots) {
		this.screenShots = screenShots;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
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

	public Map<String, String> getIconUrlsMap() {
		return iconUrlsMap;
	}

	public void setIconUrlsMap(Map<String, String> iconUrlsMap) {
		this.iconUrlsMap = iconUrlsMap;
	}

	public Map<String, String> getScreenShotsMap() {
		return screenShotsMap;
	}

	public void setScreenShotsMap(Map<String, String> screenShotsMap) {
		this.screenShotsMap = screenShotsMap;
	}

	public Map<String, String> getDownloadUrlMap() {
		return downloadUrlMap;
	}

	public void setDownloadUrlMap(Map<String, String> downloadUrlMap) {
		this.downloadUrlMap = downloadUrlMap;
	}

	public int getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}
	
	

}
