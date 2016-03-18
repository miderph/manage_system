package com.onewave.backstage.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.StringTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Site;
import com.onewave.backstage.service.SiteService;
import com.onewave.backstage.util.BmUtil;

@Controller("siteController")
@RequestMapping("/site/*")
public class SiteController extends MultiActionController {
	
	@Autowired
	@Qualifier("siteService")
	private SiteService siteService;

	
	@RequestMapping("query_all.do")
	public void queryAllSite(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		try {
			Operator operator =(Operator) req.getSession().getAttribute("user");
			
			List<Site> list = siteService.findWithAuth(operator);
			
			JSONObject colJson = null;
         colJson = new JSONObject();
         colJson.put("s_id", InitManager.Defaut_Unselected_ID);
         colJson.put("s_name", "请选择");
         //rootArr.add(colJson);

			if(list != null && list.size() > 0) {
				for(Site site: list) {
					if(site == null) continue;
					
					colJson = new JSONObject();
					
					colJson.put("s_id", site.getId());
					colJson.put("s_name", site.getName());
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

	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		int firstResult = Integer.parseInt(req.getParameter("start"));
		int maxResults = firstResult + Integer.parseInt(req.getParameter("limit"));

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			List<Site> list = siteService.findAll(firstResult, maxResults);
			if(list != null && list.size() > 0) {
				for(Site site: list) {
					if(site == null) continue;
					
					rootArr.add(parserSite(site));
				}
			}
			
			total = siteService.countAll();
			
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
				Site site = new Site();
				updateSite(site, req);
				issuc = siteService.save(site);
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
				Site site = new Site();
				site.setId(req.getParameter("s_id"));
				updateSite(site, req);
				issuc = siteService.update(site);
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
				issuc = siteService.delete(req.getParameter("s_id"));
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
	
	@RequestMapping(".do")
	public ModelAndView updateIndexHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		boolean bflag = false;
		String updateIndex = siteService.updateIndex();
		logger.info(updateIndex);
		if("".equals(updateIndex)){
			bflag = false;
		}else{
			JSONObject json = JSONObject.fromObject(updateIndex); 
			String succ =json.getString("succ");
			String msg = json.getString("msg");
			if("true".equals(succ)){
				bflag = true;
			}else{
				bflag = false;
			}
		}
		if(bflag){
			pw.print("{success:true , msg:'索引成功'}");
		}
		else{
			pw.print("{failure:true , msg:'索引失败'}");
		}
		return null;
	}
	
	private JSONObject parserSite(Site site) {
		
		if(site == null) return null;
		
		JSONObject colJson = new JSONObject();
		
		colJson.put("s_id", site.getId());
		colJson.put("name", StringTool.null2Empty(site.getName()));
		colJson.put("status", site.getStatus());
		colJson.put("intro", StringTool.null2Empty(site.getIntro()));
		colJson.put("active_time", BmUtil.formatDate(site.getActive_time()));
		colJson.put("deactive_time", BmUtil.formatDate(site.getDeactive_time()));
		colJson.put("create_time", BmUtil.formatDate(site.getCreate_time()));
		colJson.put("modify_time", BmUtil.formatDate(site.getModify_time()));
		
		return colJson;
	}
	
	private void updateSite(Site site, HttpServletRequest req) {
		
		try {
			site.setName(req.getParameter("name"));
			site.setStatus(Integer.parseInt(req.getParameter("status")));
			site.setIntro(req.getParameter("intro"));
			site.setActive_time(BmUtil.parseDate(req.getParameter("active_time")));
			site.setDeactive_time(BmUtil.parseDate(req.getParameter("deactive_time")));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
