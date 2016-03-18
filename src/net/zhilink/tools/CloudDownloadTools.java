package net.zhilink.tools;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.onewave.backstage.service.AppDownloadUrlService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.RelaMenuAndContService;
import com.onewave.backstage.service.StatusDictService;
import com.zhilink.tv.model.CheckUpdate;
import com.zhilink.tv.model.ZlAppDownloadUrl;

public class CloudDownloadTools {
	private static Logger log = Logger.getLogger(CloudDownloadTools.class);
	public static final java.text.SimpleDateFormat sdf_yyyy_MM_ddHHmmss = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
   
	public static CheckUpdate computeUpdateUrl(List<CheckUpdate> list) {
		ZlAppDownloadUrl downloadUrl = null;
		String UpgradeTempUrl = "";
		if (list == null || list.size() == 0) {
			return null;
		} else {
			for (CheckUpdate update : list) {
				int urlType = 1;
				try {
					urlType = (Integer.valueOf(update.getUrl_Type()))
							.intValue();
				} catch (Exception e) {
					log.error(e);
				}
				log.info("--------------urlType:" + urlType+",Update_url="+update.getUpdate_url());
				try{
					if (urlType == 1) { // 1普通地址 2 baidu云盘 3 360云盘
						UpgradeTempUrl = update.getUpdate_url();
					} else {
						if (urlType == 2) {// 百度云盘 待完善
							// UpgradeTempUrl =
							// DownloadUrlParser.getDownloadUrlBaidu(update.getUpdate_url());
						} else if (urlType == 3) {
							downloadUrl = ZlAppDownloadUrl.get360Url(update.getUpdate_url(), update.getShare_password());
							UpgradeTempUrl = downloadUrl.getUpgradeTempUrl();
							update.setTempUrlExpireTime(downloadUrl.getTempUrlExpireTime());
						}
					}
				}catch(Exception e){
					log.error("get yunpan url error :" + e);
				}
				log.info("--------------urlType:" + urlType+",Update_url="+update.getUpdate_url()+",Share_password="+update.getShare_password()+",UpgradeTempUrl="+UpgradeTempUrl);
				if (!StringUtils.isEmpty(UpgradeTempUrl)) {
					update.setUpdate_url(UpgradeTempUrl);
					if (urlType == 2 || urlType == 3) {
						downloadUrl.setId(update.getUrl_id());
					}
					return update;
				}
			}
		}
		return null;
	}
}
