package com.onewave.backstage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.InitManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.Status;
import com.onewave.backstage.service.StatusService;
import com.onewave.backstage.util.BmUtil;

@Controller("statusController")
@RequestMapping("/status/*")
public class StatusController extends MultiActionController {

	@Autowired
	@Qualifier("statusService")
	private StatusService statusService;


	public JSONArray findAll(String table_name, String field_name, String exceptValues) {

		JSONArray rootArr = new JSONArray();

		try {
			List<Status> list = null;

			if(!BmUtil.isEmpty(table_name) && !BmUtil.isEmpty(field_name)) {
				list = statusService.findAll(table_name, field_name, exceptValues);
			}

			if(list == null) {
				list = new ArrayList<Status>();
			}

			JSONObject colJson = null;
			colJson = new JSONObject();
			colJson.put("s_id", InitManager.Defaut_Unselected_ID);
			colJson.put("s_name", "请选择");
			rootArr.add(colJson);

			for(Status status: list) {
				if(status == null) continue;

				colJson = new JSONObject();

				colJson.put("s_id", status.getStatus());
				colJson.put("s_name", status.getDescription());
				rootArr.add(colJson);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootArr;
	}

	@RequestMapping("query_for_operator.do")
	public void queryOperatorStatus(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_OPERATORS", "STATUS", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_site.do")
	public void querySiteStatus(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_SITE", "STATUS", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_usergroup.do")
	public void queryUserGroupStatus(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_USER_GROUP", "TYPE", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_software_filetype.do")
	public void querySoftwareFileTypeStatus(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_SOFTWARE_VERSION", "FILE_TYPE", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_column.do")
	public void queryColumnStatus(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_MENU", "STATUS", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_column_structtype.do")
	public void queryColumnStructType(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_MENU", "STRUCT_TYPE", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_column_acttype.do")
	public void queryColumnActType(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_MENU", "ACT_TYPE", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_column_restype.do")
	public void queryColumnResourceType(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_MENU", "RESOURCE_TYPE", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_cont.do")
	public void queryContStatus(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_CONT", "STATUS", null);

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

	@RequestMapping("query_for_conttype.do")
	public void queryContType(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = findAll("ZL_CONT", "TYPE", "17");

		JSONObject root = new JSONObject();
		root.put("data", rootArr);
		resp.getWriter().print(root);
	}

}
