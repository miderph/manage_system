package com.onewave.backstage.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.ImageUtils;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.StringTool;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.controller.ImgController;
import com.onewave.backstage.model.CheckResult;
import com.onewave.backstage.model.Column;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.ColumnService;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoService;
import com.onewave.backstage.service.ImgService;
import com.onewave.backstage.service.RelaMenuAndContService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.service.SiteService;
import com.onewave.backstage.service.UserGroupService;
import com.onewave.backstage.util.BmUtil;

@Controller("columnController")
@RequestMapping("/column/*")
public class ColumnController extends MultiActionController {
	@Autowired
	@Qualifier("columnService")
	private ColumnService columnService;
	@Autowired
	@Qualifier("siteService")
	private SiteService siteService;
	@Autowired
	@Qualifier("imgService")
	private ImgService imgService;
	@Autowired
	@Qualifier("imgController")
	private ImgController imgController;
	@Autowired
	@Qualifier("contService")
	private ContService contService;
	@Autowired
	@Qualifier("contVideoService")
	private ContVideoService contVideoService;
	@Autowired
	@Qualifier("userGroupService")
	private UserGroupService userGroupService;
	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;

	@Autowired
	private RelaMenuAndContService relaMenuAndContService;

	private CheckResult checkValid(HttpServletRequest req, Column column){
		CheckResult result = new CheckResult();
		int shortcutType = 0;
		try {
			shortcutType = Integer.parseInt(column.getIs_shortcut());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		//对栏目的限制
		//一、新建子栏目的父栏目不能是【内容链接、栏目链接、分渠道活动广告栏目】
		//二、修改栏目的快捷方式类型：
		//1，【内容链接、栏目链接、分渠道活动广告栏目】下不能有子栏目
		//2，【内容链接、栏目链接】下不能编排资产
		//三、新建或修改：栏目链接站点不能有栏目快捷方式
		if (StringUtils.isBlank(column.getId())){
			if (!"0".equals(column.getParent_id())){
				Column parent = columnService.findById(column.getParent_id());
				int tempShortcutType = 0;
				try {
					tempShortcutType = Integer.parseInt(parent.getIs_shortcut());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				switch (tempShortcutType){
				case 1://内容链接
				case 2://栏目链接
				case 3://分渠道活动广告栏目
					result.setResult(-1);
					result.setMsg("保存失败！该栏目下不能增加子栏目【栏目id="+parent.getId()+"】");
				break;
				}
			}
		}
		else{
			switch (shortcutType){
			case 1://内容链接
			case 2://栏目链接
			case 3://分渠道活动广告栏目
				int submenucount = columnService.countAll(Long.parseLong(column.getId()), InitManager.Defaut_Unselected_ID, column.getSite_id());
				if (submenucount > 0){//有子栏目则保存失败
					result.setResult(-1);
					result.setMsg("保存失败！该栏目下不能有子栏目【栏目id="+column.getId()+"】");
				}
				break;
			}
			switch (shortcutType){
			case 1://内容链接
			case 2://栏目链接
				int contNum = relaMenuAndContService.countAllForMAR(column.getId(), InitManager.Defaut_Unselected_ID, 0);
				if (contNum > 0){//不能有变编排的资产
					result.setResult(-1);
					result.setMsg("保存失败！该栏目下不能有编排的资产【栏目id="+column.getId()+"】");
				}
				break;
			}
		}
		switch (shortcutType){
		case 2://栏目链接
			//栏目链接站点不允许有栏目快捷方式
			if ("0".equals(column.getSite_id())){
				result.setResult(-1);
				result.setMsg("保存失败！栏目链接站点不允许有栏目快捷方式！【栏目id="+column.getId()+"】");
			}
			break;
		}
		
		return result;
	}
	private JSONObject parserColumn(Column column) {

		if(column == null) return null;

		JSONObject colJson = new JSONObject();

		colJson.put("c_id", column.getId());
		colJson.put("c_pid", column.getParent_id());
		colJson.put("sub_count", column.getSub_count());
		boolean isLeaf = column.getSub_count() <= 0 ? true : false;
		colJson.put("leaf", isLeaf);
		colJson.put("link_url", StringTool.null2Empty(column.getLink_url()));
		colJson.put("islocal", StringTool.null2Empty(column.getIslocal()));
		colJson.put("is_shortcut", StringTool.null2Empty(column.getIs_shortcut()));
		colJson.put("is_autoplay", StringTool.null2Empty(column.getIs_autoplay()));
		colJson.put("act_type", StringTool.null2Empty(column.getAct_type()));
		colJson.put("order_num", column.getOrder_num());
		colJson.put("provider_id", StringTool.null2Empty(column.getProvider_id()));
		colJson.put("resource_type", StringTool.null2Empty(column.getResource_type()));
		colJson.put("shortcut_contid", StringTool.null2Empty(column.getShortcut_contid()));
		colJson.put("site_id", StringTool.null2Empty(column.getSite_id()));
		colJson.put("status", column.getStatus());
		colJson.put("struct_type", StringTool.null2Empty(column.getStruct_type()));
		colJson.put("title", StringTool.null2Empty(column.getTitle()));
		colJson.put("usergroup_ids_mac", StringTool.null2Empty(column.getUsergroup_ids_mac()));
		colJson.put("usergroup_ids_model", StringTool.null2Empty(column.getUsergroup_ids_model()));
		colJson.put("usergroup_ids_zone", StringTool.null2Empty(column.getUsergroup_ids_zone()));
		colJson.put("usergroup_ids_channel", StringTool.null2Empty(column.getUsergroup_ids_channel()));
		colJson.put("usergroup_ids_mac2", StringTool.null2Empty(column.getUsergroup_ids_mac2()));
		colJson.put("usergroup_ids_model2", StringTool.null2Empty(column.getUsergroup_ids_model2()));
		colJson.put("usergroup_ids_zone2", StringTool.null2Empty(column.getUsergroup_ids_zone2()));
		colJson.put("usergroup_ids_channel2", StringTool.null2Empty(column.getUsergroup_ids_channel2()));

		colJson.put("version", StringTool.null2Empty(column.getVersion()));
		colJson.put("active_time", BmUtil.formatDate(column.getActive_time()));
		colJson.put("deactive_time", BmUtil.formatDate(column.getDeactive_time()));
		colJson.put("create_time", BmUtil.formatDate(column.getCreate_time()));
		colJson.put("modify_time", BmUtil.formatDate(column.getModify_time()));
		colJson.put("rule_ids", StringTool.null2Empty(column.getImport_rule_ids()));
		return colJson;
	}

	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		long c_pid = Long.parseLong(req.getParameter("c_pid"));
		long status = Long.parseLong(req.getParameter("status"));
		String site_id = req.getParameter("site_id");

		JSONArray root = new JSONArray();
		try {
			List<UserGroup> userGroupList = this.userGroupService.findAll();
			Map<String, String> userGroupMap = new HashMap<String, String>();
			for(UserGroup userGroup: userGroupList){
				userGroupMap.put(userGroup.getId(), userGroup.getName());
			}

			List<Column> list = columnService.findAll(c_pid, status, site_id);
			JSONObject json = null;

			JSONArray leafArr = new JSONArray();
			for(Column column: list) {
				if(column == null){
					continue;
				}

				json = parserColumn(column);
				json.put("usergroup_names_mac", getGroupNames(column.getUsergroup_ids_mac(), userGroupMap));
				json.put("usergroup_names_zone", getGroupNames(column.getUsergroup_ids_zone(), userGroupMap));
				json.put("usergroup_names_model", getGroupNames(column.getUsergroup_ids_model(), userGroupMap));
				json.put("usergroup_names_channel", getGroupNames(column.getUsergroup_ids_channel(), userGroupMap));
				json.put("usergroup_names_mac2", getGroupNames(column.getUsergroup_ids_mac2(), userGroupMap));
				json.put("usergroup_names_zone2", getGroupNames(column.getUsergroup_ids_zone2(), userGroupMap));
				json.put("usergroup_names_model2", getGroupNames(column.getUsergroup_ids_model2(), userGroupMap));
				json.put("usergroup_names_channel2", getGroupNames(column.getUsergroup_ids_channel2(), userGroupMap));

				if ("1".equals(column.getIs_shortcut())){
					Cont cont=null;
					try {
						cont = contService.findById(column.getShortcut_contid());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					json.put("shortcut_contname","内容链接");
					json.put("shortcut_linktoname",cont==null?"链接目标不存在":column.getShortcut_contid()+"|"+cont.getName());
				}
				else if("2".equals(column.getIs_shortcut())){
					Column linkmenu = null;
					try {
						linkmenu = columnService.findById(column.getShortcut_contid());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					json.put("shortcut_contname","栏目链接");
					json.put("shortcut_linktoname",linkmenu==null?"链接目标不存在":column.getShortcut_contid()+"|"+linkmenu.getTitle());
				}
				else if("3".equals(column.getIs_shortcut())){
					json.put("shortcut_contname","分渠道活动广告栏目");
					json.put("shortcut_linktoname","");
				}
				else{
					json.put("shortcut_contname","否");
					json.put("shortcut_linktoname","");
				}
				//				boolean isLeaf = column.getSub_count() <= 0 ? true : false;

				root.add(json);
				/*if(isLeaf) {
					leafArr.add(json);
				} else {
					root.add(json);
				}*/

			}

			//			root.addAll(leafArr);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			logger.info(root);
			resp.getWriter().print(root);
		}
	}
	private String getGroupNames(String ids, Map<String, String> userGroupMap){
		String a="";
		if(ids !=null){
			String[] usergroup_ids = ids.split(",");
			for (int i = 0; i < usergroup_ids.length; i++) {
				a = a+userGroupMap.get(usergroup_ids[i])+",";
			}
		}
		return a;
	}
	@RequestMapping("query_hasname.do")
	public void queryHasNameHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.setContentType("application/json; charset=UTF-8");

		String site_id = req.getParameter("site_id");
		String ignore_id = req.getParameter("ignore_id");
		String title = req.getParameter("title");
		int pid = Integer.parseInt(req.getParameter("c_pid"));
		logger.info("site_ids: " + site_id + ",title: " + title + ",pid: " + pid);

		String menuIds = "";
		if(BmUtil.isAdminOperator(req)) {

		}
		else {
			Operator operator =(Operator) req.getSession().getAttribute("user");
			String siteIds = roleService.queryIdsWithAuth(operator,"site");
			if(site_id==null || "-1".equals(site_id) || (""+InitManager.Defaut_Unselected_ID).equals(site_id)) {
				site_id = siteIds;
			}
			else if ((","+siteIds+",").indexOf(","+siteIds+",")==-1){
				siteIds = "-1";
				logger.info("用户【"+operator.getName()+"】没有站点数据的权限，providerId=" + site_id);
			}
			logger.info("site_ids: " + site_id + ",title: " + title + ",pid: " + pid);

			if(pid == 0 && "0".equals(site_id)){//栏目链接站点需要数据权限
				menuIds = roleService.queryIdsWithAuth(operator,"menu");
			}
			else{
				//是否是权限内的子栏目

			}
		}

		JSONArray root = new JSONArray();
		try {
			List<Column> list = columnService.findAll(pid, site_id, title, ignore_id, menuIds);
			JSONObject json = null;

			JSONArray leafArr = new JSONArray();
			for(Column column: list) {

				if(column == null) continue;

				json = parserColumn(column);

				boolean isLeaf = column.getSub_count() <= 0 ? true : false;

				if(isLeaf) {
					leafArr.add(json);
				} else {
					root.add(json);
				}

			}

			root.addAll(leafArr);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			logger.info("--:" + root);
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
			req = BmUtil.resolveMultipart(req);
			boolean has_base_info = false;
			try {
				logger.info("--:column_base_info=" + req.getParameter("column_base_info"));
				has_base_info = Integer.parseInt(req.getParameter("column_base_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
			Column column = new Column();

			if(has_base_info) {
				column.setParent_id(req.getParameter("c_pid"));
				updateColumn(column, req);
				CheckResult result = checkValid(req, column);
				if (result.getResult() == 0) {
					issuc = columnService.save(column);
					if(!issuc) {
						msg = "添加栏目基本信息失败";
						throw new Exception(msg);
					}
				} else {
					msg = result.getMsg();
					throw new Exception(msg);
				}
			}

			boolean has_img_info = false;
			try {
				logger.info("--:column_img_info=" + req.getParameter("column_img_info"));
				has_img_info = Integer.parseInt(req.getParameter("column_img_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if(issuc && has_img_info) {
				issuc = imgController.saveOrUpdateImg(req, column.getId(), column.getProvider_id(), "0");
				if(!issuc) {
					msg = "添加栏目图片信息失败";
					throw new Exception(msg);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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

		boolean issuc = false;
		String msg = "修改失败";
		try {
			req = BmUtil.resolveMultipart(req);
			boolean has_base_info = false;
			try {
				logger.info("--:column_base_info=" + req.getParameter("column_base_info"));
				has_base_info = Integer.parseInt(req.getParameter("column_base_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			String c_id = req.getParameter("c_id");
			long id = Long.parseLong(c_id); 
			Column column = columnService.findById(c_id);

			if(column != null && has_base_info) {
				updateColumn(column, req);

				CheckResult result = checkValid(req, column);
				if (result.getResult() == 0) {
					issuc = columnService.update(column);
					if(!issuc) {
						msg = "修改栏目基本信息失败";
						throw new Exception(msg);
					}

					Cont cont = contService.findById(column.getId());
					if (cont != null){//混排栏目
						cont.setActive_time(column.getActive_time());
						cont.setDeactive_time(column.getDeactive_time());
						cont.setStatus(column.getStatus()==1?11:column.getStatus());
						cont.setProvider_id(column.getProvider_id());
						cont.setName(column.getTitle());
						contService.update(cont);
					}
					//不用处理了 ContVideo contVideo = contService.findById(column.getId());
				} else {
					msg = result.getMsg();
					throw new Exception(msg);
				}
			}

			boolean has_img_info = false;
			try {
				logger.info("--:column_img_info=" + req.getParameter("column_img_info"));
				has_img_info = Integer.parseInt(req.getParameter("column_img_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if(has_img_info && column != null) {
				issuc = imgController.saveOrUpdateImg(req, column.getId(), column.getProvider_id(), "0");
				if(!issuc) {
					msg = "修改栏目图片信息失败";
					throw new Exception(msg);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (issuc) {
				msg = "修改成功";
			}

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			resp.getWriter().print(rootJson);
		}
	}

	private void updateColumn(Column column, HttpServletRequest req) {
		try {
			column.setTitle(req.getParameter("title"));
			column.setOrder_num(Long.parseLong(req.getParameter("order_num")));
			column.setSite_id(req.getParameter("site_id"));
			column.setStatus(Integer.parseInt(req.getParameter("status")));
			column.setStruct_type(req.getParameter("struct_type"));
			column.setResource_type(req.getParameter("resource_type"));
			column.setAct_type(req.getParameter("act_type"));

			String link_url = req.getParameter("link_url");
			logger.info("-----iyadi-----link_url: " + link_url);
			link_url = link_url == null ? "" : link_url;
			column.setLink_url(link_url);

			String islocal = req.getParameter("islocal");
			logger.info("-----iyadi-----islocal: " + islocal);
			islocal = islocal == null ? "" : islocal;
			column.setIslocal(islocal);

			String is_autoplay = req.getParameter("is_autoplay");
			logger.info("-----iyadi-----is_autoplay: " + is_autoplay);
			if("on".equals(is_autoplay)) {
				column.setIs_autoplay("1");
			} else {
				column.setIs_autoplay("0");
			}

			String shortcut_contid = req.getParameter("shortcut_contid");
			logger.info("-----iyadi-----shortcut_contid: " + shortcut_contid);
			shortcut_contid = shortcut_contid == null ? "" : shortcut_contid;
			column.setShortcut_contid(shortcut_contid);

			column.setIs_shortcut(req.getParameter("is_shortcut"));
			column.setProvider_id(req.getParameter("provider_id"));
			column.setActive_time(BmUtil.parseDate(req.getParameter("active_time")));
			column.setDeactive_time(BmUtil.parseDate(req.getParameter("deactive_time")));
			column.setVersion(req.getParameter("version"));
			column.setUsergroup_ids_mac(req.getParameter("usergroup_ids_mac"));
			column.setUsergroup_ids_zone(req.getParameter("usergroup_ids_zone"));
			column.setUsergroup_ids_model(req.getParameter("usergroup_ids_model"));
			column.setUsergroup_ids_channel(req.getParameter("usergroup_ids_channel"));
			column.setUsergroup_ids_mac2(req.getParameter("usergroup_ids_mac2"));
			column.setUsergroup_ids_zone2(req.getParameter("usergroup_ids_zone2"));
			column.setUsergroup_ids_model2(req.getParameter("usergroup_ids_model2"));
			column.setUsergroup_ids_channel2(req.getParameter("usergroup_ids_channel2"));

		} catch(Exception e) {
			logger.error(e.getMessage(), e);
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
			String id = req.getParameter("c_id");
			Column column = columnService.findById(id);

			int submenucount = columnService.countAll(Long.parseLong(column.getId()), InitManager.Defaut_Unselected_ID, column.getSite_id());
			if (submenucount > 0){//有子栏目则保存失败
				msg = "删除失败！请先删除该栏目下的子栏目【栏目id="+column.getId()+"】";
			}
			else{
				try {
					contService.delete(id);
					contVideoService.delete(id);
					imgService.deleteAll(id, "");
					relaMenuAndContService.deleteRelaMenuAndCont(id, null);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				issuc = columnService.delete(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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

	private boolean needAuth(HttpServletRequest req) {
		String auth = req.getParameter("auth");
		boolean flag =!StringUtils.isEmpty(auth)&&"1".equals(auth);
		return flag;
	}

}
