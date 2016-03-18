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
@ITable(name = "zl_menu")
public class Menu implements Serializable {
	
	@IId
	private String id;
	
	private String parent_id;
	
	private String title;
	
	//站点ID
	private String site_id;
	
	//栏目排序序号
	private long order_num;
	
	//状态
	private int status;
	
	//栏目结构类型
	private String struct_type;
	
	//栏目下存放资源的类型
	private String resource_type;
	
	//栏目动作类型
	private String act_type;
	
	private String link_url;
   private String is_autoplay;

	
	@IField(update=false)
	private Date create_time; // 创建时间

	private Date modify_time; // 修改时间
	
	private Date active_time;	//生效时间
	
	private Date deactive_time;	//失效时间
	
	//当act_type=getChanne时有效
	private String islocal;
	
	//提供商
	private String provider_id;
	
	private String usergroup_id;
	
	private String is_shortcut;
	
	private String shortcut_contid;
	
	private String version;
	private String usergroup_ids_mac;
	private String usergroup_ids_zone;
	private String usergroup_ids_model;

	public String getUsergroup_ids_mac() {
		return usergroup_ids_mac;
	}
	public void setUsergroup_ids_mac(String usergroup_ids_mac) {
		this.usergroup_ids_mac = usergroup_ids_mac;
	}
	public String getUsergroup_ids_zone() {
		return usergroup_ids_zone;
	}
	public void setUsergroup_ids_zone(String usergroup_ids_zone) {
		this.usergroup_ids_zone = usergroup_ids_zone;
	}
	public String getUsergroup_ids_model() {
		return usergroup_ids_model;
	}
	public void setUsergroup_ids_model(String usergroup_ids_model) {
		this.usergroup_ids_model = usergroup_ids_model;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getShortcut_contid() {
		return shortcut_contid;
	}

	public void setShortcut_contid(String shortcutContid) {
		shortcut_contid = shortcutContid;
	}

	public String getUser_group_id() {
		return usergroup_id;
	}

	public void setUser_group_id(String user_group_id) {
		this.usergroup_id = user_group_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public long getOrder_num() {
		return order_num;
	}

	public void setOrder_num(long order_num) {
		this.order_num = order_num;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStruct_type() {
		return struct_type;
	}

	public void setStruct_type(String struct_type) {
		this.struct_type = struct_type;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public String getAct_type() {
		return act_type;
	}

	public void setAct_type(String act_type) {
		this.act_type = act_type;
	}

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
   public String getIs_autoplay() {
      return is_autoplay;
   }

   public void setIs_autoplay(String is_autoplay) {
      this.is_autoplay = is_autoplay;
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

	public String getIslocal() {
		return islocal;
	}

	public void setIslocal(String islocal) {
		this.islocal = islocal;
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}
	
	public String getIs_shortcut() {
		return is_shortcut;
	}

	public void setIs_shortcut(String isShortcut) {
		is_shortcut = isShortcut;
	}
	
}
