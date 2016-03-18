package com.duolebo.tools.yunpan;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;

public class YunPanParameter {
	
//	public static String postUrl_360 ="http://awenrxevje.l11.yunpan.cn/share/downloadfile/";
	public static String postUrl_Path_360 ="/share/downloadfile/";
	public static String postUrl_Path_Baidu = "http://pan.baidu.com/share/download";
	
	private HttpClient httpClient;
	private Cookie[] cookies;
	
	//360云盘相关
	private String shorturl;
	private String nid ;
	private String shareUrl360;
	private String host360;
	private String sharePassword;

	//百度云盘相关
	private String shareUrlBaidu;
	private String channel;
	private String clienttype;
	private String web;
	private String uk;
	private String shareid;
	private String timestamp;
	private String sign;
	private String bdstoken;
	private String fsid;

	public YunPanParameter() {
		this.httpClient = new HttpClient();
	}
	
	
	public String getSharePassword() {
		return sharePassword;
	}


	public void setSharePassword(String sharePassword) {
		this.sharePassword = sharePassword;
	}


	public HttpClient getHttpClient() {
		return httpClient;
	}
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	public String getShareUrl360() {
		return shareUrl360;
	}
	public void setShareUrl360(String shareUrl360) {
		this.shareUrl360 = shareUrl360;
	}
	public String getShorturl() {
		return shorturl;
	}
	public void setShorturl(String shorturl) {
		this.shorturl = shorturl;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	

	public String getHost360() {
		return host360.startsWith("http") ? host360: "http://"+host360;
	}
	public String getHost360WithOutHttp() {
		return host360;
	}
	public void setHost360(String host360) {
		this.host360 = host360;
	}
	
	public String getFsid() {
		return fsid;
	}
	public void setFsid(String fsid) {
		this.fsid = fsid;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getClienttype() {
		return clienttype;
	}
	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public String getUk() {
		return uk;
	}
	public void setUk(String uk) {
		this.uk = uk;
	}
	public String getShareid() {
		return shareid;
	}
	public void setShareid(String shareid) {
		this.shareid = shareid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBdstoken() {
		return bdstoken;
	}
	public void setBdstoken(String bdstoken) {
		this.bdstoken = bdstoken;
	}
	public String getShareUrlBaidu() {
		return shareUrlBaidu;
	}
	public void setShareUrlBaidu(String shareUrlBaidu) {
		this.shareUrlBaidu = shareUrlBaidu;
	}


	public Cookie[] getCookies() {
		return cookies;
	}


	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}
	
}
