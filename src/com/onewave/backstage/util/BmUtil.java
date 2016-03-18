package com.onewave.backstage.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.zhilink.tools.ImageUtils;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.StringTool;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.service.ImgService;

public class BmUtil {
	public static Logger logger = Logger.getLogger(BmUtil.class);
	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   public static final java.text.SimpleDateFormat sdf_yyyyMMddHHmmss = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
	
	public static Date parseDate(String dateStr) {
		Date date = new Date();
		if(!isEmpty(dateStr)) {
			try {
				date = dateformat.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return date;
	}
	
//	public String formatDate(Date date) {
//		String result = "0000-00-00 00:00:00";
//
//		if(date != null) result = dateformat1.format(date);
//
//		return result;
//	}
	public static String formatDate(Date date) {
		String dateStr = "";
		if(date != null) {
			dateStr = dateformat.format(date);
		}
		
		return dateStr;
	}

	public static boolean isNumeric(String str) {
		boolean result = false;

		if (!isEmpty(str)) {
			result = str
					.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
		}

		return result;
	}

	public static boolean isEmpty(String str) {
		boolean result = false;

		if (str == null || "".equals(str.trim())) {
			result = true;
		}

		return result;
	}
	
	public static boolean isAdminOperator(HttpServletRequest req) {
		
		boolean result = false;
		
		if(req != null) {
			Object obj = req.getSession().getAttribute("admin");
			Operator admin = null;
			
			if(obj != null && obj instanceof Operator) {
				admin = (Operator) obj;
			}
			
			if(admin != null && "admin".equals(admin.getName())) {
				result = true;
			}
		}
		
		return result;
	}
   
   public static String createImgUrl(String url) {
      String result = url;
      
      if (StringTool.isEmpty(url)) {
         result = "";
      } else if(!url.startsWith("http://") && !url.startsWith("https://")) {
         result = InitManager.getRootHttpPath() + url;
      }
      
      return result;
   }
   public static String getGroupNames(String ids, Map<String, String> userGroupMap){
	   String a="";
	   if(ids !=null){
		   String[] usergroup_ids = ids.split(",");
		   for (int i = 0; i < usergroup_ids.length; i++) {
			   a = a+userGroupMap.get(usergroup_ids[i])+",";
		   }
	   }
	   return a;
   }
   /*
    * 用在有文件上传的请求中。因为注解方法映射不会自动把HttpServletRequest转化为MultipartHttpServletRequest，所以需要明确调用下。
    */
   public static HttpServletRequest resolveMultipart(HttpServletRequest req){
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());
		//判断req是否有文件上传,即多部分请求
		if(multipartResolver.isMultipart(req)) {
			req = multipartResolver.resolveMultipart(req);
		}
		return req;
   }   
}
