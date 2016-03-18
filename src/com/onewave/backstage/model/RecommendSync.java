package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

@ITable(name = "zl_recommend_sync")
public class RecommendSync implements Serializable {
	@IId
	private String id;
	private String c_id;
	private String name;
	private int status;
	private Date create_time;
	private Date update_time;
	private String sp_code;
	private String extra_params;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String cId) {
		c_id = cId;
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
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date updateTime) {
		update_time = updateTime;
	}
	public String getSp_code() {
		return sp_code;
	}
	public void setSp_code(String spCode) {
		sp_code = spCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExtra_params() {
		return extra_params;
	}
	public void setExtra_params(String extraParams) {
		extra_params = extraParams;
	}
	
}
