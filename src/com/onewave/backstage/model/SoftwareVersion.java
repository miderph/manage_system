package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

@ITable(name = "zl_software_version")
public class SoftwareVersion implements Serializable {

	@IId
	private String ID;
	private String version_num;

	private String software_info;

	private String plat;

	private String enforce_flag;

	private String update_url;

	private String description;

	private Date publish_time;
	// private String usergroup_id;
	private String file_type;
	private String url_type;
	private String status;
	private String upgrade_temp_url;
	private Date temp_url_expire_time;
	private String share_password;

	private String apk_md5sum;
	private String usergroup_ids_mac;
	private String usergroup_ids_zone;
	private String usergroup_ids_model;
	private String usergroup_ids_channel;

	private String usergroup_ids_mac2;
	private String usergroup_ids_zone2;
	private String usergroup_ids_model2;
	private String usergroup_ids_channel2;


	/**
	 * @param version_num
	 * @param software_info
	 * @param plat
	 * @param enforce_flag
	 * @param update_url
	 * @param description
	 * @param publish_time
	 */
	public SoftwareVersion(String id, String version_num, String software_info,
			String plat, String enforce_flag, String update_url,
			String description, Date publish_time, String userGroupIdsMac,
			String userGroupIdsZone, String userGroupIdsModel, String usergroup_ids_hannel
			, String usergroup_ids_mac_black,String usergroup_ids_zone_black,String usergroup_ids_model_black,String usergroup_ids_hannel_black
			, String fileType, String url_type, String status, String share_password, String md5) {
		this.ID = id;
		this.version_num = version_num;
		this.software_info = software_info;
		this.plat = plat;
		this.enforce_flag = enforce_flag;
		this.update_url = update_url;
		this.description = description;
		this.publish_time = publish_time;
		this.usergroup_ids_mac = userGroupIdsMac;
		this.usergroup_ids_zone = userGroupIdsZone;
		this.usergroup_ids_model = userGroupIdsModel;
		this.usergroup_ids_channel = usergroup_ids_channel;
		this.usergroup_ids_mac2 = usergroup_ids_mac2;
		this.usergroup_ids_zone2= usergroup_ids_zone2;
		this.usergroup_ids_model2= usergroup_ids_model2;
		this.usergroup_ids_channel2= usergroup_ids_channel2;

		this.file_type = fileType;
		this.url_type = url_type;
		this.status = status;
		this.share_password = share_password;
		this.apk_md5sum = md5;

	}
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

	public String getUsergroup_ids_channel() {
		return usergroup_ids_channel;
	}

	public void setUsergroup_ids_channel(String usergroup_ids_channel) {
		this.usergroup_ids_channel = usergroup_ids_channel;
	}

	public String getUsergroup_ids_mac2() {
		return usergroup_ids_mac2;
	}

	public void setUsergroup_ids_mac2(String usergroup_ids_mac2) {
		this.usergroup_ids_mac2 = usergroup_ids_mac2;
	}

	public String getUsergroup_ids_zone2() {
		return usergroup_ids_zone2;
	}

	public void setUsergroup_ids_zone2(String usergroup_ids_zone2) {
		this.usergroup_ids_zone2 = usergroup_ids_zone2;
	}

	public String getUsergroup_ids_model2() {
		return usergroup_ids_model2;
	}

	public void setUsergroup_ids_model2(String usergroup_ids_model2) {
		this.usergroup_ids_model2 = usergroup_ids_model2;
	}

	public String getUsergroup_ids_channel2() {
		return usergroup_ids_channel2;
	}

	public void setUsergroup_ids_channel2(String usergroup_ids_channel2) {
		this.usergroup_ids_channel2 = usergroup_ids_channel2;
	}

	public String getMd5() {
		return apk_md5sum;
	}

	public void setMd5(String md5) {
		this.apk_md5sum = md5;
	}

	public String getShare_password() {
		return share_password;
	}

	public void setShare_password(String sharePassword) {
		share_password = sharePassword;
	}

	public SoftwareVersion() {
	}

	public String getUrl_type() {
		return url_type;
	}

	public void setUrl_type(String url_type) {
		this.url_type = url_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersion_num() {
		return version_num;
	}

	public void setVersion_num(String versionNumber) {
		version_num = versionNumber;
	}

	public String getSoftware_info() {
		return software_info;
	}

	public void setSoftware_info(String softwareInfo) {
		software_info = softwareInfo;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPlat() {
		return plat;
	}

	public void setPlat(String plat) {
		this.plat = plat;
	}

	public String getEnforce_flag() {
		return enforce_flag;
	}

	public void setEnforce_flag(String enforceFlag) {
		enforce_flag = enforceFlag;
	}

	public String getUpdate_url() {
		return update_url;
	}

	public void setUpdate_url(String updateUrl) {
		update_url = updateUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(Date publishTime) {
		publish_time = publishTime;
	}

	// public String getUsergroup_id() {
	// return usergroup_id;
	// }
	//
	// public void setUsergroup_id(String usergroup_id) {
	// this.usergroup_id = usergroup_id;
	// }

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public Date getTemp_url_expire_time() {
		return temp_url_expire_time;
	}

	public void setTemp_url_expire_time(Date tempUrlExpireTime) {
		temp_url_expire_time = tempUrlExpireTime;
	}

	public String getUpgrade_temp_url() {
		return upgrade_temp_url;
	}

	public void setUpgrade_temp_url(String upgrateTempUrl) {
		upgrade_temp_url = upgrateTempUrl;
	}
}
