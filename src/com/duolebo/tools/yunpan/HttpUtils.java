package com.duolebo.tools.yunpan;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.tag.common.core.ParamSupport;

public class HttpUtils {
	private static Logger log = Logger.getLogger(HttpUtils.class);

	public static String post(String url, List<Header> headers,
			List<NameValuePair> nameValuePairs, YunPanParameter param)
			throws IOException {

		HttpClient client = param.getHttpClient();
		if (null == client) {
			client = new HttpClient();
		}
		client.getHostConfiguration().setHost(param.getHost360(), 80);

		PostMethod post = new PostMethod(url);
		//必须设置，设置后才能取到服务器写入请求头的cookie
		post.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置请求头
		if (headers != null) {
			for (Header header : headers) {
				post.setRequestHeader(header);
			}
		}
		// 填入各个表单域的值
		NameValuePair[] data = null;
		if (null != nameValuePairs) {
			data = (NameValuePair[]) nameValuePairs
					.toArray(new NameValuePair[nameValuePairs.size()]);
		} else {
			data = new NameValuePair[0];
		}
		// 将表单的值放入postMethod中
		post.setRequestBody(data);
		// 执行postMethod
		int statusCode = client.executeMethod(post);
		log.info(" status code:" + statusCode);
		String returnStr = "";
		if (statusCode == HttpStatus.SC_OK) {
			InputStream is = post.getResponseBodyAsStream();
			returnStr = IOUtils.toString(is, "UTF-8");
			is.close();
			log.info(returnStr.trim());
		}
		// 查看 cookie 信息
//		DefaultHttpParams.getDefaultParams().setParameter(
//				"http.protocol.cookie-policy",
//				CookiePolicy.BROWSER_COMPATIBILITY);
		CookieSpec cookiespec = CookiePolicy.getDefaultSpec();

		Cookie[] cookies =cookiespec.match(param.getHost360(), 80, "/", true,client.getState().getCookies());
		param.setCookies(cookies);
		if (cookies.length == 0) {
			log.info("cookie: None1");
		} else {
			for (int i = 0; i < cookies.length; i++) {
				log.info("cookie " + cookies[i].toString());
			}
		}
		
		post.releaseConnection();
		client.getHttpConnectionManager().closeIdleConnections(0);
		return returnStr.trim();
	}

	public static String get(String url) throws Exception {
		log.info(url);
		String returnStr = "";
		HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(url);
		get.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
		int statusCode = httpclient.executeMethod(get);
		if (statusCode == HttpStatus.SC_OK) {
			InputStream is = get.getResponseBodyAsStream();
			returnStr = IOUtils.toString(is, "utf-8");
			is.close();
		}
		get.releaseConnection();
		httpclient.getHttpConnectionManager().closeIdleConnections(0);
		int length = returnStr.length() > 1000 ? 1000 : returnStr.length();
		log.info(returnStr.trim().substring(0, length));
		return returnStr.trim();
	}

	public static String getHtml360(YunPanParameter param) throws Exception {
		String returnStr = "";
		HttpClient httpclient = param.getHttpClient();
		GetMethod get = new GetMethod(param.getShareUrl360());
		get.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		get.getParams().setParameter("http.protocol.single-cookie-header",true);
		log.info("get " + param.getShareUrl360());
		int statusCode = httpclient.executeMethod(get);
		System.out.println(getCookieStr(httpclient));
		try {
			String host = get.getURI().getHost();
			param.setHost360(host);
			param.setShareUrl360(param.getHost360() + get.getPath());
		} catch (Exception e) {
			log.error(e);
		}
		if (statusCode == HttpStatus.SC_OK) {
			InputStream is = get.getResponseBodyAsStream();
			returnStr = IOUtils.toString(is, "utf-8");
			is.close();
		}
		get.releaseConnection();
		httpclient.getHttpConnectionManager().closeIdleConnections(0);
		int length = returnStr.length() > 1000 ? 1000 : returnStr.length();
		log.info(returnStr.trim().substring(0, length));
		return returnStr.trim();
	}
	private static String getCookieStr(HttpClient client) { 
		Cookie[] cookies = client.getState().getCookies(); 
		 StringBuilder builder = new StringBuilder(); 
		for (Cookie cookie : cookies) { 
		       builder.append(cookie.getDomain()).append(":") 
		               .append(cookie.getName()).append("=").append(cookie.getValue()).append(";") 
		               .append(cookie.getPath()).append(";") 
		               .append(cookie.getExpiryDate()).append(";") 
		               .append(cookie.getSecure()).append(";/n"); 
		    } 
		    return builder.toString(); 
	}
}
