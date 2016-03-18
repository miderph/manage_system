package com.onewave.backstage.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.StringTool;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.Column;
import com.onewave.backstage.model.ContRule;
import com.onewave.backstage.service.ColumnService;
import com.onewave.backstage.service.ContRuleService;

@Controller("contRuleController")
@RequestMapping("/contrule/*")
public class ContRuleController extends MultiActionController {

	private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	@Qualifier("contRuleService")
	private ContRuleService contRuleService;
	
	@Autowired
	@Qualifier("columnService")
	private ColumnService columnService;
	
	private void setBean(ContRule contRule, HttpServletRequest req) {
		try {
			contRule.setName(req.getParameter("name"));
			contRule.setPrice(StringTool.quote(req.getParameter("price")));
			contRule.setPrice_rela(req.getParameter("price_rela"));
			contRule.setPrice_right(StringTool.quote(req.getParameter("price_right")));
			contRule.setProvider_ids(req.getParameter("provider_ids"));
			contRule.setProvider_rela(req.getParameter("provider_rela"));
			System.out.println(req.getParameter("provider_ids1"));
			contRule.setShop_ids(req.getParameter("shop_ids"));
			contRule.setShop_rela(req.getParameter("shop_rela"));
			contRule.setCategory(req.getParameter("category"));
			contRule.setCategory_rela(req.getParameter("category_rela"));
			contRule.setCategory_new_menu(req.getParameter("category_new_menu"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.setContentType("application/json; charset=UTF-8");

		String menu_id = req.getParameter("menu_id");
		
		JSONArray root = new JSONArray();
		try {
			List<ContRule> list = contRuleService.findAllByMenuId(menu_id);
			JSONObject json = null;

			for(ContRule contRule: list) {
				if(contRule == null){
					continue;
				}
				json = JSONObject.fromObject(contRule);
				json.put("rule_id", contRule.getId());
				json.put("create_time", dateformat.format(contRule.getCreate_time()));
				json.put("modify_time", dateformat.format(contRule.getModify_time()));
				
				root.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		} finally {
			logger.info(root);
			resp.getWriter().print(root);
		}
	}
	@RequestMapping("save.do")
	public void saveHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		Column column = columnService.findById(req.getParameter("menu_id"));
		boolean issuc = false;
		String msg = "添加失败";
		try {
			ContRule contRule = new ContRule();
			setBean(contRule, req);
			issuc = contRuleService.saveAndRef(contRule,column);
			if(!issuc){
				msg = "添加失败";
				return;
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

			resp.getWriter().print(rootJson);
		}
	}

	@RequestMapping("update.do")
	public void updateHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8"); 
		ContRule contRule = contRuleService.findById(req.getParameter("rule_id"));
		boolean issuc = false;
		String msg = "更新失败";
		try {
			setBean(contRule, req);
			issuc = contRuleService.update(contRule);
			if(!issuc){
				msg = "更新失败";
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (issuc) {
				msg = "更新成功";
			}
			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			resp.getWriter().print(rootJson);
		}
	}

	@RequestMapping("delete.do")
	public void delHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		boolean issuc = false;
		String msg = "删除失败";
		try {
			String id = req.getParameter("rule_id");
			String menu_id = req.getParameter("menu_id");
			issuc = contRuleService.deleteAndRef(id,menu_id);
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

			resp.getWriter().print(rootJson);
		}
	}
}
