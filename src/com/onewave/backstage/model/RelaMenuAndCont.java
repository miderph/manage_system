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
@ITable(name = "zl_rela_menu_cont")
public class RelaMenuAndCont implements Serializable {

	@IId
	private String id;

	private String menu_id;

	private String c_id;

	private String admin_id;

	@IField(update = false)
	private Date date_time;

	private String order_num;
	private String epg_id;
	private String prompt;
	private String provider_id;
//	private String update_status;
	private String is_url_used;
	private int locked;
	

	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String providerId) {
		provider_id = providerId;
	}
//
//	public String getUpdate_status() {
//		return update_status;
//	}

//	public void setUpdate_status(String updateStatus) {
//		update_status = updateStatus;
//	}

	public String getIs_url_used() {
		return is_url_used;
	}

	public void setIs_url_used(String isUrlUsed) {
		is_url_used = isUrlUsed;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getC_id() {
		return c_id;
	}

	public void setC_id(String c_id) {
		this.c_id = c_id;
	}

	public String getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public String getEpg_id() {
		return epg_id;
	}

	public void setEpg_id(String epg_id) {
		this.epg_id = epg_id;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
