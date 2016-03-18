package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * Site of the concrete class
 * 
 * @author zyf
 * 
 */
@ITable(name = "ZL_CONT_CHANNEL")
public class Channel implements Serializable {
	@IField(save=false)
	final static long serialVersionUID = 1L; 
	@IId
	private String id; // ID

	private String name; // 名称
	private String type; // 类型：1 apk合作渠道,2 内容接入渠道
	private String channel; 

	private String intro; // 描述

	@IField(update=false)
	private Date create_time; // 创建时间
	private Date modify_time; // 修改时间


	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
