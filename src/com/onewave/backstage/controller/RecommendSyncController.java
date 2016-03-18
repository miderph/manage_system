package com.onewave.backstage.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.directwebremoting.util.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.RecommendSync;
import com.onewave.backstage.service.RecommendSyncService;

@Controller("recommendSyncController")
@RequestMapping("/recommend_syncs/*")
public class RecommendSyncController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(RecommendSyncController.class);
	
	@Autowired
	@Qualifier("recommendSyncService")
	private RecommendSyncService recommendSyncService;
	

	@RequestMapping("query.do")
	public ModelAndView query(HttpServletRequest req,HttpServletResponse resp) throws Exception{
		logger.info("--:");
	    resp.setContentType("application/json; charset=UTF-8");
		String start = req.getParameter("start"); logger.info("--------------start:" + start);
		String limit = req.getParameter("limit"); logger.info("--------------limit:" + limit);
		int startInt = -1;
		if (start != null && !"".equals(start.trim())) {
			startInt = Integer.parseInt(start);
		}

		int limitInt = -1;
		if (limit != null && !"".equals(limit.trim())) {
			limitInt = Integer.parseInt(limit);
		}
		
		List<RecommendSync> sync_list = recommendSyncService.findAll(startInt, startInt+limitInt);
		int contNum = recommendSyncService.countAll();
		JSONArray jsonArray = new JSONArray();
		for(RecommendSync sync : sync_list){
			JSONObject syncJson = new JSONObject();
			syncJson.put("id", sync.getId());
			syncJson.put("c_id", sync.getC_id());
			syncJson.put("name", sync.getName());
			syncJson.put("status", sync.getStatus()==1 ? "成功" : sync.getStatus());
			syncJson.put("create_time", InitManager.dateformat.format(sync.getCreate_time()));
			syncJson.put("update_time", InitManager.dateformat.format(sync.getUpdate_time()));
			syncJson.put("extra_params", sync.getExtra_params());
			jsonArray.put(syncJson);
		}
		//jsonArray = JSONArray.fromObject(sync_list);
		
		PrintWriter pw = resp.getWriter();
		JSONObject json = new JSONObject();
		json.put("results", contNum);
		json.put("datastr", jsonArray);
		logger.info(json.toString());
	    pw.print(json);
		return null;
	}
	
	@RequestMapping("add.do")
	public ModelAndView add(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		   logger.info("--:");
		   resp.setContentType("application/json; charset=UTF-8");
		   PrintWriter pw = resp.getWriter();
		   
		   String ids = req.getParameter("ids");
		   JSONObject json = new JSONObject();
		   if(StringUtils.isEmpty(ids)){
			   json.put("success", false);
		       json.put("info", "请选择要同步的内容!");
		       json.put("id", "");
		       pw.print(json);
			   return null; 
		   }
		   if(recommendSyncService.syncAdd(ids)){
			   json.put("success", true);
		       json.put("info", "同步成功!");
		       json.put("id", ids);
		   }else{
			   json.put("success", false);
		       json.put("info", "同步失败!");
		       json.put("id", ids);
		   }
	       pw.print(json);
		   return null;
	   }
	   
	@RequestMapping("delete.do")
	   public ModelAndView delete(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		   logger.info("--:");
		   resp.setContentType("application/json; charset=UTF-8");
		   PrintWriter pw = resp.getWriter();
		   
		   String ids = req.getParameter("ids");
		   JSONObject json = new JSONObject();
		   if(StringUtils.isEmpty(ids)){
			   json.put("success", false);
		       json.put("info", "请选择要删除的已同步的内容!");
		       json.put("id", "");
		       pw.print(json);
			   return null; 
		   }
		   if(recommendSyncService.syncDelete(ids)){
			   json.put("success", true);
		       json.put("info", "删除同步成功!");
		       json.put("id", ids);
		   }else{
			   json.put("success", false);
		       json.put("info", "删除已同步失败!");
		       json.put("id", ids);
		   }
	       pw.print(json);
		   return null;
	   }
}
