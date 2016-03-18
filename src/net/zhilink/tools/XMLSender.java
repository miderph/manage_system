package net.zhilink.tools;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XMLSender {
	private static Log logger = LogFactory.getLog(XMLSender.class);
	
	public static String sendXml(String xmlStr, String postUrl){
		String returnStr = "";
		try{
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(postUrl);
			System.out.println(xmlStr);
			logger.info("request:" + xmlStr);
			logger.info("url: " + postUrl);
			post.setRequestEntity(new StringRequestEntity(xmlStr,"text/xml","utf-8"));
			int statusCode = httpclient.executeMethod(post);
			if(statusCode == HttpStatus.SC_OK){
				InputStream is = post.getResponseBodyAsStream();
				returnStr = IOUtils.toString(is, "utf-8");
				is.close();
				logger.info(returnStr.substring(0, Math.min(returnStr.length(), 1000)));
			}
			post.releaseConnection();
			httpclient.getHttpConnectionManager().closeIdleConnections(0);
		}catch(Exception e){
			logger.error(e);
		}
		return returnStr;
	}
	
	public static String get(String url) throws Exception{
		return get(url, 0); //默认超时时间
	}

	public static String get(String url,int timeout) throws Exception{
		String returnStr = "";
		HttpClient httpclient=new HttpClient();
		if(timeout > 0){ //默认超时时间
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			httpclient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
		}
		GetMethod get=new GetMethod(url);
		get.addRequestHeader("Content-type" , "text/html; charset=utf-8"); 
		logger.info("get url : " + url);
		
		int statusCode = httpclient.executeMethod(get);
		if(statusCode == HttpStatus.SC_OK){
			InputStream is = get.getResponseBodyAsStream();
			returnStr = IOUtils.toString(is, "utf-8");
			logger.info(returnStr.substring(0, Math.min(returnStr.length(), 1000)));
			is.close();
		}
		get.releaseConnection();
		httpclient.getHttpConnectionManager().closeIdleConnections(0);
		return returnStr.trim();
	}
	
	public static String post(String url,Map params){
		String returnStr = "";
		try{
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			httpclient.getParams().setContentCharset("utf-8");
			NameValuePair[] pairs =null;
			if(params!=null && params.size() > 0){
				pairs = new NameValuePair[params.size()];
				int i =0;
				for(Object key: params.keySet()){
					logger.info(key+"="+params.get(key));
					NameValuePair p = new NameValuePair();
					String k = (String) key;
					p.setName(k);
					p.setValue((String)params.get(k));
					pairs[i] = p;
					i++;
				}
				
			}else{
				pairs = new NameValuePair[0];
			}
			post.addParameters(pairs);
			//post.setRequestBody(pairs); //此时和上一句效果一样
			
			logger.info("url: " + url);
			int statusCode = httpclient.executeMethod(post);
			if(statusCode == HttpStatus.SC_OK){
				InputStream is = post.getResponseBodyAsStream();
				returnStr =  IOUtils.toString(is, "utf-8");
				is.close();
				logger.info(returnStr.substring(0, Math.min(returnStr.length(), 1000)));
			}
			post.releaseConnection();
			httpclient.getHttpConnectionManager().closeIdleConnections(0);
		}catch(Exception e){
			logger.error(e);
		}
		return returnStr;
	}
}
