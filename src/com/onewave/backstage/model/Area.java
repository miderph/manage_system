package com.onewave.backstage.model;

import java.io.Serializable;

import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * 区域
 * 
 * @author zyf
 * 
 */
@SuppressWarnings("serial")
@ITable(name = "zl_area")
public class Area implements Serializable {
	@IId
	private String id; // 区域ID
	private String name; // 区域名称
	private String parent_id; // 所属省ID
	private String pname; // 所属省ID
	private String description; //

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
