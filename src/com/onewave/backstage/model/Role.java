package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

@SuppressWarnings("serial")
@ITable(name = "zl_roles")
public class Role implements Serializable {

	@IId
	private String id;
	private String name;
	private String site_ids;
	@IField(update=false, save=false)
	private String site_names;
	
	private String provider_ids;
	@IField(update=false, save=false)
	private String provider_names;
	
	private String module_ids;
	@IField(update=false, save=false)
	private String module_names;
   
   private String menu_ids;
   @IField(update=false, save=false)
   private String menu_names;
   
   private String group_ids;
   @IField(update=false, save=false)
   private String group_names;

   @IField(update = false)
	private Date create_time;
	private Date update_time;
	
	public String getModule_ids() {
		return module_ids;
	}
	public void setModule_ids(String moduleIds) {
		module_ids = moduleIds;
	}
	public String getModule_names() {
		return module_names;
	}
	public void setModule_names(String moduleNames) {
		module_names = moduleNames;
	}
	
	public String getSite_names() {
		return site_names;
	}
	public void setSite_names(String siteNames) {
		site_names = siteNames;
	}
	public String getProvider_names() {
		return provider_names;
	}
	public void setProvider_names(String providerNames) {
		provider_names = providerNames;
	}
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
	
	public String getSite_ids() {
		return site_ids;
	}
	public void setSite_ids(String siteIds) {
		site_ids = siteIds;
	}
	public String getProvider_ids() {
		return provider_ids;
	}
	public void setProvider_ids(String providerIds) {
		provider_ids = providerIds;
	}
   
   public String getMenu_ids() {
      return menu_ids;
   }
   public void setMenu_ids(String menu_ids) {
      this.menu_ids = menu_ids;
   }
   public String getMenu_names() {
      return menu_names;
   }
   public void setMenu_names(String menu_names) {
      this.menu_names = menu_names;
   }
   public String getGroup_ids() {
      return group_ids;
   }
   public void setGroup_ids(String group_ids) {
      this.group_ids = group_ids;
   }
   public String getGroup_names() {
      return group_names;
   }
   public void setGroup_names(String group_names) {
      this.group_names = group_names;
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
	
	
}
