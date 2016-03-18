package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * Menu of the concrete class
 * 
 * @author liuhaidi
 * 
 */
@SuppressWarnings("serial")
@ITable(name = "zl_cont")
public class Cont implements Serializable {

	@IId
	private String id;
	private int type;
	private int ad_type;

	private int status;
	private String name;
	private String description;

	@IField(update=false)
	private Date create_time;
	private Date modify_time;
	private Date active_time;
	private Date deactive_time;
	private String provider_id;
	private String pinyin;

	private String locked;//锁定

	private String usergroup_ids_mac;
	private String usergroup_ids_zone;
	private String usergroup_ids_model;
	private String usergroup_ids_channel;

	private String usergroup_ids_mac2;
	private String usergroup_ids_zone2;
	private String usergroup_ids_model2;
	private String usergroup_ids_channel2;

	private String video_seg_time;


	public String getVideo_seg_time() {
		return video_seg_time;
	}
	public void setVideo_seg_time(String video_seg_time) {
		this.video_seg_time = video_seg_time;
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
	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public int getAd_type() {
		return ad_type;
	}

	public void setAd_type(int ad_type) {
		this.ad_type = ad_type;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public Date getActive_time() {
		return active_time;
	}

	public void setActive_time(Date active_time) {
		this.active_time = active_time;
	}

	public Date getDeactive_time() {
		return deactive_time;
	}

	public void setDeactive_time(Date deactive_time) {
		this.deactive_time = deactive_time;
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}


}
