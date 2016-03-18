package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

@ITable(name = "zl_cont_sales_hotinfo")
public class ContHotInfo implements Serializable {
	@IId
	private String id; // 区域ID

	private String c_id;
	private String type;
	private String hot_info;
	private String icon_url;
	private String channel;
	private String description; //

	@IField(update=false)
	private Date create_time;
	private Date modify_time;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String c_id) {
		this.c_id = c_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHot_info() {
		return hot_info;
	}
	public void setHot_info(String hot_info) {
		this.hot_info = hot_info;
	}
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
	
}
