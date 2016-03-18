package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 平台
 * 
 * @author liuhaidi
 * 
 */
public class Plat implements Serializable {

	private static final long serialVersionUID = 1L;

	private String plat;

	private String name;

	private String platgroup_id;

	private String status;

	private Date create_time;

	private Date modify_time;

	public Plat() {
	}

	public Plat(String plat, String name, String platgroup_id, String status) {
		this.plat = plat;
		this.name = name;
		this.platgroup_id = platgroup_id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlat() {
		return plat;
	}

	public void setPlat(String plat) {
		this.plat = plat;
	}

	public String getPlatgroup_id() {
		return platgroup_id;
	}

	public void setPlatgroup_id(String platgroup_id) {
		this.platgroup_id = platgroup_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
