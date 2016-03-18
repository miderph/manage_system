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
@ITable(name = "zl_img")
public class Img implements Serializable {

	@IId
	private String id;

	private String use_type;
	private String target_id;
	private String url;
	private String url_little;
	private String url_icon;
	private String intro;
	private String platgroup_id;
	private String provider_id;
	private String is_url_used;

	@IField(update = false)
	private Date create_time;
	private Date modify_time;
	private Date active_time;
	private Date deactive_time;
	private String url_4_squares;
	private String url_6_squares;
	
	private String url_icon_source; //小方图下载源
	private String url_source; //横图/截屏 下载源
	private String locked;//锁定
	
	
	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getUrl_4_squares() {
		return url_4_squares;
	}

	public void setUrl_4_squares(String url_4Squares) {
		url_4_squares = url_4Squares;
	}

	public String getUrl_6_squares() {
		return url_6_squares;
	}

	public void setUrl_6_squares(String url_6Squares) {
		url_6_squares = url_6Squares;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUse_type() {
		return use_type;
	}

	public void setUse_type(String use_type) {
		this.use_type = use_type;
	}

	public String getTarget_id() {
		return target_id;
	}

	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl_little() {
		return url_little;
	}

	public void setUrl_little(String url_little) {
		this.url_little = url_little;
	}

	public String getUrl_icon() {
		return url_icon;
	}

	public void setUrl_icon(String url_icon) {
		this.url_icon = url_icon;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getPlatgroup_id() {
		return platgroup_id;
	}

	public void setPlatgroup_id(String platgroup_id) {
		this.platgroup_id = platgroup_id;
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}

	public String getIs_url_used() {
		return is_url_used;
	}

	public void setIs_url_used(String is_url_used) {
		this.is_url_used = is_url_used;
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

	public String getUrl_icon_source() {
		return url_icon_source;
	}

	public void setUrl_icon_source(String urlIconSource) {
		url_icon_source = urlIconSource;
	}

	public String getUrl_source() {
		return url_source;
	}

	public void setUrl_source(String urlSource) {
		url_source = urlSource;
	}
	
}
