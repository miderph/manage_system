package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * Site of the concrete class
 * 
 * @author liuhaidi
 * 
 */
@SuppressWarnings("serial")
@ITable(name = "ZL_SITE")
public class Site implements Serializable {
	@IField(save=false)
	public final static  String LINK_SITE_ID = "0";
	@IId
	private String id; // 站点ID

	private String name; // 站点名称

	private int status; // 站点状态

	// private String img; //图标URL
	private String intro; // 描述

	@IField(update=false)
	private Date create_time; // 创建时间

	private Date modify_time; // 修改时间
	
	private Date active_time;	//生效时间
	
	private Date deactive_time;	//失效时间

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

}
