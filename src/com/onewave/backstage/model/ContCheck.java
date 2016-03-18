package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.Date;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

@ITable(name = "zl_cont_checks")
public class ContCheck implements Serializable {
	@IId
	private String id;
	//商品id
	private String code;
	//商品名称
	private String name;
	//商品主图
	private String icon;
	//商品详情页链接地址
	@IField(title="item_url")
	private String itemUrl;
	//店铺名称
	private String shop;
	//商品价格(单位：元)
	private String price;
	//商品月销量
	private String sales_num;
	//收入比率
	private String bate;
	//卖家旺旺
	private String wangwang;
	//淘宝客短链接(300天内有效)
	private String taobaoke_url_short;
	//淘宝客链接
	private String taobaoke_url;
	// 内容一级分类
	private String classify;
	private long status;
	public static enum EnumStatus{
		UnderShelf(-1,"下架的"), //下架的
		Normal(1,"正常的"); //正常的
		int value;
		String name;
		private EnumStatus(int value,String name) {
			this.value = value;
			this.name = name;
		}
		public int getValue(){
			return value;
		}
		public String getName(){
			return name;
		}
	}
	
	private Date create_time;
	private Date update_time;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getSales_num() {
		return sales_num;
	}

	public void setSales_num(String sales_num) {
		this.sales_num = sales_num;
	}

	public String getBate() {
		return bate;
	}

	public void setBate(String bate) {
		this.bate = bate;
	}

	public String getWangwang() {
		return wangwang;
	}

	public void setWangwang(String wangwang) {
		this.wangwang = wangwang;
	}

	public String getTaobaoke_url_short() {
		return taobaoke_url_short;
	}

	public void setTaobaoke_url_short(String taobaoke_url_short) {
		this.taobaoke_url_short = taobaoke_url_short;
	}

	public String getTaobaoke_url() {
		return taobaoke_url;
	}

	public void setTaobaoke_url(String taobaoke_url) {
		this.taobaoke_url = taobaoke_url;
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

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}
	
	public static void main(String[] args) {
		System.out.println(EnumStatus.Normal.toString());
		System.out.println(EnumStatus.valueOf("Normaaal"));
	}
}
