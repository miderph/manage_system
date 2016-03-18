package com.zhilink.tv.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtils {
	private static Log logger = LogFactory.getLog(HttpUtils.class);

	public static String sendXml(String xmlStr, String postUrl)
			throws Exception {
		String returnStr = "";
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod(postUrl);
		//System.out.println(xmlStr);
		//logger.debug("request:" + xmlStr);
		logger.debug("url: " + postUrl);
		post.setRequestEntity(new StringRequestEntity(xmlStr, "text/xml",
				"utf-8"));
		int statusCode = httpclient.executeMethod(post);
		if (statusCode == HttpStatus.SC_OK) {
			InputStream is = post.getResponseBodyAsStream();
			returnStr = IOUtils.toString(is, "utf-8");
			is.close();
			logger.debug(returnStr);
			//System.out.println(returnStr);
		}
		post.releaseConnection();
		httpclient.getHttpConnectionManager().closeIdleConnections(0);
		return returnStr;
	}

	public static String get(String url) throws Exception {
		String returnStr = "";
		HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(url);
		int statusCode = httpclient.executeMethod(get);
		if (statusCode == HttpStatus.SC_OK) {
			InputStream is = get.getResponseBodyAsStream();
			returnStr = IOUtils.toString(is, "utf-8");
			is.close();
		}
		get.releaseConnection();
		httpclient.getHttpConnectionManager().closeIdleConnections(0);
		return returnStr.trim();
	}

	/**
	 * 下载文件保存到本地
	 * 
	 * @param path
	 *            文件保存位置
	 * @param url
	 *            文件地址
	 * @throws IOException
	 */
	public static boolean downloadFile(String url,String path){
		return downloadFile(url,path,false);
	}
	//coverage = true 强制覆盖
	public static boolean downloadFile(String url,String path,boolean coverage){
		// 创建文件对象
		File f = new File(path);
		// 创建文件路径
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if(f.exists()){
			if(!coverage){
				if (f.exists())
				{
					long size = getRemoteFileSize(url);
					if (size == f.length())
					{
						logger.info("已下载，并且文件大小一致，不需要重新下载。url="+url+",file=" +path);
						return true;
					}
				}
			}
		}
		// 写入文件
		try {
			logger.info("download start : url : "+ url +" to path: "+f.getAbsolutePath());

			URL u = new URL(url);
			File tempFile = new File(path+"_temp");
			FileUtils.copyURLToFile(u, tempFile);
			f.delete();
			tempFile.renameTo(f);
		} catch (IOException e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
	public static void downloadfileWithHttpClient(String path, String url) {
		HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(url);
		try {
			int statusCode = httpclient.executeMethod(get);
			if (statusCode == HttpStatus.SC_OK) {
				InputStream is = get.getResponseBodyAsStream();
				BufferedOutputStream bw = null;
				try {
					// 创建文件对象
					File f = new File(path);
					// 创建文件路径
					if (!f.getParentFile().exists())
						f.getParentFile().mkdirs();
					// 写入文件
					bw = new BufferedOutputStream(new FileOutputStream(path));
					byte[] b = new byte[2048];
					int len =0;
					while((len=is.read(b))!=-1){
						bw.write(b, 0, len);
					}
					is.close();
					bw.close();
				} catch (Exception e) {
					logger.error("保存文件错误,path=" + path + ",url=" + url, e);
				} finally {
					try {
						if (bw != null)
							bw.close();
					} catch (Exception e) {
						logger.error(
								"finally BufferedOutputStream shutdown close",
								e);
					}
				}
				is.close();
			}
		}catch (HttpException e){  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace(); } finally {
			get.releaseConnection();
			httpclient.getHttpConnectionManager().closeIdleConnections(0);
		}
	}
	public static long getRemoteFileSize(String url) {
		logger.info("...getRemoteFileSize()..");
		long size = -1;
		try {
			HeadMethod httpGet = new HeadMethod(url);//duolebo:zyf 2013-08-01
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setConnectionManagerTimeout(5000);
			httpClient.getParams().setSoTimeout(5000);

			int code = httpClient.executeMethod(httpGet);
			logger.info("HeadMethod-code=" + code);
			try{

				if (code == 200)
					size = Long.parseLong(httpGet.getResponseHeader("Content-Length").getValue());
					//size = response.getEntity().getContentLength();
			} catch (Exception e) {
				size = -1;
				e.printStackTrace();
			}
		} catch (Exception e) {
			logger.info("&&&&....getRemoteFileSize()..&&&&&&&&&&&&&&=" + e.toString());
			e.printStackTrace();
		}
		logger.info("...getRemoteFileSize(),size=" + size);
		return size;
	}
	public static String post(String url,Map params) throws Exception{
		String returnStr = "";
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod(url);
		httpclient.getParams().setContentCharset("UTF-8");
		NameValuePair[] pairs =null;
		if(params!=null && params.size() > 0){
			pairs = new NameValuePair[params.size()];
			int i =0;
			for(Object key: params.keySet()){
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
		return returnStr;
	}
	public static void main(String[] args) throws MalformedURLException {
		try {
			HttpUtils.sendXml("{\"name\": 1}", "http://localhost:8081/QDGH_manageService/apps/import.do");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
