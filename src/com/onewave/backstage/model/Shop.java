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
@ITable(name = "ZL_CONT_SHOP")
public class Shop implements Serializable {
	@IId
	private String id; // ID

	private String name; // 名称

	private String credit; // 信用度

	private String icon_url; //图标URL
	private String hot_info; // 描述
	private String intro; // 描述

	@IField(update=false)
	private Date create_time; // 创建时间

	private Date modify_time; // 修改时间

	
	public String getCredit() {
      return credit;
   }

   public void setCredit(String credit) {
      this.credit = credit;
   }

   public String getIcon_url() {
      return icon_url;
   }

   public void setIcon_url(String icon_url) {
      this.icon_url = icon_url;
   }

   public String getHot_info() {
      return hot_info;
   }

   public void setHot_info(String hot_info) {
      this.hot_info = hot_info;
   }

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
