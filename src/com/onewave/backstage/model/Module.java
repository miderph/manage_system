package com.onewave.backstage.model;

import java.io.Serializable;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

@ITable(name = "zl_modules")
public class Module implements Serializable {

	@IField(save = false)
	private static final long serialVersionUID = 7433195438760976483L;
	
	@IId
	private String id;
	private String name;
	private String url;
	private String parent_id;
	private String levels;
	private String leaf;
	private String path;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parentId) {
		parent_id = parentId;
	}

	public String getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		this.levels = levels;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
