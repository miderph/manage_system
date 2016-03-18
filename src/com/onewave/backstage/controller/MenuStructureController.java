package com.onewave.backstage.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.StringTool;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ImgService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.RelaMenuAndContService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.service.SiteService;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.backstage.service.UserGroupService;
import com.onewave.backstage.util.BmUtil;

@Controller("menuStructureController")
@RequestMapping("/menus/*")
public class MenuStructureController extends MultiActionController {
   @Autowired
   @Qualifier("menuService")
	private MenuService menuService;
   @Autowired
   @Qualifier("statusDictService")
	private StatusDictService statusDictService;
   @Autowired
   @Qualifier("imgService")
	private ImgService imgService;
   @Autowired
   @Qualifier("siteService")
	private SiteService siteService;
   @Autowired
   @Qualifier("contService")
	private ContService contService;
	@Autowired
	@Qualifier("userGroupService")
	private UserGroupService userGroupService;

	@Autowired
	private RelaMenuAndContService relaMenuAndContService;
	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;

//	@RequestMapping("query_menutree.do")
//	public ModelAndView queryMenuTreeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		
//		logger.info("--:");
//		
//		String site_id = req.getParameter("site_id");
//		int status_id = Integer.parseInt(req.getParameter("status_id"));
//		long parent_id = Long.parseLong(req.getParameter("parent_id"));
//		
//		if(site_id.equals("null")) {
//			site_id = "";
//		}
//		
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		
//		/*long site_id = 10000;
//		String area_id = "010";
//		long uag_id = 13;
//		long parent_id = 0;*/
//		
//		logger.info("search menu list:site_id=" + site_id + ",status=" + status_id + ",parent_id=" + parent_id);
//		
//		List<Menu> menuList = this.menuService.findMenu(site_id, parent_id, status_id);
//		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "STRUCT_TYPE");
//		Map<String, String> menuStructTypeMap = new HashMap<String, String>();
//		for(StatusDict statusDict: statusDictList){
//			menuStructTypeMap.put(createStructTypeKey(statusDict.getStatus()).trim(), statusDict.getDescription());
//		}
//		
//		JSONArray jsonArray = new JSONArray();
//		JSONObject catalogJson=new JSONObject();
//		
//		for(Menu menu: menuList){
//			catalogJson.put("id", menu.getId());
//			catalogJson.put("taskid", menu.getId());
//			catalogJson.put("text", menu.getTitle());
//			catalogJson.put("singleClickExpand", true);
//			catalogJson.put("menu_task_structId", menu.getStruct_type());
//			catalogJson.put("iconCls", "no-node-icon");
//			catalogJson.put("qtip", menuStructTypeMap.get(createStructTypeKey(menu.getStruct_type()).trim()));
//			jsonArray.add(catalogJson);
//		}
//		
//		JSONObject json = new JSONObject();
//		
//		json.put("root", jsonArray);
//		logger.info(jsonArray);
//		pw.print(jsonArray);
//		return null;
//	}
	
	@RequestMapping("query_menutree_hasname.do")
	public ModelAndView queryMenuTreeHasNameHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		
		String site_ids = req.getParameter("site_id");
		int status_id = Integer.parseInt(req.getParameter("status_id"));
		String name = req.getParameter("name");
		int parent_id = Integer.parseInt(req.getParameter("parent_id"));
		if(needAuth(req)&&(StringUtils.isEmpty(site_ids) || "-1".equals(site_ids))){ //没有指定具体site_id时，查询权限内所有。
			Operator operator= (Operator) req.getSession().getAttribute("user");
			site_ids = siteService.queryIdsWithAuth(operator);
		}
		logger.info("search menu list:site_ids=" + site_ids + ",status=" + status_id + ",name=" + name + ",parent_id=" + parent_id);
		String ignore_id = req.getParameter("ignore_id");
		List<Menu> menuList = this.menuService.findMenu(site_ids, parent_id, status_id, name,ignore_id);
		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "STRUCT_TYPE");
		Map<String, String> menuStructTypeMap = new HashMap<String, String>();
		for(StatusDict statusDict: statusDictList){
			menuStructTypeMap.put(createStructTypeKey(statusDict.getStatus()).trim(), statusDict.getDescription());
		}
		
		JSONArray jsonArray = new JSONArray();
		JSONObject catalogJson=new JSONObject();
		
		for(Menu menu: menuList){
			catalogJson.put("id", menu.getId());
			catalogJson.put("taskid", menu.getId());
			String status = (menu.getStatus()!=1?"【已禁用】":"");
			switch (menu.getStatus()){
		     case -1: status = "【禁用】";break;
		     case 0: status = "【待审】";break;
		     //case 1: status = "";break;
			}
			if (!"0".equals(menu.getIs_shortcut())){
				if ("1".equals(menu.getIs_shortcut()))
				   status += "【内容链接】";
				else if ("2".equals(menu.getIs_shortcut()))
			       status += "【栏目链接】";
			}
			if ("Hyperlink".equals(menu.getAct_type())){
			   status += "【超链接】";
			}
			catalogJson.put("text", menu.getTitle()+status);
			catalogJson.put("singleClickExpand", true);
			catalogJson.put("menu_task_structId", menu.getStruct_type());
			catalogJson.put("iconCls", "no-node-icon");
			catalogJson.put("qtip", menuStructTypeMap.get(createStructTypeKey(menu.getStruct_type()).trim()));
			jsonArray.add(catalogJson);
		}
		
		JSONObject json = new JSONObject();
		
		json.put("root", jsonArray);
		logger.info(jsonArray);
		pw.print(jsonArray);
		return null;
	}
	@RequestMapping("query_sub_menu.do")
   public ModelAndView querySubMenuByPidHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      
      logger.info("--:");
      resp.setContentType("application/json; charset=UTF-8");
      PrintWriter pw = resp.getWriter();

      String site_ids = req.getParameter("site_id");
      int parent_id = Integer.parseInt(req.getParameter("parent_id"));
      if(needAuth(req)&&(StringUtils.isEmpty(site_ids) || "-1".equals(site_ids))){ //没有指定具体site_id时，查询权限内所有。
         Operator operator= (Operator) req.getSession().getAttribute("user");
         site_ids = siteService.queryIdsWithAuth(operator);
      }
      logger.info("parent_id: " + parent_id);

      if (parent_id > 0){
         List<Menu> menuList = this.menuService.findMenu(site_ids, parent_id, "");
         List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "STRUCT_TYPE");
         Map<String, String> menuStructTypeMap = new HashMap<String, String>();
         for(StatusDict statusDict: statusDictList){
            menuStructTypeMap.put(createStructTypeKey(statusDict.getStatus()).trim(), statusDict.getDescription());
         }
         
         JSONArray jsonArray = new JSONArray();
         JSONObject catalogJson=new JSONObject();
         
         for(Menu menu: menuList){
            catalogJson.put("id", menu.getId());
            catalogJson.put("text", menu.getTitle());
            catalogJson.put("menu_task_structId", menu.getStruct_type());
            catalogJson.put("qtip", menuStructTypeMap.get(createStructTypeKey(menu.getStruct_type()).trim()));
            jsonArray.add(catalogJson);
         }
         JSONObject json = new JSONObject();
         
         json.put("results", menuList.size());
         json.put("datastr", jsonArray);
         logger.info(json);
         pw.print(json);
      }
      else{
         String json = "{'datastr':{'results':0}}";
         logger.info(json);
         pw.print(json);
      }
      
      return null;
   }
	@RequestMapping("query_menu_for_mar.do")
   public ModelAndView querySubMenuForMarHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      
      logger.info("--:");
      resp.setContentType("application/json; charset=UTF-8");
      PrintWriter pw = resp.getWriter();

      String site_ids = req.getParameter("site_id");
      int parent_id = Integer.parseInt(req.getParameter("parent_id"));
      if(needAuth(req)&&(StringUtils.isEmpty(site_ids) || "-1".equals(site_ids))){ //没有指定具体site_id时，查询权限内所有。
         Operator operator= (Operator) req.getSession().getAttribute("user");
         site_ids = siteService.queryIdsWithAuth(operator);
      }
      logger.info("parent_id: " + parent_id);

      if (parent_id > 0){
         List<Menu> menuList = this.menuService.querySubMenuForMar(parent_id);
         List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "STRUCT_TYPE");
         Map<String, String> menuStructTypeMap = new HashMap<String, String>();
         for(StatusDict statusDict: statusDictList){
            menuStructTypeMap.put(createStructTypeKey(statusDict.getStatus()).trim(), statusDict.getDescription());
         }
         
         JSONArray jsonArray = new JSONArray();
         JSONObject catalogJson=new JSONObject();
         
         for(Menu menu: menuList){
            catalogJson.put("id", menu.getId());
            catalogJson.put("text", menu.getTitle());
            catalogJson.put("menu_task_structId", menu.getStruct_type());
            catalogJson.put("qtip", menuStructTypeMap.get(createStructTypeKey(menu.getStruct_type()).trim()));
            jsonArray.add(catalogJson);
         }
         JSONObject json = new JSONObject();
         
         json.put("results", menuList.size());
         json.put("datastr", jsonArray);
         logger.info(json);
         pw.print(json);
      }
      else{
         String json = "{'datastr':{'results':0}}";
         logger.info(json);
         pw.print(json);
      }
      
      return null;
   }
   
//	@RequestMapping("click_menu.do")
//	public ModelAndView clickMenuDataHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		logger.info("--:");
//		
//		String id = req.getParameter("id");
//		logger.info("id: " + id);
//		
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		
//		Menu menu = menuService.findById(id);
//		
//		List<UserGroup> userGroupList = this.userGroupService.findAll();
//		Map<String, String> userGroupMap = new HashMap<String, String>();
//		for(UserGroup userGroup: userGroupList){
//			userGroupMap.put(userGroup.getId(), userGroup.getName());
//		}
//
//		JSONArray jsonArray = new JSONArray();
//		JSONObject catalogJson=new JSONObject();
//		
//		catalogJson.put("catalog_name", menu.getTitle());
//		catalogJson.put("statusId", menu.getStatus());
//		catalogJson.put("version", menu.getVersion());
//		catalogJson.put("structTypeId", menu.getStruct_type());
//		catalogJson.put("providerId", menu.getProvider_id());
//		catalogJson.put("order_num", menu.getOrder_num());
//		catalogJson.put("usergroup_ids_mac", menu.getUsergroup_ids_mac());
//        String a="";
//        if(menu.getUsergroup_ids_mac()!=null){
//           String[] usergroup_ids = menu.getUsergroup_ids_mac().split(",");
//           for (int i = 0; i < usergroup_ids.length; i++) {
//              a = a+userGroupMap.get(usergroup_ids[i])+",";
//           }
//        }
//        catalogJson.put("usergroup_names_mac", a);
//
//        catalogJson.put("usergroup_ids_zone", menu.getUsergroup_ids_zone());
//        a="";
//        if(menu.getUsergroup_ids_zone()!=null){
//           String[] usergroup_ids = menu.getUsergroup_ids_zone().split(",");
//           for (int i = 0; i < usergroup_ids.length; i++) {
//              a = a+userGroupMap.get(usergroup_ids[i])+",";
//           }
//        }
//        catalogJson.put("usergroup_names_zone", a);
//
//        catalogJson.put("usergroup_ids_model", menu.getUsergroup_ids_model());
//        a="";
//        if(menu.getUsergroup_ids_model()!=null){
//           String[] usergroup_ids = menu.getUsergroup_ids_model().split(",");
//           for (int i = 0; i < usergroup_ids.length; i++) {
//              a = a+userGroupMap.get(usergroup_ids[i])+",";
//           }
//        }
//        catalogJson.put("usergroup_names_model", a);
//		
//		String actTypeId = menu.getAct_type();
//		String islocalId = "-1";
//		if(actTypeId!=null || "getChannel".equals(actTypeId)) {
//			islocalId = menu.getIslocal();
//		} else if(actTypeId==null) {
//			actTypeId = "-1";
//		}
//		catalogJson.put("actTypeId", actTypeId);
//		catalogJson.put("islocalId", islocalId);
//		
//		catalogJson.put("resourceTypeId", menu.getResource_type());
//		//**********************************************************
//		catalogJson.put("shortcut_contid", menu.getShortcut_contid());
//		catalogJson.put("is_shortcut", menu.getIs_shortcut());
//		if ("1".equals(menu.getIs_shortcut())){
//			Cont cont=null;
//			try {
//				cont = contService.findById(menu.getShortcut_contid());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			catalogJson.put("shortcut_contname",cont==null?"":menu.getShortcut_contid()+"|"+cont.getName());
//		}
//		else if("2".equals(menu.getIs_shortcut())){
//			Menu linkmenu = null;
//			try {
//				linkmenu = menuService.findById(menu.getShortcut_contid());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			catalogJson.put("shortcut_contname",linkmenu==null?"":menu.getShortcut_contid()+"|"+linkmenu.getTitle());
//		}
//		
//		//**********************************************************
//		catalogJson.put("menu_link_url", menu.getLink_url());
//		catalogJson.put("is_autoplay", menu.getIs_autoplay());
//		
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DAY_OF_MONTH, -1);
//		Date beginDate = cal.getTime();
//		cal.add(Calendar.YEAR, 50);
//		Date endDate = cal.getTime();
//
//		Date activiItem = menu.getActive_time() == null ? beginDate : menu.getActive_time();
//		String activiItemS = "0000-00-00 00:00:00";
//		if(activiItem != null) {
//			activiItemS = BmUtil.formatDate(activiItem);
//		}
//		catalogJson.put("menuactivetime1", activiItemS);
//		
//		Date deactiviItem = menu.getDeactive_time() == null ? endDate : menu.getDeactive_time();
//		String deactiviItemS = "0000-00-00 00:00:00";
//		if(deactiviItem != null) {
//			deactiviItemS = BmUtil.formatDate(deactiviItem);
//		}
//		catalogJson.put("menudeactivetime1", deactiviItemS);
//		
//		List<Img> imgList = imgService.findAll(id, "0");
//		if(imgList!=null && imgList.size()>0) {
//			Img img = imgList.get(0);
//			
//			Date menu_img_active_time = img.getActive_time() == null ? beginDate : img.getActive_time();
//			String menu_img_active_timeS = "0000-00-00 00:00:00";
//			if(menu_img_active_time != null) {
//				menu_img_active_timeS = BmUtil.formatDate(menu_img_active_time);
//			}
//			catalogJson.put("menu_img_active_time_1", menu_img_active_timeS);
//			
//			Date menu_img_deactive_time = img.getDeactive_time() == null ? endDate : img.getDeactive_time();
//			String menu_img_deactive_timeS = "0000-00-00 00:00:00";
//			if(menu_img_deactive_time != null) {
//				menu_img_deactive_timeS = BmUtil.formatDate(menu_img_deactive_time);
//			}
//			catalogJson.put("menu_img_deactive_time_1", menu_img_deactive_timeS);
//			
//			catalogJson.put("menu_img_intro", img.getIntro());
//			
//			String img_url = StringTool.null2Empty(img.getUrl());
//			String img_url_little = StringTool.null2Empty(img.getUrl_little());
//			String img_url_icon = StringTool.null2Empty(img.getUrl_icon());
//			String img_url_4_squares = StringTool.null2Empty(img.getUrl_4_squares());
//			String img_url_6_squares = StringTool.null2Empty(img.getUrl_6_squares());
//         	
//			catalogJson.put("menu_img_url", img_url);
//			catalogJson.put("menu_img_url_little", img_url_little);
//			catalogJson.put("menu_img_url_icon", img_url_icon);
//			catalogJson.put("menu_img_url_4_squares", img_url_4_squares);
//			catalogJson.put("menu_img_url_6_squares", img_url_6_squares);
//         
//			catalogJson.put("menu_img_isurlused", img.getIs_url_used());
//			catalogJson.put("menu_img_id", img.getId());
//			catalogJson.put("plat_groupId", img.getPlatgroup_id());
//			if (StringTool.isEmpty(img_url_4_squares))
//				catalogJson.put("menu_img_url_4_squares_show", "");
//	        else if(img_url_4_squares.startsWith("http://")) {
//	        	catalogJson.put("menu_img_url_4_squares_show", img_url_4_squares);
//			}else {
//				catalogJson.put("menu_img_url_4_squares_show", InitManager.getRootHttpPath() + img_url_4_squares);
//			}
//			if (StringTool.isEmpty(img_url_6_squares))
//				catalogJson.put("menu_img_url_6_squares_show", "");
//	        else if(img_url_6_squares.startsWith("http://")) {
//	        	catalogJson.put("menu_img_url_6_squares_show", img_url_6_squares);
//			} else {
//				catalogJson.put("menu_img_url_6_squares_show", InitManager.getRootHttpPath() + img_url_6_squares);
//			}
//			if (StringTool.isEmpty(img_url))
//            catalogJson.put("menu_img_url_show", "");
//         else if(img_url.startsWith("http://")) {
//				catalogJson.put("menu_img_url_show", img_url);
//			} else {
//				catalogJson.put("menu_img_url_show", InitManager.getRootHttpPath() + img_url);
//			}
//			
//			if (StringTool.isEmpty(img_url_little))
//            catalogJson.put("menu_img_url_little_show", "");
//         else if(img_url_little.startsWith("http://")) {
//				catalogJson.put("menu_img_url_little_show", img_url_little);
//			} else {
//				catalogJson.put("menu_img_url_little_show", InitManager.getRootHttpPath() + img_url_little);
//			}
//
//         if (StringTool.isEmpty(img_url_icon))
//            catalogJson.put("menu_img_url_icon_show", "");
//         else if(img_url_icon.startsWith("http://")) {
//            catalogJson.put("menu_img_url_icon_show", img_url_icon);
//         } else {
//            catalogJson.put("menu_img_url_icon_show", InitManager.getRootHttpPath() + img_url_icon);
//         }
//		} else {
//			catalogJson.put("menu_img_id", "-1");
//		}
//			
//		jsonArray.add(catalogJson);
//		
//		JSONObject json = new JSONObject();
//		
//		json.put("success", true);
//		json.put("siteStructure_data", jsonArray);
//		logger.info(json);
//		pw.print(json);
//		
//		return null;
//	}
//	
	@RequestMapping("status_menu.do")
	public ModelAndView queryMenuStatusHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		logger.info("--:");
		
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		
		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "STATUS");
		
		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();
		
		groupJson.put("statusId", InitManager.Defaut_Unselected_ID);
		groupJson.put("statusName", "请选择");
		jsonArray.add(groupJson);
		
		for(StatusDict statusDict: statusDictList){
			groupJson.put("statusId", statusDict.getStatus());
			groupJson.put("statusName", statusDict.getDescription());
			jsonArray.add(groupJson);
		}
		
		JSONObject json = new JSONObject();
		
		json.put("results", statusDictList.size());
		json.put("status_data", jsonArray);
		logger.info(json);
		pw.print(json);
		
		return null;
	}
	
	@RequestMapping("struct_type_menu.do")
	public ModelAndView queryMenuStructTypeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		logger.info("--:");
		
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		
		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "STRUCT_TYPE");
		
		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();
		
		groupJson.put("structTypeId", InitManager.Defaut_Unselected_ID);
		groupJson.put("structTypeName", "请选择");
		jsonArray.add(groupJson);
		
		for(StatusDict statusDict: statusDictList){
			groupJson.put("structTypeId", statusDict.getStatus());
			groupJson.put("structTypeName", statusDict.getDescription());
			jsonArray.add(groupJson);
		}
		
		JSONObject json = new JSONObject();
		
		json.put("results", statusDictList.size());
		json.put("structType_data", jsonArray);
		logger.info(json);
		pw.print(json);
		
		return null;
	}
	
//	@RequestMapping("resource_type_menu.do")
//	public ModelAndView queryMenuResourceTypeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		
//		logger.info("--:");
//		
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		
//		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "RESOURCE_TYPE");
//		
//		JSONArray jsonArray = new JSONArray();
//		JSONObject groupJson=new JSONObject();
//		
//		groupJson.put("resourceTypeId", InitManager.Defaut_Unselected_ID);
//		groupJson.put("resourceTypeName", "请选择");
//		jsonArray.add(groupJson);
//		int i=1;
//		for(StatusDict statusDict: statusDictList){
//			groupJson.put("resourceTypeId", statusDict.getStatus());
//			groupJson.put("resourceTypeName", statusDict.getDescription());
//			jsonArray.add(groupJson);
//		}
//		
//		JSONObject json = new JSONObject();
//		
//		json.put("results", statusDictList.size());
//		json.put("resourceType_data", jsonArray);
//		logger.info(json);
//		pw.print(json);
//		
//		return null;
//	}
	
	@RequestMapping("provider_menu.do")
	public ModelAndView queryMenuProviderHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
	   resp.setContentType("application/json; charset=UTF-8");
	   PrintWriter pw = resp.getWriter();

		String siteId = req.getParameter("siteId");
		logger.info("--------------siteId:" + siteId);
		List<ContProvider> contProviderList  = new ArrayList<ContProvider>();
		Operator operator =(Operator) req.getSession().getAttribute("user");
		//String siteId = "22";
		if(needAuth(req)){ //根据权限获取
         contProviderList= roleService.queryProviderListWithAuth(operator, BmUtil.isAdminOperator(req));
		}else{//获取所有
			contProviderList= this.menuService.queryContProviderBySiteId(siteId);
		}
		
		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();
		
		groupJson.put("providerId", InitManager.Defaut_Unselected_ID);
		groupJson.put("providerName", "请选择");
		jsonArray.add(groupJson);
		int i=1;
		for(ContProvider contProvider: contProviderList){
			groupJson.put("providerId", contProvider.getId());
			groupJson.put("providerName", contProvider.getName());
			jsonArray.add(groupJson);
		}
		
		JSONObject json = new JSONObject();
		
		json.put("results", contProviderList.size());
		json.put("provider_data", jsonArray);
		logger.info(json);
		pw.print(json);
		
		return null;
	}

	private boolean needAuth(HttpServletRequest req) {
		String auth = req.getParameter("auth");logger.info("--------------need auth:" + auth);
		boolean flag =!StringUtils.isEmpty(auth)&&"1".equals(auth);
		return flag;
	}
	
//	@RequestMapping("act_type_menu.do")
//	public ModelAndView queryMenuActTypeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		
//		logger.info("--:");
//		
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		
//		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "ACT_TYPE");
//		
//		JSONArray jsonArray = new JSONArray();
//		JSONObject groupJson=new JSONObject();
//		
//		groupJson.put("actTypeId", InitManager.Defaut_Unselected_ID);
//		groupJson.put("actTypeName", "请选择");
//		jsonArray.add(groupJson);
//		int i=1;
//		for(StatusDict statusDict: statusDictList){
//			groupJson.put("actTypeId", statusDict.getStatus());
//			groupJson.put("actTypeName", statusDict.getDescription());
//			jsonArray.add(groupJson);
//		}
//		
//		JSONObject json = new JSONObject();
//		
//		json.put("results", statusDictList.size());
//		json.put("actType_data", jsonArray);
//		logger.info(json);
//		pw.print(json);
//		
//		return null;
//	}
	
	@RequestMapping("islocal_menu.do")
	public ModelAndView queryMenuIslocalHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		logger.info("--:");
		
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		
		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_MENU", "ISLOCAL");
		
		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();
		
		groupJson.put("islocalId", InitManager.Defaut_Unselected_ID);
		groupJson.put("islocalName", "请选择");
		jsonArray.add(groupJson);
		int i=1;
		for(StatusDict statusDict: statusDictList){
			groupJson.put("islocalId", statusDict.getStatus());
			groupJson.put("islocalName", statusDict.getDescription());
			jsonArray.add(groupJson);
		}
		
		JSONObject json = new JSONObject();
		
		json.put("results", statusDictList.size());
		json.put("islocal_data", jsonArray);
		logger.info(json);
		pw.print(json);
		
		return null;
	}
	
	private String createStructTypeKey(String key) {
		if(key==null) {
			key = "0";
		}
		String result = key;
		
		int intKey = Integer.parseInt(key);
		if(intKey<0) {
			intKey = -intKey;
			result = "key_" + intKey;;
		}
		
		return result;
	}
	
//	@RequestMapping("save_update_menu.do")
//	public ModelAndView saveAndUpdateMenuHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//
//		logger.info("--:");
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		boolean isSuccess = false;
//		String result = "";
//		String isNewNode = "";
//		try{
//			String parentId = req.getParameter("parentId");logger.info("---------parentId:"+parentId);
//			String siteId = req.getParameter("siteId");logger.info("---------siteId:"+siteId);
//			String title = req.getParameter("catalog_name");logger.info("---------title:"+title);
//			String order_num = req.getParameter("order_num");try{order_num=""+Integer.parseInt(order_num);}catch(Exception E){order_num="-1";}logger.info("---------order_num:"+order_num);
//			String structType = req.getParameter("structType");logger.info("---------structType:"+structType);
//			int status = Integer.parseInt(req.getParameter("menuStatus"));logger.info("---------status:"+status);
//			String providerId = req.getParameter("providerId");logger.info("---------providerId:"+providerId);
//			String activeTimeStr = req.getParameter("activeTime");activeTimeStr = activeTimeStr.replaceAll("T", " ");logger.info("---------activeTime:"+activeTimeStr);
//			Date activeTime = BmUtil.parseDate(activeTimeStr);
//			String deactiveTimeStr = req.getParameter("deactiveTime");deactiveTimeStr = deactiveTimeStr.replaceAll("T", " ");logger.info("---------deactiveTime:"+deactiveTimeStr);
//			Date deactiveTime = BmUtil.parseDate(deactiveTimeStr);
//			String actType = req.getParameter("actType");logger.info("---------actType:"+actType);
//			String islocal = req.getParameter("islocal");logger.info("---------islocal:"+islocal);
//			String resourceType = req.getParameter("resourceType");logger.info("---------resourceType:"+resourceType);
//			//*******************************cuihehui**********************************************************************************      
//			String is_shortcut = req.getParameter("is_shortcut");logger.info("---------is_shortcut:"+is_shortcut);
//			String shortcut_contid = req.getParameter("shortcut_contid");logger.info("---------shortcut_contid:"+shortcut_contid);
//			//*******************************cuihehui**********************************************************************************
//			String linkUrl = req.getParameter("linkUrl");logger.info("---------linkUrl:"+linkUrl);
//			int is_autoplay = 0;try{is_autoplay ="".equals(req.getParameter("is_autoplay")) ?0:Integer.parseInt(req.getParameter("is_autoplay"));}catch(Exception E){}logger.info("---------is_autoplay:"+is_autoplay);
//			isNewNode = req.getParameter("isNewNode");logger.info("---------isNewNode:"+isNewNode);
//			String menu_img_id = req.getParameter("menu_img_id");logger.info("---------menu_img_id:"+menu_img_id);
//			String plat_groupId = req.getParameter("plat_groupId");logger.info("---------plat_groupId:"+plat_groupId);
//			String menu_img_intro_form = req.getParameter("menu_img_intro_form");logger.info("---------menu_img_intro_form:"+menu_img_intro_form);
//			String menu_img_url = req.getParameter("menu_img_url");logger.info("---------menu_img_url:"+menu_img_url);
//			String menu_img_url_little = req.getParameter("menu_img_url_little");logger.info("---------menu_img_url_little:"+menu_img_url_little);
//			String menu_img_url_icon = req.getParameter("menu_img_url_icon");logger.info("---------menu_img_url_icon:"+menu_img_url_icon);
//			//*****************************************************************************************************************************
//			String menu_img_url_4_squares = req.getParameter("menu_img_url_4_squares"); logger.info("---------menu_img_url_icon:"+menu_img_url_4_squares);
//			String menu_img_url_6_squares = req.getParameter("menu_img_url_6_squares"); logger.info("---------menu_img_url_icon:"+menu_img_url_6_squares);
//			//*****************************************************************************************************************************
//
//			String menu_img_active_timeStr = req.getParameter("menu_img_active_time");menu_img_active_timeStr = menu_img_active_timeStr.replaceAll("T", " ");logger.info("---------menu_img_active_time:"+menu_img_active_timeStr);
//			Date menu_img_active_time = BmUtil.parseDate(menu_img_active_timeStr);
//			String menu_img_deactive_timeStr = req.getParameter("menu_img_deactive_time");menu_img_deactive_timeStr = menu_img_deactive_timeStr.replaceAll("T", " ");logger.info("---------menu_img_deactive_time:"+menu_img_deactive_timeStr);
//			Date menu_img_deactive_time = BmUtil.parseDate(menu_img_deactive_timeStr);
//			String menu_img_isurlused = req.getParameter("menu_img_isurlused"); menu_img_isurlused=menu_img_isurlused==null?"0":menu_img_isurlused; logger.info("---------menu_img_isurlused:"+menu_img_isurlused);      
//			String userGroupIdsMac = req.getParameter("usergroup_ids_mac");logger.info("---------usergroup_ids_mac:"+userGroupIdsMac);
//			String userGroupIdsZone = req.getParameter("usergroup_ids_zone");logger.info("---------usergroup_ids_zone:"+userGroupIdsZone);
//			String userGroupIdsModel = req.getParameter("usergroup_ids_model");logger.info("---------usergroup_ids_model:"+userGroupIdsModel);
//
//			if (!"0".equals(is_shortcut) && isNewNode!=null && !"-1".equals(isNewNode)){//只有叶子节点能设置为快捷方式
//				List<Menu> tempMenuList = menuService.findMenu(siteId, Long.parseLong(isNewNode), InitManager.Defaut_Unselected_ID);
//				if (tempMenuList.size() > 0){//有子栏目则保存失败
//					result = "{success:false, info:'该栏目下存在多级栏目，保存失败！【栏目id="+isNewNode+"】', catalog_id:" + isNewNode +"}";
//					throw new Exception("该栏目下存在多级栏目，保存失败！【栏目id="+isNewNode+"】");
//				}
//				int contNum = relaMenuAndContService.countAllForMAR(isNewNode, InitManager.Defaut_Unselected_ID);
//				if (contNum > 0){//不能有变编排的资产
//					result = "{success:false, info:'该栏目下存在编排的资产，保存失败！【栏目id="+isNewNode+"】', catalog_id:" + isNewNode +"}";
//					throw new Exception("该栏目下存在编排的资产，保存失败！【栏目id="+isNewNode+"】");
//				}
//			}
//			if ("2".equals(is_shortcut)){//栏目链接站点不允许有栏目快捷方式
//				if ("0".equals(siteId)){
//					result = "{success:false, info:'保存失败！栏目链接站点不允许有栏目快捷方式！【栏目id="+isNewNode+"】', catalog_id:" + isNewNode +"}";
//					throw new Exception("保存失败！栏目链接站点不允许有栏目快捷方式！【栏目id="+isNewNode+"】");
//				}
//				else{
////					List<Menu> tempMenuList = menuService.findByShortCutId(shortcut_contid);
////					boolean isExistsOtherMenuLinkInSameSite = false;
////					for (Menu tempMenu : tempMenuList){
////						if (tempMenu.getSite_id().equals(siteId) && !tempMenu.getId().equals(isNewNode))
////							isExistsOtherMenuLinkInSameSite = true;
////					}
////					if (isExistsOtherMenuLinkInSameSite){
////						result = "{success:false, info:'保存失败！同一站点下不允许有多个指向同一个栏目的快捷方式！"+shortcut_contid+"', catalog_id:" + isNewNode +"}";
////						throw new Exception("'保存失败！同一站点下不允许有多个指向同一个栏目的快捷方式！"+shortcut_contid+"'");
////					}
////					//还应判断不能指向一个子栏目：栏目链接站点的某栏目已被指向，则这个栏目的子栏目不能再被指向
//				}
//			}
//			
//			Menu menu = null;
//			if(isNewNode!=null && "-1".equals(isNewNode)) {
//				menu = new Menu();
//				menu.setParent_id(parentId);
//			} else {
//				menu = menuService.findById(isNewNode);
//
//				if((menu_img_url != null && !"".equals(menu_img_url.trim()))
//						|| (menu_img_url_little != null && !"".equals(menu_img_url_little.trim()))
//						|| (menu_img_url_icon != null && !"".equals(menu_img_url_icon.trim()))
//						|| (menu_img_url_4_squares != null && !"".equals(menu_img_url_4_squares.trim()))
//						|| (menu_img_url_6_squares != null && !"".equals(menu_img_url_6_squares.trim()))) {
//					if("-1".equals(menu_img_id.trim())) {
//						Img img = new Img();
//						img.setPlatgroup_id(plat_groupId);
//						img.setProvider_id(providerId);
//						img.setTarget_id(isNewNode);
//						img.setUse_type("0");
//						img.setUrl(menu_img_url);
//						img.setUrl_little(menu_img_url_little);
//						img.setUrl_icon(menu_img_url_icon);
//						//************************************************************
//						img.setUrl_4_squares(menu_img_url_4_squares);
//						img.setUrl_6_squares(menu_img_url_6_squares);
//						//************************************************************
//
//						img.setIs_url_used(menu_img_isurlused);
//						img.setIntro(menu_img_intro_form);
//						img.setActive_time(menu_img_active_time);
//						img.setDeactive_time(menu_img_deactive_time);
//						menu_img_id = imgService.saveAndReturnId(img);
//					} else {
//						List<Img> imgList = imgService.findAll(isNewNode, "0");
//						if(imgList!=null && imgList.size()>0) {
//							Img img = imgList.get(0);
//							img.setPlatgroup_id(plat_groupId);
//							img.setProvider_id(providerId);
//							img.setTarget_id(isNewNode);
//							img.setUse_type("0");
//							img.setUrl(menu_img_url);
//							img.setUrl_little(menu_img_url_little);
//							img.setUrl_icon(menu_img_url_icon);
//							img.setUrl_4_squares(menu_img_url_4_squares);
//							img.setUrl_6_squares(menu_img_url_6_squares);
//
//							img.setIs_url_used(menu_img_isurlused);
//							img.setIntro(menu_img_intro_form);
//							img.setActive_time(menu_img_active_time);
//							img.setDeactive_time(menu_img_deactive_time);
//							imgService.update(img);
//						}
//					}
//				}
//			}
//
//			if(menu != null) {
//				menu.setTitle(title);
//				menu.setOrder_num(Long.parseLong(order_num));
//				menu.setSite_id(siteId);
//				menu.setStatus(status);
//				menu.setStruct_type(structType);
//				menu.setResource_type(resourceType);
//				menu.setAct_type(actType);
//				menu.setLink_url(linkUrl);
//				menu.setIs_autoplay(""+is_autoplay);
//				menu.setIs_shortcut(is_shortcut);
//				menu.setShortcut_contid(shortcut_contid);
//				menu.setIslocal(islocal);
//				menu.setProvider_id(providerId);
//				menu.setActive_time(activeTime);
//				menu.setDeactive_time(deactiveTime);
//				menu.setUsergroup_ids_mac(userGroupIdsMac);
//				menu.setUsergroup_ids_zone(userGroupIdsZone);
//				menu.setUsergroup_ids_model(userGroupIdsModel);
//
//			} else {
//				return null;
//			}
//
//			if("-1".equals(isNewNode)) {
//				isSuccess = menuService.save(menu);
//			} else {
//				menu.setId(isNewNode);
//				isSuccess = menuService.update(menu);
//				
//				Cont cont = contService.findById(menu.getId());
//				if (cont != null){//混排栏目
//					cont.setActive_time(menu.getActive_time());
//					cont.setDeactive_time(menu.getDeactive_time());
//					cont.setStatus(menu.getStatus()==1?11:menu.getStatus());
//					contService.update(cont);
//				}
//			}
//			if (isSuccess)
//				result = "{success:"+isSuccess+", info:'保存失败！【栏目id="+isNewNode+"】', catalog_id:" + isNewNode +"}";
//			else
//				result = "{success:"+isSuccess+", info:'保存成功！【栏目id="+isNewNode+"】', catalog_id:" + isNewNode +"}";
//		}catch(Exception E){
//			if ("".equals(result)){
//				result = "{success:false, info:'保存失败！【栏目id="+isNewNode+"】', catalog_id:" + isNewNode +"}";				
//			}
//			E.printStackTrace();
//		}
//		logger.info("-----------result:"+result);
//		pw.print(result);
//
//		return null;
//	}
//	
//	@RequestMapping("delete_menu.do")
//	public ModelAndView deleteMenuHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		
//		logger.info("--:");
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		
//		String id = req.getParameter("contextNode_id");
//		logger.info("-----------id:"+id);
//		if(id!=null) {
//			menuService.delete(id);
//			pw.print("{success:true, errors:{}, catalog_id:" + id +"}");
//		} else {
//			pw.print("{success:false, errors:{}, catalog_id:" + id +"}");
//		}
//		
//		return null;
//	}
//	
//	@RequestMapping("move_node_append_menu.do")
//	public ModelAndView menuMoveNodeAppendHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		
//		logger.info("--:");
//
//		String site_id = req.getParameter("site_id");
//		String presentNode_id = req.getParameter("presentNode_id");
//		String presentParentNode_id = req.getParameter("presentParentNode_id");
//		String dragPresentParentNode_id = req.getParameter("dragPresentParentNode_id");
//		
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		
//		Menu menu = menuService.findById(presentNode_id);
//		long Serial = menu.getOrder_num();
//		
//		long maxNum = menuService.findMaxSerialBySite_idAndParent_id(site_id, presentParentNode_id);
//		
//		menu.setParent_id(presentParentNode_id);
//		menu.setOrder_num(maxNum+1);
//		
//		menuService.update(menu);
//		
//		menuService.modifyBetweenSerialByParentId(site_id, dragPresentParentNode_id, Serial, -1, "sub");
//		
//		pw.print("{success:true, errors:{}}");
//		
//		return null;
//	}
//
//	@RequestMapping("move_node_above_menu.do")
//	public ModelAndView menuMoveNodeAboveHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		
//		logger.info("--:");
//		
//		String presentNode_id = req.getParameter("presentNode_id");
//		String targetNode_id = req.getParameter("targetNode_id");
//		String presentParentNode_id = req.getParameter("presentParentNode_id");
//		String dragPresentParentNode_id = req.getParameter("dragPresentParentNode_id");
//		
//		//logger.info("dragPresentParentNode_id: " + dragPresentParentNode_id);
//		//logger.info("presentNode_id: " + presentNode_id + "\npresentParentNode_id: " + presentParentNode_id);
//		//logger.info("targetNode_id: " + targetNode_id + "\ntargetParentNode_id: " + targetParentNode_id);
//		
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		
//		Menu presentMenu = menuService.findById(presentNode_id);
//		
//		Menu targetMenu = menuService.findById(targetNode_id);
//		String site_id = targetMenu.getSite_id();
//		
//		long presentNodeSerial = menuService.findById(presentNode_id).getOrder_num();
//		long targetNodeSerial = menuService.findById(targetNode_id).getOrder_num();
//		
//		if(dragPresentParentNode_id == presentParentNode_id) {	//同级节点的移动
//			
//			if(presentNodeSerial > targetNodeSerial) {	//当前节点的序号大于目标节点的序号则执行
//				//目标节点以及两者之间的序号加1,当前节点的序号等于目标节点变更前的序号
//				menuService.modifyBetweenSerialByParentId(site_id, presentParentNode_id, targetNodeSerial-1, presentNodeSerial, "add");
//				presentMenu.setOrder_num(targetNodeSerial);
//				presentMenu.setParent_id(presentParentNode_id);
//				menuService.update(presentMenu);
//			} else {
//				//两者之间的序号减1,当前节点的序号等于目标节点的序号减1
//				menuService.modifyBetweenSerialByParentId(site_id, presentParentNode_id, presentNodeSerial, targetNodeSerial, "sub");
//				presentMenu.setOrder_num(targetNodeSerial-1);
//				presentMenu.setParent_id(presentParentNode_id);
//				menuService.update(presentMenu);
//			}
//		} else {	//不同级节点的移动
//			//目标节点及大于目标节点序号的节点序号都加1,修改当前节点的父字段，当前节点的序号改为目标节点的序号,当前节点前一个父节点下的节点序号小于当前节点变更前序号的都减1
//			menuService.modifyBetweenSerialByParentId(site_id, presentParentNode_id, targetNodeSerial-1, -1, "add");
//			presentMenu.setParent_id(presentParentNode_id);
//			presentMenu.setOrder_num(targetNodeSerial);
//			menuService.update(presentMenu);
//			
//			menuService.modifyBetweenSerialByParentId(site_id, dragPresentParentNode_id, presentNodeSerial, -1, "sub");
//		}
//		
//		pw.print("{success:true, errors:{}}");
//		
//		return null;
//	}
//	
//	@RequestMapping("move_node_below_menu.do")
//	public ModelAndView menuMoveNodeBelowHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		
//		logger.info("--:");
//		
//		String presentNode_id = req.getParameter("presentNode_id");
//		String targetNode_id = req.getParameter("targetNode_id");
//		String presentParentNode_id = req.getParameter("presentParentNode_id");
//		String targetParentNode_id = req.getParameter("targetParentNode_id");
//		String dragPresentParentNode_id = req.getParameter("dragPresentParentNode_id");
//		
//		//logger.info("dragPresentParentNode_id: " + dragPresentParentNode_id);
//		//logger.info("presentNode_id: " + presentNode_id + "\npresentParentNode_id: " + presentParentNode_id);
//		//logger.info("targetNode_id: " + targetNode_id + "\ntargetParentNode_id: " + targetParentNode_id);
//		
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		
//		Menu presentMenu = menuService.findById(presentNode_id);
//		
//		Menu targetMenu = menuService.findById(targetNode_id);
//		String site_id = targetMenu.getSite_id();
//		
//		long presentNodeSerial = menuService.findById(presentNode_id).getOrder_num();
//		long targetNodeSerial = menuService.findById(targetNode_id).getOrder_num();
//		
//		if(dragPresentParentNode_id == presentParentNode_id) {	//同级节点的移动
//			
//			if(presentNodeSerial > targetNodeSerial) {	//当前节点的序号大于目标节点的序号则执行
//				//两者之间的序号加1,当前节点的序号等于目标节点的序号加1
//				menuService.modifyBetweenSerialByParentId(site_id, presentParentNode_id, targetNodeSerial, presentNodeSerial, "add");
//				presentMenu.setOrder_num(targetNodeSerial+1);
//				presentMenu.setParent_id(presentParentNode_id);
//				menuService.update(presentMenu);
//			} else {
//				//目标节点以及两者之间的序号减1,当前节点的序号等于目标节点变更前的序号
//				menuService.modifyBetweenSerialByParentId(site_id, presentParentNode_id, presentNodeSerial, targetNodeSerial+1, "sub");
//				presentMenu.setOrder_num(targetNodeSerial);
//				presentMenu.setParent_id(presentParentNode_id);
//				menuService.update(presentMenu);
//			}
//		} else {	//不同级节点的移动
//			//大于目标节点序号的节点序号都加1,修改当前节点的父字段，当前节点的序号改为目标节点的序号,当前节点前一个父节点下的节点序号小于当前节点变更前序号的都减1
//			menuService.modifyBetweenSerialByParentId(site_id, presentParentNode_id, targetNodeSerial, -1, "add");
//			presentMenu.setParent_id(presentParentNode_id);
//			presentMenu.setOrder_num(targetNodeSerial+1);
//			menuService.update(presentMenu);
//			
//			menuService.modifyBetweenSerialByParentId(site_id, dragPresentParentNode_id, presentNodeSerial, -1, "sub");
//		}
//		
//		pw.print("{success:true, errors:{}}");
//		
//		return null;
//	}
	
	/**
	 * 使用正则表达式检查字符串，如果符合正则表达式，则返回true，否则返回false
	 * 
	 * @param str	被检查的字符串
	 * @param regex	正则表达式字符串
	 * 
	 * @return boolean
	 */
	private boolean checkStringUsingRegex(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
	
	@RequestMapping("copyMenu.do")
	public ModelAndView copyMenu(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		logger.info("--:");
		String name = req.getParameter("name");
		logger.info("name: " + name);
		String sourceSiteId =  req.getParameter("sourceSiteId");
		String sourceMenuId = req.getParameter("sourceMenuId");
		logger.info("sourceMenuId: " + sourceMenuId);
		String targetSiteId = req.getParameter("targetSiteId");
		logger.info("targetSiteId: " + targetSiteId);
		String targetParentId = req.getParameter("targetParentId");
		logger.info("targetParentId: " + targetParentId);
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		boolean bflag = false;
		int sMenuId = -1;
		int sSiteId = -1;
		try{
			sMenuId = Integer.parseInt(sourceMenuId);
			sSiteId = Integer.parseInt(sourceSiteId);
		}catch(Exception e){
			logger.error(e, e);
		}
		if(sSiteId>0 &&  sMenuId <= 0 ){
			bflag = menuService.copySite(sourceSiteId, targetSiteId);
		}else{
			bflag = menuService.copymenu(sourceMenuId, targetSiteId, targetParentId, name);
		}
		if(bflag){
			pw.print("{success:true , msg:'复制成功'}");
		}
		else{
			pw.print("{failure:true , msg:'复制失败'}");
		}
		return null;
	}
}
