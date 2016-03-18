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
@ITable(name = "zl_user_group")
public class UserGroup implements Serializable {
	
	@IId
	private String id;
	private String type;
	private String name;
	private String raw_value;
	private String ids_value;
	private String description;
	
	@IField(update=false)
	private Date create_time; // 创建时间

	private Date modify_time; // 修改时间

	public UserGroup(){
		
	}
	/**
	 * @param version_num
	 * @param software_info
	 * @param plat
	 * @param enforce_flag
	 * @param update_url
	 * @param description
	 * @param publish_time
	 */
	public UserGroup(String id, String name, String type,
			String raw_value,String ids_value,String description) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.raw_value = raw_value;
		this.ids_value = ids_value;
		this.description = description;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getRaw_value() {
		return raw_value;
	}

	public void setRaw_value(String raw_value) {
		this.raw_value = raw_value;
	}
	public String getIds_value() {
		return ids_value;
	}
	public void setIds_value(String ids_value) {
		this.ids_value = ids_value;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
//	private Date active_time;	//生效时间
//	private Date deactive_time;	//失效时间

	
}
