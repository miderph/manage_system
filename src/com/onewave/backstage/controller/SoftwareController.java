package com.onewave.backstage.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.PinyinUtil;
import net.zhilink.tools.StringTool;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.excel.ExcelUtil;
import com.onewave.backstage.excel.SoftwareExcel;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.SoftDownloadUrl;
import com.onewave.backstage.model.SoftwareVersion;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.SoftDownlaodUrlService;
import com.onewave.backstage.service.SoftwareService;
import com.onewave.backstage.service.UserGroupService;
import com.onewave.backstage.util.BmUtil;

@Controller("softwareController")
@RequestMapping("/software/*")
public class SoftwareController extends MultiActionController {
	@Autowired
	@Qualifier("softwareService")
	private SoftwareService softwareService;
	@Autowired
	@Qualifier("softDownloadUrlService")
	private SoftDownlaodUrlService softDownloadUrlService;
	@Autowired
	@Qualifier("userGroupService")
	private UserGroupService userGroupService;

	@RequestMapping("query.do")
	public ModelAndView softwareQueryHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		int firstResult = Integer.parseInt(req.getParameter("start"));logger.info("--------------firstResult:" + firstResult);
		int maxResults = firstResult + Integer.parseInt(req.getParameter("limit"));logger.info("--------------maxResults:" + maxResults);

		String version_number = req.getParameter("version_number");
		String software_info = req.getParameter("software_info");
		String plat = req.getParameter("plat");
		String enforce_flag = req.getParameter("enforce_flag");
		String usergroup_id = req.getParameter("usergroup_id");
		String file_type = req.getParameter("file_type");
		String update_url = req.getParameter("update_url");
		String description = req.getParameter("description");
		String urlType = req.getParameter("url_type");
		String status = req.getParameter("status");
		if (file_type == null
				|| file_type.equals(InitManager.Defaut_Unselected_ID + ""))
			file_type = "";
		if (usergroup_id == null
				|| usergroup_id.equals(InitManager.Defaut_Unselected_ID + ""))
			usergroup_id = "";
		if (enforce_flag == null
				|| enforce_flag.equals(InitManager.Defaut_Unselected_ID + ""))
			enforce_flag = "";
		if (urlType == null
				|| urlType.equals(InitManager.Defaut_Unselected_ID + ""))
			urlType = null;
		if (status == null
				|| status.equals(InitManager.Defaut_Unselected_ID + ""))
			status = null;
		version_number = (version_number == null || "".equals(version_number)) ? null
				: version_number;
		software_info = (software_info == null || "".equals(software_info)) ? null
				: software_info;
		plat = (plat == null || "".equals(plat)) ? null : plat;
		enforce_flag = (enforce_flag == null || "".equals(enforce_flag)) ? null
				: enforce_flag;
		usergroup_id = (usergroup_id == null || "".equals(usergroup_id)) ? null
				: usergroup_id;
		file_type = (file_type == null || "".equals(file_type)) ? null
				: file_type;
		update_url = (update_url == null || "".equals(update_url)) ? null
				: update_url;
		description = (description == null || "".equals(description)) ? null
				: description;
		urlType = (urlType == null || "".equals(urlType)) ? null : urlType;
		status = (status == null || "".equals(status)) ? null : status;

		long num = this.softwareService.getLength(version_number,
				software_info, plat, enforce_flag, usergroup_id, file_type,
				update_url, description, urlType, status);
		List<SoftwareVersion> allData = this.softwareService.getAllData(firstResult, maxResults,
				version_number, software_info, plat, enforce_flag,
				usergroup_id, file_type, update_url, description, urlType,
				status);

		String ids = "";
		for (Object obj : allData) {
			SoftwareVersion soft = (SoftwareVersion) obj;
			ids += soft.getID() + ",";
		}
		if (!StringUtils.isBlank(ids)) {
			ids = ids.substring(0, ids.length() - 1);
		}
		Map<String, SoftDownloadUrl> map = this.softDownloadUrlService
				.findMapByCId(ids, "2");

		List<UserGroup> userGroupList = this.userGroupService.findAll();
		Map<String, String> userGroupMap = new HashMap<String, String>();
		for (UserGroup userGroup : userGroupList) {
			userGroupMap.put(userGroup.getId(), userGroup.getName());
		}

		JSONArray jsonArray = new JSONArray();
		String statusname;
		for (Object obj : allData) {
			JSONObject taskJson = new JSONObject();
			SoftwareVersion test = (SoftwareVersion) obj;
			parserCont(taskJson, test);

			taskJson.put("usergroup_names_mac", BmUtil.getGroupNames(test.getUsergroup_ids_mac(), userGroupMap));
			taskJson.put("usergroup_names_zone", BmUtil.getGroupNames(test.getUsergroup_ids_zone(), userGroupMap));
			taskJson.put("usergroup_names_model", BmUtil.getGroupNames(test.getUsergroup_ids_model(), userGroupMap));
			taskJson.put("usergroup_names_channel", BmUtil.getGroupNames(test.getUsergroup_ids_channel(), userGroupMap));
			taskJson.put("usergroup_names_mac2", BmUtil.getGroupNames(test.getUsergroup_ids_mac2(), userGroupMap));
			taskJson.put("usergroup_names_zone2", BmUtil.getGroupNames(test.getUsergroup_ids_zone2(), userGroupMap));
			taskJson.put("usergroup_names_model2", BmUtil.getGroupNames(test.getUsergroup_ids_model2(), userGroupMap));
			taskJson.put("usergroup_names_channel2", BmUtil.getGroupNames(test.getUsergroup_ids_channel2(), userGroupMap));

			SoftDownloadUrl url = map.get("2_" + test.getID() + "_1");// 远程普通地址
			if (url != null) {
				taskJson.put("url_general_id", url.getId());
				taskJson.put("update_url_general", url.getDownload_url());
			}
			url = map.get("2_" + test.getID() + "_3");// 360云盘
			if (url != null) {
				taskJson.put("url_360_id", url.getId());
				taskJson.put("update_url_360", url.getDownload_url());
				taskJson.put("share_password_360", url.getShare_password());
				taskJson.put("upgrate_temp_url", url.getUpgrade_temp_url());
				if (url.getTemp_url_expire_time() != null) {
					taskJson.put("temp_url_expire_time",
							BmUtil.formatDate(url.getTemp_url_expire_time()));
				}
			}

			jsonArray.add(taskJson);
		}
		JSONObject json = new JSONObject();
		json.put("rows", jsonArray);
		json.put("num", num);
		logger.info("json===================" + json.toString());
		pw.print(json);
		return null;
	}
	private void parserCont(JSONObject json, SoftwareVersion cont) {
		if (cont != null) {
			json.put("id", cont.getID());
			json.put("version_number", StringTool.null2Empty(cont.getVersion_num()));
			json.put("software_info", StringTool.null2Empty(cont.getSoftware_info()));
			json.put("add_plat", StringTool.null2Empty(cont.getPlat()));
			json.put("enforce_flag", cont.getEnforce_flag());
			json.put("publish_time",BmUtil.formatDate(cont.getPublish_time()));
			json.put("description", StringTool.null2Empty(cont.getDescription()));
			json.put("file_type", StringTool.null2Empty(cont.getFile_type()));
			json.put("md5", cont.getMd5());
			json.put("status", cont.getStatus());
			String statusname = "";
			if ("-1".equalsIgnoreCase(cont.getStatus()))
				statusname = "失效";
			else if ("0".equalsIgnoreCase(cont.getStatus()))
				statusname = "待审核";
			else if ("1".equalsIgnoreCase(cont.getStatus()))
				statusname = "生效";
			else
				statusname = "";
			json.put("statusname", statusname);
			json.put("usergroup_ids_mac", StringTool.null2Empty(cont.getUsergroup_ids_mac()));
			json.put("usergroup_ids_model", StringTool.null2Empty(cont.getUsergroup_ids_model()));
			json.put("usergroup_ids_zone", StringTool.null2Empty(cont.getUsergroup_ids_zone()));
			json.put("usergroup_ids_channel", StringTool.null2Empty(cont.getUsergroup_ids_channel()));
			json.put("usergroup_ids_mac2", StringTool.null2Empty(cont.getUsergroup_ids_mac2()));
			json.put("usergroup_ids_model2", StringTool.null2Empty(cont.getUsergroup_ids_model2()));
			json.put("usergroup_ids_zone2", StringTool.null2Empty(cont.getUsergroup_ids_zone2()));
			json.put("usergroup_ids_channel2", StringTool.null2Empty(cont.getUsergroup_ids_channel2()));
		}
	}
	@RequestMapping("save_from_excel.do")
	public void saveFromExcelHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "批量导入失败";
		try {
			req = BmUtil.resolveMultipart(req);

			File file = ExcelUtil.paserFile(req, "software_save");
			
			if(file != null && file.isFile()) {
				msg = SoftwareExcel.saveFromExcel(req, file.getPath(), 
						softwareService, this);
				
				if(msg != null) issuc = true;
				
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			resp.getWriter().print(rootJson);
		}
	}

	@RequestMapping("update.do")
	public ModelAndView softwareModifyHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		logger.info("--:");

		for (Enumeration enums = req.getParameterNames(); enums
				.hasMoreElements();) {
			String name = (String) enums.nextElement();
			String value = "" + req.getParameter(name);
			logger.info("--field:" + name + "=" + value);
			// System.out.out.println("<br>--field:" + name +"=" + value );
		}
		resp.setContentType("application/json; charset=UTF-8");

		SoftwareVersion soft = new SoftwareVersion();
		updateCont(soft, req);

		// 普通地址
		String url_general_id = req.getParameter("url_general_id");
		String update_url_general = req.getParameter("update_url_general");
		// 360云盘
		String url_360_id = req.getParameter("url_360_id");
		String update_url_360 = req.getParameter("update_url_360");
		String share_password_360 = req.getParameter("share_password_360");

		String softId = "";
		String msg = "";
		boolean issuc = false;
		try {
		   if (this.softwareService.isExistRecord(soft)) {
		      msg = "已有此版本的升级信息，请检查！";
		      return null;
		   }
			String actionStr = "";
			if (StringUtils.isBlank(soft.getID()) || "-1".equals(soft.getID())) {
				actionStr = "新建";
				soft.setID(null);
				softId = this.softwareService.saveAndReturnId(soft);
			} else {
				actionStr = "修改";
				this.softwareService.update(soft);
				softId = soft.getID();
			}
			issuc = saveOrUpdateUrl(url_general_id, update_url_general,
					url_360_id, update_url_360, share_password_360, softId);
			msg = actionStr + (issuc ? "成功" : "失败");
		} catch (DataIntegrityViolationException e) {
			logger.error(e.getMessage(), e);
			msg = "已有此版本的升级信息，请检查！";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			msg = "保存异常";
		} finally {
			if (issuc) {
				//msg = "添加成功";
			}

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);
			resp.getWriter().print(rootJson);
		}

		return null;
	}
	private void updateCont(SoftwareVersion cont, HttpServletRequest req) {
		logger.info("--:here");

		try {
			cont.setID(req.getParameter("id"));
			cont.setVersion_num(req.getParameter("version_number"));
			cont.setSoftware_info(req.getParameter("software_info"));
			cont.setPlat(req.getParameter("add_plat"));
			cont.setEnforce_flag(req.getParameter("enforce_flag"));
			Date publish_time = null;
			try {
				publish_time = BmUtil.parseDate(req.getParameter("publish_time"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			cont.setPublish_time(publish_time);
			cont.setDescription(req.getParameter("description"));
			cont.setUsergroup_ids_mac(req.getParameter("usergroup_ids_mac"));
			cont.setUsergroup_ids_zone(req.getParameter("usergroup_ids_zone"));
			cont.setUsergroup_ids_model(req.getParameter("usergroup_ids_model"));
			cont.setUsergroup_ids_channel(req.getParameter("usergroup_ids_channel"));
			cont.setUsergroup_ids_mac2(req.getParameter("usergroup_ids_mac2"));
			cont.setUsergroup_ids_zone2(req.getParameter("usergroup_ids_zone2"));
			cont.setUsergroup_ids_model2(req.getParameter("usergroup_ids_model2"));
			cont.setUsergroup_ids_channel2(req.getParameter("usergroup_ids_channel2"));
			cont.setFile_type(req.getParameter("file_type"));
			cont.setUrl_type(req.getParameter("url_type_mod"));

			// -1 失效,0 待审核,1生效'
			cont.setStatus(req.getParameter("status"));
			cont.setMd5(req.getParameter("md5"));
			
			if (cont.getFile_type() == null
					|| cont.getFile_type().equals(InitManager.Defaut_Unselected_ID + ""))
				cont.setFile_type("");
			if (cont.getMd5() == null)
				cont.setMd5("");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public boolean saveOrUpdateUrl(String url_general_id,
			String update_url_general, String url_360_id,
			String update_url_360, String share_password_360, String softId) {
		if (StringUtils.isBlank(softId) || "-1".equals(softId))
			return false;
		if (!StringUtils.isBlank(update_url_general)) {
			if (!StringUtils.isBlank(url_general_id)
					&& !"-1".equals(url_general_id)) {
				SoftDownloadUrl url_general = softDownloadUrlService
						.findById(url_general_id);
				url_general.setDownload_url(update_url_general);
				softDownloadUrlService.update(url_general);
			} else {
				SoftDownloadUrl url_general = new SoftDownloadUrl();
				url_general.setC_type("2"); // 应用的类型：1,应用软件 2，软件升级
				url_general.setC_id(softId);
				url_general.setUrl_type("1");
				url_general.setDownload_url(update_url_general);
				softDownloadUrlService.save(url_general);
			}
		} else {
			if (!StringUtils.isBlank(url_general_id)
					&& !"-1".equals(url_general_id)) {
				softDownloadUrlService.delete(url_general_id);
			}
		}
		if (!StringUtils.isBlank(update_url_360)) {
			if (!StringUtils.isBlank(url_360_id) && !"-1".equals(url_360_id)) {
				SoftDownloadUrl url_360 = softDownloadUrlService
						.findById(url_360_id);
				url_360.setDownload_url(update_url_360);
				url_360.setShare_password(share_password_360);
				softDownloadUrlService.update(url_360);
			} else {
				SoftDownloadUrl url_360 = new SoftDownloadUrl();
				url_360.setC_type("2"); // 应用的类型：1,应用软件 2，软件升级
				url_360.setC_id(softId);
				url_360.setUrl_type("3");
				url_360.setDownload_url(update_url_360);
				url_360.setShare_password(share_password_360);
				softDownloadUrlService.save(url_360);
			}
		} else {
			if (!StringUtils.isBlank(url_360_id) && !"-1".equals(url_360_id)) {
				softDownloadUrlService.delete(url_360_id);
			}
		}
		return true;
	}

	@RequestMapping("del.do")
	public ModelAndView softwareDeleteHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		String msg = "删除失败";
		boolean issuc = false;
		try {
		logger.info("--:");
			String id = req.getParameter("id_del");
			String version_number = req.getParameter("version_number_del");
			String software_info = req.getParameter("software_info_del");
			String plat = req.getParameter("plat_del");

			resp.setContentType("application/json; charset=UTF-8");

			SoftwareVersion test = new SoftwareVersion();
			test.setID(id);
			test.setVersion_num(version_number);
			test.setSoftware_info(software_info);
			test.setPlat(plat);
			this.softwareService.deleteData(test);
			
			List<SoftDownloadUrl> urlList = this.softDownloadUrlService
					.findByCId(id, "2");
			if (urlList != null && urlList.size() > 0) {
				for (SoftDownloadUrl url : urlList) {
					issuc = this.softDownloadUrlService.delete(url);
				}
			}else{
				issuc = true;				
			}
		} catch (Exception e) {

		} finally {
			if (issuc) {
				msg = "删除成功";
			}

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);
			logger.info("rootJson="+rootJson);
			resp.getWriter().print(rootJson);
		}
		return null;
	}

	private String[] getPerData(String str) {

		String[] result = null;
		try {
			// 用Pattern的split()方法把字符串按" "分割
			Pattern p = Pattern.compile("(\\ )+");
			result = p.split(str);// 把取得的URL存到数组中
		} catch (Exception err) {
			err.printStackTrace();
		}
		return result;
	}

}
