package com.zhilink.tv.model;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;

import com.duolebo.tools.yunpan.DownloadUrlParser;
//import com.zhilink.framework.util.InitManager;

public class ZlAppDownloadUrl {
	private static Logger log = Logger.getLogger(ZlAppDownloadUrl.class);

	public static final Map<String, ZlAppDownloadUrl> map = new ConcurrentHashMap<String, ZlAppDownloadUrl>();
	private static final ReadWriteLock mapLock = new ReentrantReadWriteLock();
	private static final ReadWriteLock instanceLock = new ReentrantReadWriteLock();
	
	private  final ReadWriteLock lock = new ReentrantReadWriteLock();
	public static String validPeriod360 ="180"; // 180 分钟
	public static String validPeriodBaidu = "10"; // 10分钟
	
	private String id;
	private String updateUrl;
	private int urlType;
	private String status;
	private String upgradeTempUrl;
	private Date tempUrlExpireTime;
	private boolean requesting; // 请求中
	private boolean updated;
	private String password;
	
	public static ZlAppDownloadUrl getInstance(String url,String password,int urlType){
		try{
			instanceLock.writeLock().lock();
			ZlAppDownloadUrl soft = map.get(url);
			if(soft == null){
				soft = new ZlAppDownloadUrl(url,password,urlType);
				map.put(url, soft);
			}
			return soft;
		}finally{
			instanceLock.writeLock().unlock();
		}
	}
	
	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	private ZlAppDownloadUrl(String url,String password,int urlType) {
		this.updateUrl = url;
		this.password = password;
		this.urlType = urlType;
	}
	
	public ZlAppDownloadUrl(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}


	public int getUrlType() {
		return urlType;
	}

	public void setUrlType(int urlType) {
		this.urlType = urlType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpgradeTempUrl() {
		return upgradeTempUrl;
	}

	public void setUpgradeTempUrl(String upgradeTempUrl) {
		this.upgradeTempUrl = upgradeTempUrl;
	}

	public Date getTempUrlExpireTime() {
		return tempUrlExpireTime;
	}

	public void setTempUrlExpireTime(Date tempUrlExpireTime) {
		this.tempUrlExpireTime = tempUrlExpireTime;
	}

	public boolean isRequesting() {
		try {
			lock.readLock().lock();
			return requesting;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void setRequesting(boolean requesting) {
		try {
			lock.writeLock().lock();
			this.requesting = requesting;
		}finally{
			lock.writeLock().unlock();
		}
	}

	public boolean needDownload() {
//		return  !isRequesting() &&( StringUtils.isEmpty(getUpgradeTempUrl()) || getTempUrlExpireTime() == null || getTempUrlExpireTime()
//				.before(new Date()));
		return true;//每次都重新获取下载地址//
	}

	public void download() {
		log.info("--------------urlType:" + urlType+",Update_url="+updateUrl+",Share_password="+password+",UpgradeTempUrl="+upgradeTempUrl);
		try{
			setRequesting(true);
			String result = "";
			int i = 0;
			int urlType = 0;
			urlType = getUrlType();
			while (i < 1 && StringUtils.isEmpty(result)) {// 重复三次
				if(urlType == 3 ){
					result = DownloadUrlParser.getDownloadUrl360(getUpdateUrl(),getPassword());
					
				}else if (urlType == 2){
					//TODO
				}
				if(StringUtils.isEmpty(result)){
					try {
						Thread.sleep(1000*(i+1));
					} catch (InterruptedException e) {
						log.error(e);
					}
				}
				i++;
			}
			setRequesting(false);
			if(!StringUtils.isEmpty(result)){
				setUpgradeTempUrl(result);
				if(urlType == 3){
					setTempUrlExpireTime(new Date(System.currentTimeMillis()+ Integer.valueOf(this.getValidPeriod360())* 60*1000));
				}else if(urlType == 2){
					setTempUrlExpireTime(new Date(System.currentTimeMillis()+ Integer.valueOf(this.getValidPeriodBaidu())* 60*1000));
				}
				setUpdated(true);
				map.put(getUpdateUrl(), this);
			}
		}catch(Exception e){
			log.error(e);
		}
		finally{
			setRequesting(false);
		}
		log.info("--------------urlType:" + urlType+",Update_url="+updateUrl+",Share_password="+password+",UpgradeTempUrl="+upgradeTempUrl);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static ZlAppDownloadUrl getUrl(String url,String password,int urlType) {
		ZlAppDownloadUrl soft = null;
		try{
			mapLock.readLock().lock();
			soft = map.get(url);
			if(soft == null || soft.needDownload()){
				try{
					mapLock.readLock().unlock();
					mapLock.writeLock().lock();
					if (soft == null) { // 如果缓存中没有,释放读锁，上写锁
						soft = ZlAppDownloadUrl.getInstance(url,password,urlType);
					}
					log.info("soft="+soft);
					if (soft.needDownload()){
						soft.download();
					}
				}finally{
					mapLock.writeLock().unlock();
				}
			}
		}finally{
			try{
				mapLock.readLock().unlock();
			}catch(Exception e){
				log.error(e);
			}
		}
		return soft;
	}
	
	public static ZlAppDownloadUrl get360Url(String url,String password){
		return getUrl(url,password, 3);
	}
	
	public static ZlAppDownloadUrl getBaiduUrl(String url){
		//TODO
		return null;
	}
	public static String getValidPeriod360() {
		return validPeriod360;
	}

	public static void setValidPeriod360(String validPeriod360) {
		ZlAppDownloadUrl.validPeriod360 = validPeriod360;
	}

	public static String getValidPeriodBaidu() {
		return validPeriodBaidu;
	}

	public static void setValidPeriodBaidu(String validPeriodBaidu) {
		ZlAppDownloadUrl.validPeriodBaidu = validPeriodBaidu;
	}
}
