package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * 热词管理
 * 
 * @author cuihehui
 * 
 */
@SuppressWarnings("serial")
@ITable(name = "zl_hotwords")
public class HotWord implements Serializable {

	
	@IId
   private String id; // ID
   private String hid; // hID指向栏目链接站点的热词id
	private String hotword; // 热词
	private String site_id; // 站点ID
	@IField(update = false)
	private Date create_time; // 创建时间
	private Date modify_time; // 修改时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
   public String getHid() {
      return hid;
   }

   public void setHid(String hid) {
      this.hid = hid;
   }
	public String getHotword() {
		return hotword;
	}

	public void setHotword(String hotword) {
		this.hotword = hotword;
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

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String siteId) {
		site_id = siteId;
	}

}
