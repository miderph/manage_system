package com.zhilink.tv.util;


public class RelaObj {
	private static final long serialVersionUID = 1L;

	private long id;

	private String imgUrl;
	private String iconUrl;
	private String landscapeUrl;
	private String portraitUrl;
	private String url_4_squares;
	private String url_6_squares;
	private int is_url_used;
	
	
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

	public int getIs_url_used() {
		return is_url_used;
	}

	public void setIs_url_used(int isUrlUsed) {
		is_url_used = isUrlUsed;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getLandscapeUrl() {
		return landscapeUrl;
	}

	public void setLandscapeUrl(String landscapeUrl) {
		this.landscapeUrl = landscapeUrl;
	}

	public String getPortraitUrl() {
		return portraitUrl;
	}

	public void setPortraitUrl(String portraitUrl) {
		this.portraitUrl = portraitUrl;
	}

}
