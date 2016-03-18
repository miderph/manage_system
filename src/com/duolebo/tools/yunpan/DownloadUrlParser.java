package com.duolebo.tools.yunpan;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhilink.tv.model.ZlAppDownloadUrl;


public class DownloadUrlParser {
	private static Logger logger = Logger.getLogger(DownloadUrlParser.class);
	
	private static boolean verifyPassword(YunPanParameter param){
		String resp = "";
		String result ="";
		try {
			resp=HttpUtils.getHtml360(param); //获取 host
			logger.info(resp);
			List<Header> headers = new ArrayList<Header>();
			headers.add(new Header("Host",param.getHost360()));
			headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0"));
			headers.add(new Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
			headers.add(new Header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"));
			headers.add(new Header("Content-Type", "application/x-www-form-urlencoded UTF-8; charset=UTF-8"));
			headers.add(new Header("X-Requested-With", "XMLHttpRequest"));
			headers.add(new Header("Referer", param.getShareUrl360()));
			headers.add(new Header("Connection", "keep-alive"));
			headers.add(new Header("Pragma", "no-cache"));
			headers.add(new Header("Cache-Control", "no-cache"));
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			String shorturl = param.getShareUrl360().substring(param.getShareUrl360().lastIndexOf("/")+1,param.getShareUrl360().length());
			pairs.add(new NameValuePair("shorturl", shorturl ));
			pairs.add(new NameValuePair("linkpassword", param.getSharePassword()));
			resp = HttpUtils.post(param.getHost360()+"/share/verifyPassword", headers, pairs, param);
			logger.info("resp="+resp);
			JSONObject json = new JSONObject(resp);
			String errno = json.getString("errno");
			logger.info("errno="+errno+",errmsg="+json.getString("errmsg"));
			if("0".equals(errno)){
//				if(param.getCookies().length>0){//duolebo:zyf 2015-03-24//云盘此步骤不返回cookie了，所以注释掉
//					for(int i =0;i<param.getCookies().length;i++){
//						String [] values= param.getCookies()[i].toString().split("=");
//						if(values[0].indexOf("user_visit_token")>=0){
							return true;
//						}
//					}
//				}
			}
		}catch (Exception e) {
			logger.error(e);
			return false;
		}
		return false;
	}
	
	private static String parseHtml360(YunPanParameter param){
		String resp = "";
		String result ="";
		try {
			resp=HttpUtils.getHtml360(param);
			Pattern p = Pattern.compile("<script>var SYS_CONF =([^>]*);*</script>");
			Matcher m = p.matcher(resp);
			if(m.find()){
				result = m.group(1);  //取匹配到的中间部分的值
				logger.info(result);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	private static String downloadFile(YunPanParameter param ){
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Host",param.getHost360()));
		headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0"));
		headers.add(new Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		headers.add(new Header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"));
		headers.add(new Header("Content-Type", "application/x-www-form-urlencoded UTF-8; charset=UTF-8"));
		headers.add(new Header("X-Requested-With", "XMLHttpRequest"));
		headers.add(new Header("Referer", param.getShareUrl360()));
		headers.add(new Header("Connection", "keep-alive"));
		headers.add(new Header("Pragma", "no-cache"));
		headers.add(new Header("Cache-Control", "no-cache"));
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new NameValuePair("shorturl", param.getShorturl()));
		pairs.add(new NameValuePair("nid", param.getNid()));
		String returnStr="";
		String downloadUrl = "";
		try {
			returnStr = HttpUtils.post(param.getHost360()+YunPanParameter.postUrl_Path_360, headers, pairs,param);
			JSONObject json = new JSONObject(returnStr);
			String errno = json.getString("errno");
			if("0".equals(errno)){
				downloadUrl= json.getJSONObject("data").getString("downloadurl");
				logger.info("downloadUrl="+downloadUrl);
			}
		} catch (IOException e) {
			logger.error(e);
		} catch (JSONException e) {
			logger.error(e);
		}
		return downloadUrl;
	}
	public static String getDownloadUrl360(String url,String password){
		String result ="";
		YunPanParameter param = new YunPanParameter();
		param.setShareUrl360(url);
		param.setSharePassword(password);
		boolean flag = false;
		flag= DownloadUrlParser.verifyPassword(param);
		if(!flag){
			flag = DownloadUrlParser.verifyPassword(param);
		}
		logger.info("flag="+flag);
		if(flag){
			String params = DownloadUrlParser.parseHtml360(param);
			logger.info("params="+params);
			JSONObject json;
			try {
				json = new JSONObject(params);
				param.setShorturl(json.getString("surl"));
				param.setNid(json.getString("nid"));
				result = DownloadUrlParser.downloadFile(param);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return result;
	}
	private static boolean parseHtmlBaidu(YunPanParameter param){
		String resp = "";
		String result = "";
		boolean flag =false;
		try {
			resp=HttpUtils.get(param.getShareUrlBaidu());
//			log.debug(resp);
			Pattern p = Pattern.compile("<script type=\"text/javascript\">var server_filenam([^>]*);</script>");
			Matcher m = p.matcher(resp);
			if(m.find()){
				flag=true;
				result = m.group(1); //取匹配到的中间部分的值
				logger.info(result);
				String[] items = result.split(";");
				String[] temp = null;
				String key="",value="";
				for(String item : items){
					try{
						temp =item.split("=");
						key=temp[0];
						value = temp[1];
						if(value.startsWith("\"")&& value.endsWith("\"")){
							value=temp[1].substring(1,temp[1].length()-1);
						}
					}catch(Exception e){
						logger.error(e);
						continue;
					}
					if("disk.util.ViewShareUtils.bdstoken".equals(key)){
						param.setBdstoken(value);
					}else if("disk.util.ViewShareUtils.fsId".equals(key)){
						param.setFsid(value);
					}else if("FileUtils.share_uk".equals(key)){
						param.setUk(value);
					}else if("FileUtils.share_id".equals(key)){
						param.setShareid(value);
					}else if("FileUtils.share_timestamp".equals(key)){
						param.setTimestamp(value);
					}else if("FileUtils.share_sign".equals(key)){
						param.setSign(value);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}
	private static String postToBaidu(YunPanParameter param ){
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Host","pan.baidu.com"));
		headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0"));
		headers.add(new Header("Accept", "*/*"));
		headers.add(new Header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"));
		headers.add(new Header("Content-Type", "application/x-www-form-urlencoded UTF-8; charset=UTF-8"));
		headers.add(new Header("X-Requested-With", "XMLHttpRequest"));
		headers.add(new Header("Referer", param.getShareUrlBaidu()));
		headers.add(new Header("Connection", "keep-alive"));
		headers.add(new Header("Pragma", "no-cache"));
		headers.add(new Header("Cache-Control", "no-cache"));
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new NameValuePair("fid_list", URLEncoder.encode("[\""+param.getFsid()+"\"]")));
		String returnStr="";
		String downloadUrl = "";
		try {
			String url = YunPanParameter.postUrl_Path_Baidu+"?channel=chunlei&clienttype=0&web=1&uk="+param.getUk()+"&shareid="+param.getShareid()+
			"&timestamp="+param.getTimestamp()+"&sign="+param.getSign()+"&bdstoken=null";
			logger.info(url);
			returnStr = HttpUtils.post(url, headers, pairs,param);
			JSONObject json = new JSONObject(returnStr);
			logger.info(returnStr);
			String errno = json.getString("errno");
			if("0".equals(errno)){
				downloadUrl= json.getString("dlink");
				logger.info(downloadUrl);
			}
		} catch (IOException e) {
			logger.error(e);
		} catch (JSONException e) {
			logger.error(e);
		}
		return downloadUrl;
	}
	public static String getDownloadUrlBaidu(String url){
		String result ="";
		YunPanParameter param = new YunPanParameter();
		param.setShareUrlBaidu(url);
		DownloadUrlParser.parseHtmlBaidu(param);
		result = DownloadUrlParser.postToBaidu(param);
		return result;
	}
	
	public static void main(String args[]){
////		String url360 ="http://yunpan.cn/QpGgsW3AfgBQ3";
//		//String url360 ="http://yunpan.cn/QIWJEAHtmiUbw"; String password = "40fc"; //张月梅用户  访问密码  40fc
////		String url360 ="http://yunpan.cn/Q4srE9aYuqxPd";
//		//String url360 = "http://yunpan.cn/QInjpMVhdLBQx"; String password = "c539";
//		String url360 = "http://yunpan.cn/QNkMXEG6a6kHn"; String password = "8f33";
//		
//		//System.out.println("===="+System.currentTimeMillis()+" "+ZlSoftwareVersion.get360Url(url360).getUpgradeTempUrl());
//		
//		System.out.println("===="+System.currentTimeMillis()+" "+DownloadUrlParser.getDownloadUrl360(url360,password));
//		
//		
//		System.out.println(System.currentTimeMillis());
//		for(int i =0;i<0;i++){
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				//logger.error("message="+E.getMessage(), E);
//			}
//			new Thread("thread "+i){
//				public void run() {
//					String url360 ="http://yunpan.cn/QIWJEAHtmiUbw"; String password = "40fc"; //张月梅用户
//					String url3602 = "http://yunpan.cn/QInjpMVhdLBQx"; String password2 = "c539";
////					String url3602 ="http://yunpan.cn/QzVJdvwFHhVrL";
//					System.out.println("==============================");
////					System.out.println("===="+System.currentTimeMillis()+" "+DownloadUrlParser.getDownloadUrl360(url360,password));
////					System.out.println("===="+System.currentTimeMillis()+" "+DownloadUrlParser.getDownloadUrl360(url3602,password2));
//					System.out.println("===="+System.currentTimeMillis()+getName()+" "+ZlAppDownloadUrl.get360Url(url360,password).getUpgradeTempUrl());
//					System.out.println("===="+System.currentTimeMillis()+getName()+" "+ZlAppDownloadUrl.get360Url(url3602,password2).getUpgradeTempUrl());
//					System.out.println("==============================");
//				};
//			}.start();
//		}
		
	}
}
