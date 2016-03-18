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
@ITable(name = "zl_cont_video")
public class ContVideo implements Serializable {

	@IId
	private String c_id;
	private int t_region_id;
	private int t_conttype_id;
	private int t_show_years;
	private int t_show_months;

	private int t_quality_type_id;
	private String name;
	private String alias;
	private String description;
	private String duration;

	private String actors;
	private String director;
	private String screenwriter;
	private String language;
	private String has_volume;

	private String vol_total;
	private Date vol_update_time;
	private String prog_type;
	private String play_url;
	@IField(save=false)
	private String link_url;
	private String provider_id;
	private String superscript_id;
	
	public String getSuperscript_id() {
		return superscript_id;
	}

	public void setSuperscript_id(String superscriptId) {
		superscript_id = superscriptId;
	}

	private String package_name;

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public String getC_id() {
		return c_id;
	}

	public void setC_id(String c_id) {
		this.c_id = c_id;
	}

	public int getT_region_id() {
		return t_region_id;
	}

	public void setT_region_id(int t_region_id) {
		this.t_region_id = t_region_id;
	}

	public int getT_conttype_id() {
		return t_conttype_id;
	}

	public void setT_conttype_id(int t_conttype_id) {
		this.t_conttype_id = t_conttype_id;
	}

	public int getT_show_years() {
		return t_show_years;
	}

	public void setT_show_years(int t_show_years) {
		this.t_show_years = t_show_years;
	}

	public int getT_show_months() {
		return t_show_months;
	}

	public void setT_show_months(int t_show_months) {
		this.t_show_months = t_show_months;
	}

	public int getT_quality_type_id() {
		return t_quality_type_id;
	}

	public void setT_quality_type_id(int t_quality_type_id) {
		this.t_quality_type_id = t_quality_type_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getScreenwriter() {
		return screenwriter;
	}

	public void setScreenwriter(String screenwriter) {
		this.screenwriter = screenwriter;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHas_volume() {
		return has_volume;
	}

	public void setHas_volume(String has_volume) {
		this.has_volume = has_volume;
	}

	public String getVol_total() {
		return vol_total;
	}

	public void setVol_total(String vol_total) {
		this.vol_total = vol_total;
	}

	public Date getVol_update_time() {
		return vol_update_time;
	}

	public void setVol_update_time(Date vol_update_time) {
		this.vol_update_time = vol_update_time;
	}

	public String getProg_type() {
		return prog_type;
	}

	public void setProg_type(String prog_type) {
		this.prog_type = prog_type;
	}

	public String getPlay_url() {
		return play_url;
	}

	public void setPlay_url(String play_url) {
		this.play_url = play_url;
	}
	
	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String linkUrl) {
		link_url = linkUrl;
	}
	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}

}
