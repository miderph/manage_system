package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * 编排规则
 * @author
 */
@SuppressWarnings("serial")
@ITable(name = "zl_cont_rule")
public class ContRule implements Serializable {
	@IId
	private String id; // 区域ID
	private String name; // 
	private String price; //价格
	private String price_rela; // 取值关系
	private String price_right; //价格2
	private String provider_ids; //
	@IField(update=false, save=false)
	private String provider_names;
	private String provider_rela;
	private String shop_ids;
	@IField(update=false,save=false)
	private String shop_names;
	private String shop_rela;
	private String category;
	private String category_rela;
	private String category_new_menu;
	private Date create_time;
	private Date modify_time;
	
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getPrice_right() {
		return price_right;
	}
	public void setPrice_right(String priceRight) {
		price_right = priceRight;
	}
	public String getProvider_names() {
		return provider_names;
	}
	public void setProvider_names(String providerNames) {
		provider_names = providerNames;
	}
	public String getShop_names() {
		return shop_names;
	}
	public void setShop_names(String shopNames) {
		shop_names = shopNames;
	}
	public String getPrice_rela() {
		return price_rela;
	}
	public void setPrice_rela(String priceRela) {
		price_rela = priceRela;
	}
	public String getProvider_ids() {
		return provider_ids;
	}
	public void setProvider_ids(String providerIds) {
		provider_ids = providerIds;
	}
	public String getProvider_rela() {
		return provider_rela;
	}
	public void setProvider_rela(String providerRela) {
		provider_rela = providerRela;
	}
	public String getShop_ids() {
		return shop_ids;
	}
	public void setShop_ids(String shopIds) {
		shop_ids = shopIds;
	}
	public String getShop_rela() {
		return shop_rela;
	}
	public void setShop_rela(String shopRela) {
		shop_rela = shopRela;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory_rela() {
		return category_rela;
	}
	public void setCategory_rela(String categoryRela) {
		category_rela = categoryRela;
	}
	public String getCategory_new_menu() {
		return category_new_menu;
	}
	public void setCategory_new_menu(String categoryNewMenu) {
		category_new_menu = categoryNewMenu;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modifyTime) {
		modify_time = modifyTime;
	}
}
