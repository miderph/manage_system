package com.onewave.backstage.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.Module;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.service.ModuleService;

@Controller("moduleController")
@RequestMapping("/module/*")
public class ModuleController extends MultiActionController {

	@Autowired
	@Qualifier("moduleService")
	private ModuleService moduleService;

	@RequestMapping("query_with_auth.do")
	public ModelAndView queryWithAuthHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("begin===================");

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		
		JSONArray moduleMenu = new JSONArray();
		String username = "";
		try {
			Operator user = (Operator) req.getSession().getAttribute("user");
			if(user != null) {
				username = user.getName();
				List<Module> moduleList = moduleService.queryWithAuth(user);
				JSONObject json = null;
				for (Module module : moduleList) {
					json = new JSONObject();
					json.put("id", module.getUrl());
					json.put("text", module.getName());
					json.put("leaf", "1".equals(module.getLeaf()) ? true : false);
					
					moduleMenu.add(json);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			JSONObject sysInfo = new JSONObject();
			sysInfo.put("name", "OTT业务管理平台");
			sysInfo.put("version", "1.0.0.0");
			sysInfo.put("iconUrl", "app/resources/icons/duolebo.png");

			JSONObject root = new JSONObject();
			root.put("sysInfo", sysInfo);
			root.put("moduleMenu", moduleMenu);
			root.put("username", username);
			logger.info(root);
			resp.getWriter().print(root);
		}
		return null;
	}
	
	@RequestMapping("query_all.do")
	public ModelAndView queryAllModule(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		try {
			Operator operator =(Operator) req.getSession().getAttribute("user");
			
			List<Module> list = moduleService.findAll();
			
			if(list != null && list.size() > 0) {
				JSONObject colJson = null;
				for(Module module: list) {
					if(module == null) continue;
					
					colJson = new JSONObject();
					
					colJson.put("m_id", module.getId());
					colJson.put("m_name", module.getName());
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
		return null;
	}
}
