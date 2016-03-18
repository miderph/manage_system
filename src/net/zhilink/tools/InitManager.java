package net.zhilink.tools;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

public class InitManager {
	private static Logger logger = Logger.getLogger(InitManager.class);
	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String rootLocalPath = "F:/upload/";

	private static String rootHttpPath = "";
	private static String rootApkHttpPath = "";

	public static final int Defaut_Unselected_ID = -10000;

	public static String updateIndexUrl = "";

	public static String linkServerUri = "http://miderph.gicp.net:12416/qdghZhilinkTVService/link/link.jsp";
	
	
	public static String reportUri = "";
	public static String sync_url;
	public static String sync_username;
	public static String sync_password;
	public static String zltvIspUrl;
	public static String PROVIDER_TMALL="216046209";
	
	public static String getZltvIspUrl() {
		return zltvIspUrl;
	}
	public  void setZltvIspUrl(String zltvIspUrl) {
		InitManager.zltvIspUrl = zltvIspUrl;
	}
	public static String combineRootHttpPath(String strUrl) {
		if (strUrl == null || "".equalsIgnoreCase(strUrl)
				|| "null".equalsIgnoreCase(strUrl))
			return null;
		else if (strUrl.startsWith("http://")||strUrl.startsWith("https://"))
			return strUrl;
		else
			return rootHttpPath + strUrl;
	}
	public static String combineApkHttpPath(String strUrl) {
		if (strUrl == null || "".equalsIgnoreCase(strUrl)
				|| "null".equalsIgnoreCase(strUrl))
			return null;
		else if (strUrl.startsWith("http://")||strUrl.startsWith("https://"))
			return strUrl;
		else
			return rootApkHttpPath + strUrl;
	}
	public static String getSync_url() {
		return sync_url;
	}

	public  void setSync_url(String syncUrl) {
		sync_url = syncUrl;
	}

	public static String getSync_username() {
		return sync_username;
	}

	public  void setSync_username(String syncUsername) {
		sync_username = syncUsername;
	}

	public static String getSync_password() {
		return sync_password;
	}

	public  void setSync_password(String syncPassword) {
		sync_password = syncPassword;
	}

	public static String getReportUri() {
		return reportUri;
	}

	public void setReportUri(String reportUri) {
		InitManager.reportUri = reportUri;
	}

	public static String getLinkServerUri() {
		return linkServerUri;
	}

	public  void setLinkServerUri(String linkServerUri) {
		InitManager.linkServerUri = linkServerUri;
	}

	public static String getUpdateIndexUrl() {
		return updateIndexUrl;
	}

	public void setUpdateIndexUrl(String updateIndexUrl) {
		InitManager.updateIndexUrl = updateIndexUrl;
	}

	public static String getRootHttpPath() {
		return rootHttpPath;
	}

	public void setRootHttpPath(String rootHttpPath) {
		this.rootHttpPath = rootHttpPath;
		int pos = rootHttpPath.indexOf("/","https://".length());
		this.rootApkHttpPath = rootHttpPath.substring(0,pos) + ":83" + rootHttpPath.substring(pos);
	}

	public static String getRootLocalPath() {
		return rootLocalPath;
	}

	public void setRootLocalPath(String rootLocalPath) {
		this.rootLocalPath = rootLocalPath;
	}

	// 需要判断权限
	public static boolean needAuth(HttpServletRequest req) {
		String auth = req.getParameter("auth");
		boolean flag = !StringUtils.isEmpty(auth) && "1".equals(auth);
		return flag;
	}

	// 参数取值有效
	public static boolean isValidValue(String value) {
		return StringUtils.isEmpty(value)
				|| "-1".equals(value.trim())
				|| String.valueOf(InitManager.Defaut_Unselected_ID).equals(
						value.trim()) ? false : true;
	}
	
	public static String getWebRoot(){
		return ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
	}
	
	public static boolean isWindows(){
		if(System.getProperties().getProperty("os.name").contains("Windows")){
			return true;
		}else{
			return false;
		}
	}
	
}
