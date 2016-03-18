package com.onewave.backstage.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.JsonUtil;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.excel.ContChecksExcel;
import com.onewave.backstage.excel.ExcelUtil;
import com.onewave.backstage.model.ContCheck;
import com.onewave.backstage.service.ContChecksService;
import com.onewave.backstage.util.BmUtil;
import com.onewave.common.dao.Pagination;
import com.zhilink.tv.util.HttpUtils;

@Controller("contChecksController")
@RequestMapping("/contChecks/*")
public class ContChecksController extends MultiActionController {

	@Autowired
	@Qualifier("contChecksService")
	ContChecksService service;

	@RequestMapping("save_excel.do")
	public void saveFromExcelHandler(HttpServletRequest req,
			HttpServletResponse resp) {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "批量添加失败";
		try {
			req = BmUtil.resolveMultipart(req);

			String fileName = ExcelUtil.getOriginalFileName(req);
			if (!StringUtils.isBlank(fileName)) {
				if (fileName.contains(".")) {
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
				}
				fileName = fileName.split("-")[0];
			}
			String classify = fileName;
			File file = ExcelUtil.paserFile(req, "contchecks_save");

			if (file != null && file.isFile()) {
				msg = ContChecksExcel.saveFromExcel(req, file.getPath(),
						service, classify);
				logger.info(msg);
				if (msg != null)
					issuc = true;

				file.delete();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JSONObject json = new JSONObject();
			json.put("success", issuc);
			json.put("issuc", issuc);
			json.put("msg", msg);
			logger.info(json);
			try {
				resp.getWriter().print(json);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	@RequestMapping("query.do")
	public void query(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.setContentType("application/json;charset=UTF-8");
		printParams(req);
		String pageNumStr = req.getParameter("page");
		String pageSizeStr = req.getParameter("limit");

		String status = req.getParameter("c_status");
		String name = req.getParameter("c_name");
		String price_from = req.getParameter("c_price_from");
		String price_to = req.getParameter("c_price_to");
		String start_time = req.getParameter("start_time");
		if(!StringUtils.isBlank(start_time)){
			start_time = start_time.replaceAll("T"," ");
		}
		String end_time = req.getParameter("end_time");
		if(!StringUtils.isBlank(end_time)){
			end_time = end_time.replaceAll("T"," ");
		}

		// TODO 查询条件带参数
		int pageNum = 1, pageSize=25;
		try {
			pageNum = Integer.parseInt(pageNumStr);
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (Exception e) {
		}
		Pagination<ContCheck> pagination = service.findPagination(pageNum, pageSize, status, name, price_from, price_to, start_time, end_time);
		String json = JsonUtil.beanToJson(pagination);
		logger.info(json);
		resp.getWriter().print(json);
	}

	@RequestMapping("robot.do")
	public void robot(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.setContentType("application/json;charset=UTF-8");
		String postData = "{\"key\":\"Reload\",\"header\":{\"client-version\":\"0.0.0.0\",\"user-agent\":\"0\",\"sequence\":\"-986121_100748\",\"plat\":\"android\",\"format\":\"json\",\"mid\":\"1\",\"time\":\"20150130\",\"mtype\":\"1\",\"sys_name\":\"manual\",\"app_name\":\"GetContChecksSalesService\",},\"body\":{\"key\":\"GetContChecksSalesService\",\"param\":\"\"}}";
		JSONObject jo = new JSONObject();

		try {
			String text = HttpUtils.sendXml(postData,
					InitManager.getZltvIspUrl());
			if ("{}".equals(text)) {
				jo.put("issuc", true);
				jo.put("msg", "任务添加成功");
			}
		} catch (Exception e) {
			logger.error(e, e);
			jo.put("issuc", false);
			jo.put("msg", "任务添加失败");
		}
		String json = jo.toString();
		logger.info(json);
		resp.getWriter().print(json);
	}

	@RequestMapping("delete.do")
	public void delete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.setContentType("application/json;charset=UTF-8");
		String msg = "";
		try {
			String id = (String) req.getParameter("id");
			if (service.deleteById(id)) {
				msg = "删除成功";
			} else {
				msg = "删除失败";
			}
		} finally {
			JSONObject json = new JSONObject();
			json.put("issuc", true);
			json.put("msg", msg);
			resp.getWriter().print(json);
		}
	}

	public static void main(String[] args) throws JSONException {
		String str = "{\"title\":\"主题标题\",\"image_urls\":[\"http: //image_url\",\"http: //www.img\"]}";
		org.json.JSONObject jo = new org.json.JSONObject(str);
		org.json.JSONArray ja = jo.optJSONArray("image_urls");
		for (int i = 0; i < ja.length(); i++) {
			System.out.println(ja.getString(i));
		}
	}

	protected void printParams(HttpServletRequest req) {
		Map<String, String[]> map = req.getParameterMap();
		Set<Entry<String, String[]>> set = map.entrySet();
		Iterator<Entry<String, String[]>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, String[]> entry = it.next();

			for (String v : entry.getValue()) {
				logger.info(entry.getKey() +"="+v);
			}
		}
	}

}
