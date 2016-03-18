package com.onewave.backstage.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.StringTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.StbPrefix;
import com.onewave.backstage.service.StbPrefixService;
import com.onewave.backstage.util.BmUtil;

@Controller("stbPrefixController")
@RequestMapping("/stbprefixe/*")
public class StbPrefixController extends MultiActionController {
	
	@Autowired
	@Qualifier("stbPrefixService")
	private StbPrefixService stbPrefixService;


	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		int firstResult = Integer.parseInt(req.getParameter("start"));
		int maxResults = firstResult
				+ Integer.parseInt(req.getParameter("limit"));

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			List<StbPrefix> list = stbPrefixService.findAll(firstResult,
					maxResults);
			if (list != null && list.size() > 0) {
				for (StbPrefix stbPrefix : list) {
					if (stbPrefix == null)
						continue;

					rootArr.add(parserStbPrefix(stbPrefix));
				}
			}

			total = stbPrefixService.countAll();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JSONObject root = new JSONObject();
			root.put("total", total);
			root.put("data", rootArr);

			logger.info(root);
			resp.getWriter().print(root);
		}
	}

	@RequestMapping("save.do")
	public void saveHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "添加失败";
		try {

			if (BmUtil.isAdminOperator(req)) {
				StbPrefix stbPrefix = new StbPrefix();
				stbPrefix.setCode(req.getParameter("code"));

				if (stbPrefixService.isExistPrefix(stbPrefix)) {
					msg = "添加失败，前缀【" + stbPrefix.getCode() + "】已经存在。";
				} else {
					issuc = stbPrefixService.save(stbPrefix);
				}
			} else {
				msg = "非Admin用户不能添加";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (issuc) {
				msg = "添加成功";
			}
			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			logger.info(rootJson);
			resp.getWriter().print(rootJson);
		}
	}

	@RequestMapping("update.do")
	public void updateHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "修改失败";
		try {

			if (BmUtil.isAdminOperator(req)) {
				String id = req.getParameter("sp_id");
				StbPrefix stbPrefix = stbPrefixService.findById(id);
				stbPrefix.setCode(req.getParameter("code"));

				if (stbPrefixService.isExistPrefix(stbPrefix)) {
					msg = "修改失败，前缀【" + stbPrefix.getCode() + "】已经存在。";
				} else {
					issuc = stbPrefixService.update(stbPrefix);
				}
			} else {
				msg = "非Admin用户不能修改";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (issuc) {
				msg = "修改成功";
			}

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			logger.info(rootJson);
			resp.getWriter().print(rootJson);
		}
	}

	@RequestMapping("del.do")
	public void delHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "删除失败";
		try {

			if (BmUtil.isAdminOperator(req)) {
				issuc = stbPrefixService.delete(req.getParameter("sp_id"));
			} else {
				msg = "非Admin用户不能删除";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (issuc) {
				msg = "删除成功";
			}

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			logger.info(rootJson);
			resp.getWriter().print(rootJson);
		}
	}

	private JSONObject parserStbPrefix(StbPrefix stbPrefix) {

		if (stbPrefix == null)
			return null;

		JSONObject colJson = new JSONObject();

		colJson.put("sp_id", stbPrefix.getId());
		colJson.put("code", StringTool.null2Empty(stbPrefix.getCode()));
		colJson.put("provider_id", StringTool.null2Empty(stbPrefix.getProvider_id()));
		colJson.put("provider_name", StringTool.null2Empty(stbPrefix.getProvider_name()));
		colJson.put("site_id", StringTool.null2Empty(stbPrefix.getSite_id()));
		colJson.put("site_name", StringTool.null2Empty(stbPrefix.getSite_name()));
		colJson.put("create_time",
				BmUtil.formatDate(stbPrefix.getCreate_time()));
		colJson.put("modify_time",
				BmUtil.formatDate(stbPrefix.getUpdate_time()));

		return colJson;
	}

	@RequestMapping("query_unbound.do")
	public void queryUnboundStbPrefix(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		
		JSONArray rootArr = new JSONArray();
		try {
			String providerId = req.getParameter("provider_id");
			
			List<StbPrefix> stbPrefixList = stbPrefixService.findAllUnbound(providerId);
			
			if(stbPrefixList != null && stbPrefixList.size() > 0) {
				JSONObject colJson = null;
				for(StbPrefix stbPrefix: stbPrefixList) {
					if(stbPrefix == null) continue;
					
					colJson = new JSONObject();
					
					colJson.put("sp_id", stbPrefix.getId());
					colJson.put("sp_code", stbPrefix.getCode());
					rootArr.add(colJson);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JSONObject root = new JSONObject();
			root.put("data", rootArr);
			resp.getWriter().print(root);
		}
	}

}
