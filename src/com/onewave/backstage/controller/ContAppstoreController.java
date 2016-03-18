package com.onewave.backstage.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.ApkTools;
import net.zhilink.tools.CloudDownloadTools;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.PinyinUtil;
import net.zhilink.tools.StringTool;
import net.zhilink.tools.XMLSender;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.AppDownloadUrl;
import com.onewave.backstage.model.BaseApp;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.model.ContAppstore;
import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.RecommendSync;
import com.onewave.backstage.model.RelaMenuAndCont;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.AppDownloadUrlService;
import com.onewave.backstage.service.ContAppstoreService;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoService;
import com.onewave.backstage.service.ImgService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.RecommendSyncService;
import com.onewave.backstage.service.RelaMenuAndContService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.backstage.service.UserGroupService;
import com.onewave.backstage.service.impl.ContProviderServiceImpl;
import com.onewave.backstage.util.BmUtil;
import com.zhilink.tv.model.CheckUpdate;
import com.zhilink.tv.util.HttpUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("contAppstoreController")
@RequestMapping("/apps/*")
public class ContAppstoreController extends MultiActionController {
   @Autowired
   @Qualifier("contService")
	private ContService contService;
   @Autowired
   @Qualifier("contVideoService")
	private ContVideoService contVideoService;
	@Autowired
	@Qualifier("imgService")
	private ImgService imgService;
	@Autowired
	@Qualifier("imgController")
	private ImgController imgController;
   @Autowired
   @Qualifier("contAppstoreService")
	private ContAppstoreService contAppstoreService;
   @Autowired
   @Qualifier("contProviderService")
	private ContProviderServiceImpl contProviderService;
   @Autowired
   @Qualifier("appDownloadUrlService")
	private AppDownloadUrlService appDownloadUrlService;
   @Autowired
   @Qualifier("menuService")
	private MenuService menuService;
   @Autowired
   @Qualifier("relaMenuAndContService")
	private RelaMenuAndContService relaMenuAndContService;
   @Autowired
   @Qualifier("statusDictService")
   private StatusDictService statusDictService;
   @Autowired
   @Qualifier("recommendSyncService")
   private RecommendSyncService recommendSyncService;
   @Autowired
   @Qualifier("roleService")
   private RoleService roleService;
	@Autowired
	@Qualifier("userGroupService")
	private UserGroupService userGroupService;

	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		int firstResult = Integer.parseInt(req.getParameter("start"));logger.info("--------------firstResult:" + firstResult);
		int maxResults = firstResult + Integer.parseInt(req.getParameter("limit"));logger.info("--------------maxResults:" + maxResults);

		String providerId = req.getParameter("provider_id");logger.info("--------------providerId:" + providerId);
		String contStatus = req.getParameter("c_status");logger.info("--------------contStatus:" + contStatus);
		String contName = req.getParameter("c_name");logger.info("--------------contName:" + contName);

		contStatus = contStatus == null ? "" : contStatus;
		contName = contName == null ? "" : contName.trim();

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			Operator operator = (Operator) req.getSession().getAttribute("user");
			if (operator == null){
				logger.info("用户没有登录！");
				return ;
			}
			String providerIds = roleService.queryIdsWithAuth(operator,"provider");
			if (StringUtils.isNotBlank(providerId) && !(""+InitManager.Defaut_Unselected_ID).equals(providerId.trim())){
				if ((","+providerIds+",").indexOf(","+providerId+",")>=0){
					providerIds = providerId;
				}
				else{
					providerIds = "-1";
					logger.info("用户【"+operator.getName()+"】没有供应商数据的权限，providerId=" + providerId);
				}
			}
			
			List<Cont> ssContList = contService.findAllSuperscripts();
			Map<String, String> ssContMap = new HashMap<String, String>();
			for(Cont operatorCont: ssContList){
				ssContMap.put(operatorCont.getId(), operatorCont.getName());
			}
			List<UserGroup> userGroupList = this.userGroupService.findAll();
			Map<String, String> userGroupMap = new HashMap<String, String>();
			for(UserGroup userGroup: userGroupList){
				userGroupMap.put(userGroup.getId(), userGroup.getName());
			}

			total = contService.countAllForApp(providerIds, "7", contStatus, contName);logger.info("contNum===================" + total);
			List<Cont> contList = contService.findAllForApp(firstResult, maxResults, "7", contStatus, contName, providerIds);

			String ids = "";
			for(Cont cont: contList){
				ids += cont.getId() + ",";
			}

			if(ids.length()>0) ids = ids.substring(0, ids.length()-1);

			List<ContVideo>  contVideoList = contVideoService.findByIds(ids);
			List<ContAppstore> contAppList = contAppstoreService.findAllByIds(ids);

			Map<String, ContVideo> contVideoMap = new HashMap<String, ContVideo>();
			Map<String, ContAppstore> contAppMap = new HashMap<String, ContAppstore>();

			for(ContVideo contVideo: contVideoList){
				contVideoMap.put(contVideo.getC_id(), contVideo);
			}
			for(ContAppstore contApp: contAppList){
				contAppMap.put(contApp.getC_id(), contApp);
			}

			JSONObject json = null;
			ContAppstore contApp = null;
			ContVideo contVideo = null;
			for (Cont cont : contList) {
				json = new JSONObject();
				parserCont(json, cont);
				json.put("usergroup_names_mac", BmUtil.getGroupNames(cont.getUsergroup_ids_mac(), userGroupMap));
				json.put("usergroup_names_zone", BmUtil.getGroupNames(cont.getUsergroup_ids_zone(), userGroupMap));
				json.put("usergroup_names_model", BmUtil.getGroupNames(cont.getUsergroup_ids_model(), userGroupMap));
				json.put("usergroup_names_channel", BmUtil.getGroupNames(cont.getUsergroup_ids_channel(), userGroupMap));
				json.put("usergroup_names_mac2", BmUtil.getGroupNames(cont.getUsergroup_ids_mac2(), userGroupMap));
				json.put("usergroup_names_zone2", BmUtil.getGroupNames(cont.getUsergroup_ids_zone2(), userGroupMap));
				json.put("usergroup_names_model2", BmUtil.getGroupNames(cont.getUsergroup_ids_model2(), userGroupMap));
				json.put("usergroup_names_channel2", BmUtil.getGroupNames(cont.getUsergroup_ids_channel2(), userGroupMap));

				contVideo = contVideoMap.get(cont.getId());
				parserContVideo(json, contVideo);
				json.put("cont_superscript", "");
				if (contVideo != null) {
					if (StringUtils.isNotBlank(contVideo.getSuperscript_id()))
						json.put("cont_superscript", contVideo.getSuperscript_id()+"|"+ssContMap.get(contVideo.getSuperscript_id()));
				}

				contApp = contAppMap.get(cont.getId());
				if (contApp != null) {
					parserContApp(json, contApp);
				}

				rootArr.add(json);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JSONObject root = new JSONObject();
			root.put("total", total);
			root.put("data", rootArr);
			logger.info("json===================" + root.toString());
			resp.getWriter().print(root);
		}
	}
	private void parserContVideo(JSONObject json, ContVideo contVideo) {
		if (contVideo != null) {
			json.put("cv_alias", StringTool.null2Empty(contVideo.getAlias()));
			json.put("superscript_id", StringTool.null2Empty(contVideo.getSuperscript_id()));
			json.put("cv_play_url", StringTool.null2Empty(contVideo.getPlay_url()));
			
			json.put("cont_app_name_f", StringTool.null2Empty(contVideo.getName()));
			json.put("cont_app_alias", StringTool.null2Empty(contVideo.getAlias()));
			json.put("cont_app_discription", StringTool.null2Empty(contVideo.getDescription()));
			json.put("superscript_id", StringTool.null2Empty(contVideo.getSuperscript_id()));
			String superscript_name = "";
			if (null != (contService
					.findById(contVideo.getSuperscript_id()))) {
				try {
					superscript_name = contService.findById(
							contVideo.getSuperscript_id()).getName();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				superscript_name = "";
			}
			json.put("superscript_name", superscript_name);
		}
	}
	private void parserCont(JSONObject json, Cont cont) {
		if (cont != null) {
			json.put("c_id", cont.getId());
			json.put("c_name", StringTool.null2Empty(cont.getName()));
			json.put("c_type", cont.getType());
			json.put("pinyin", StringTool.null2Empty(cont.getPinyin()));
			json.put("c_status", cont.getStatus());
			json.put("is_locked", StringTool.null2Empty(cont.getLocked()));
			json.put("provider_id", StringTool.null2Empty(cont.getProvider_id()));
			json.put("c_description", StringTool.null2Empty(cont.getDescription()));
			json.put("active_time", StringTool.null2Empty(BmUtil.formatDate(cont.getActive_time())));
			json.put("deactive_time", StringTool.null2Empty(BmUtil.formatDate(cont.getDeactive_time())));
			json.put("create_time", StringTool.null2Empty(BmUtil.formatDate(cont.getCreate_time())));
			json.put("modify_time", StringTool.null2Empty(BmUtil.formatDate(cont.getModify_time())));
			json.put("usergroup_ids_mac", StringTool.null2Empty(cont.getUsergroup_ids_mac()));
			json.put("usergroup_ids_model", StringTool.null2Empty(cont.getUsergroup_ids_model()));
			json.put("usergroup_ids_zone", StringTool.null2Empty(cont.getUsergroup_ids_zone()));
			json.put("usergroup_ids_channel", StringTool.null2Empty(cont.getUsergroup_ids_channel()));
			json.put("usergroup_ids_mac2", StringTool.null2Empty(cont.getUsergroup_ids_mac2()));
			json.put("usergroup_ids_model2", StringTool.null2Empty(cont.getUsergroup_ids_model2()));
			json.put("usergroup_ids_zone2", StringTool.null2Empty(cont.getUsergroup_ids_zone2()));
			json.put("usergroup_ids_channel2", StringTool.null2Empty(cont.getUsergroup_ids_channel2()));
			json.put("c_video_seg_time", StringTool.null2Empty(cont.getVideo_seg_time()));
		}
	}
	private void parserContApp(JSONObject json, ContAppstore contApp) {
		if (contApp != null){
			json.put("ca_capacity", StringTool.null2Empty(contApp.getCapacity()));
			json.put("ca_version", StringTool.null2Empty(contApp.getVersion()));
			json.put("ca_version_code", StringTool.null2Empty(contApp.getVersion_code()));
			json.put("ca_staff", StringTool.null2Empty(contApp.getStaff()));
			json.put("ca_tags", StringTool.null2Empty(contApp.getTags()));
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			Date beginDate = cal.getTime();
			cal.add(Calendar.YEAR, 50);
			Date endDate = cal.getTime();
			Date addtiem = contApp.getAdd_time() == null ? beginDate : contApp.getAdd_time();
			String addtiemS = "0000-00-00 00:00:00";
			if (addtiem != null) {
				addtiemS = BmUtil.formatDate(addtiem);
			}
			json.put("ca_time", StringTool.null2Empty(addtiemS));

			json.put("ca_package_name", StringTool.null2Empty(contApp.getPackage_name()));
			json.put("ca_md5", contApp.getMd5sum());
			json.put("ca_download_url", StringTool.null2Empty(contApp.getDownload_url()));

			String download_url = StringTool.null2Empty(contApp.getDownload_url());
			if (StringTool.isEmpty(download_url))
				json.put("ca_download_url_show", "");
			else if (download_url.startsWith("http://")) {
				json.put("ca_download_url_show", download_url);
			} else {
				json.put("ca_download_url_show", InitManager.combineRootHttpPath(download_url));
			}
		}
	}
	@RequestMapping("save.do")
	public void saveHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "添加失败";
		try {
			req = BmUtil.resolveMultipart(req);
			boolean has_base_info = false;
			try {
				logger.info("--:cv_base_info=" + req.getParameter("cv_base_info"));
				has_base_info = Integer.parseInt(req.getParameter("cv_base_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			Cont cont = new Cont();
			if(has_base_info) {
				updateCont(cont, req);
				String c_id = contService.saveAndReturnId(cont);
				cont.setId(c_id);
				if (c_id != null && !"".equals(c_id.trim()) && -1 < Integer.parseInt(c_id)) {
					ContVideo contVideo = new ContVideo();
					updateContVideo(contVideo, req);
					contVideo.setC_id(c_id);
					issuc = contVideoService.save(contVideo);

					if(issuc) {
						ContAppstore contApp = new ContAppstore();
						updateContApp(contApp, req);
						contApp.setC_id(c_id);

						issuc = contAppstoreService.save(contApp);
						if(!issuc) {
							contService.delete(cont);
							contVideoService.delete(contVideo);
						}
					} else {
						contService.delete(cont);
					}
				}

				if(!issuc) {
					msg = "添加基本信息失败";
					return;
				}
			}

			boolean has_img_info = false;
			try {
				logger.info("--:cv_img_info=" + req.getParameter("cv_img_info"));
				has_img_info = Integer.parseInt(req.getParameter("cv_img_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if(issuc && has_img_info) {
				issuc = imgController.saveOrUpdateImg(req, cont.getId(), cont.getProvider_id(), "1");
				if(!issuc) {
					msg = "添加图片信息失败";
					return;
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
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "修改失败";
		try {
			req = BmUtil.resolveMultipart(req);
			boolean has_base_info = false;
			try {
				logger.info("--:cs_base_info=" + req.getParameter("cv_base_info"));
				has_base_info = Integer.parseInt(req.getParameter("cv_base_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			String c_id = req.getParameter("c_id");

			Cont cont = contService.findById(c_id);
			if(cont != null && has_base_info) {
				updateCont(cont, req);
				issuc = contService.update(cont);

				ContVideo contVideo = contVideoService.findById(c_id);
				if(contVideo != null) {
					updateContVideo(contVideo, req);
					issuc = contVideoService.update(contVideo);
					
					if(!issuc) {
						msg = "修基本信息失败";
						return;
					}
				}

				ContAppstore contApp = contAppstoreService.findById(c_id);
				if(contApp != null) {
					updateContApp(contApp, req);
					issuc = contAppstoreService.update(contApp);

					if(!issuc) {
						msg = "修基本信息失败";
						return;
					}
				}
				else{
					contApp = new ContAppstore();
					contApp.setC_id(c_id);
					updateContApp(contApp, req);
					issuc = contAppstoreService.save(contApp);
					if(!issuc) {
						msg = "修改基本信息失败";
						return;
					}
				}
			}

			boolean has_img_info = false;
			try {
				logger.info("--:cs_base_info=" + req.getParameter("cv_img_info"));
				has_img_info = Integer.parseInt(req.getParameter("cv_img_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if(has_img_info && cont != null) {
				issuc = imgController.saveOrUpdateImg(req, cont.getId(), cont.getProvider_id(), "1");
				if(!issuc) {
					msg = "修改图片信息失败";
					return;
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

	@RequestMapping("del.do")
	public void delHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "删除失败";
		try {
			String c_id = req.getParameter("c_id");logger.info("-----------id:" + c_id);
			if(c_id != null) {
				List<Menu> menuList = menuService.findByShortCutId(c_id);
				List<RelaMenuAndCont> relaMenuAndContList = relaMenuAndContService.findby_id(c_id);
				String info="";
				String menu="";
				String relaMenuAndCont="";
				if(menuList!=null && menuList.size()>0){
					for(int i=0;i<menuList.size();i++){
						menu+="<"+menuList.get(i).getId()+">";
					}

					info += "作为快捷方式使用不能删除，请先删除快捷方式！栏目Id《" + menu + "》";
				}

				if(relaMenuAndContList!=null && relaMenuAndContList.size()>0){
					for(int i=0;i<relaMenuAndContList.size();i++){
						relaMenuAndCont+="<栏目ID"+relaMenuAndContList.get(i).getMenu_id()+"资产ID"+relaMenuAndContList.get(i).getC_id()+">";
					}

					info += "作为绑定资产使用不能删除，请先解绑资产！绑定资产《" + relaMenuAndCont + "》";
				}

				if(!"".equals(info)){
					msg += " " + info;
					issuc = false;
				} else {
					issuc = contService.delete(c_id);
					issuc = contVideoService.delete(c_id);
					contAppstoreService.delete(c_id);//不理会删除条数
					imgService.deleteAll(c_id, "");//用途类型: 0 栏目, 1 内容, 2应用截图/商品缩略图
					appDownloadUrlService.deletebycid(c_id);
					contAppstoreService.delete(c_id);
				}
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

	private void updateCont(Cont cont, HttpServletRequest req) {
		logger.info("--:here");

		try {
			cont.setName(req.getParameter("c_name"));
			cont.setPinyin(PinyinUtil.getHeadStringWithoutAnySymbol(req.getParameter("c_name")));
			cont.setStatus(Integer.parseInt(req.getParameter("c_status")));
			cont.setDescription(req.getParameter("c_description"));
			cont.setType(7);
			cont.setProvider_id(req.getParameter("provider_id"));
			cont.setActive_time(BmUtil.parseDate(req.getParameter("active_time")));
			cont.setDeactive_time(BmUtil.parseDate(req.getParameter("deactive_time")));
			cont.setLocked(req.getParameter("is_locked"));

			cont.setUsergroup_ids_mac(req.getParameter("usergroup_ids_mac"));
			cont.setUsergroup_ids_zone(req.getParameter("usergroup_ids_zone"));
			cont.setUsergroup_ids_model(req.getParameter("usergroup_ids_model"));
			cont.setUsergroup_ids_channel(req.getParameter("usergroup_ids_channel"));
			cont.setUsergroup_ids_mac2(req.getParameter("usergroup_ids_mac2"));
			cont.setUsergroup_ids_zone2(req.getParameter("usergroup_ids_zone2"));
			cont.setUsergroup_ids_model2(req.getParameter("usergroup_ids_model2"));
			cont.setUsergroup_ids_channel2(req.getParameter("usergroup_ids_channel2"));

			String c_video_seg_time = req.getParameter("c_video_seg_time");
			if (!StringUtils.isBlank(c_video_seg_time)){
				c_video_seg_time = c_video_seg_time.replace("\r\n", "\n");
				String[] segs = c_video_seg_time.split("\n\n");
			}
			cont.setVideo_seg_time(c_video_seg_time);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void updateContVideo(ContVideo contVideo, HttpServletRequest req) {
		logger.info("--:here");

		try {
			contVideo.setName(req.getParameter("c_name"));
			contVideo.setAlias(req.getParameter("cv_alias"));
			contVideo.setDescription(req.getParameter("c_description"));
			contVideo.setProvider_id(req.getParameter("provider_id"));
			contVideo.setPackage_name("");
			contVideo.setSuperscript_id(req.getParameter("superscript_id"));
			contVideo.setVol_update_time(new Date());
			contVideo.setPlay_url(req.getParameter("cv_play_url"));

		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	private void updateContApp(ContAppstore contApp, HttpServletRequest req) {
		logger.info("--:here");

		try {
			contApp.setProvider_id(req.getParameter("provider_id"));
			contApp.setStaff(req.getParameter("ca_staff"));
			contApp.setTags(req.getParameter("ca_tags"));

			/*页面并没有传这些参数
			contApp.setPackage_name(req.getParameter("ca_package_name"));
			contApp.setCapacity(req.getParameter("ca_capacity"));
			contApp.setVersion(req.getParameter("ca_version"));
			contApp.setVersion_code(req.getParameter("ca_version_code"));
			contApp.setMd5sum(req.getParameter("ca_md5"));
			contApp.setDownload_url(req.getParameter("ca_download_url"));*/
			
			String addTimeStr = req.getParameter("ca_time");
			if (StringUtils.isNotBlank(addTimeStr)){
				addTimeStr = addTimeStr.replaceAll("T", " ");
				Date addTime = BmUtil.parseDate(addTimeStr);
				contApp.setAdd_time(addTime);
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@RequestMapping("import.do")
	public ModelAndView importHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		
		JSONObject result = new JSONObject();
		JSONArray items = new JSONArray();
		JSONObject item = new JSONObject();
		
		InputStream is = new BufferedInputStream(req.getInputStream());
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = is.read(b)) != -1) {
			out.append(new String(b, 0, len, "UTF-8"));
		}
		String reqStr = out.toString(); logger.info(reqStr);
		String provider_id = req.getParameter("provider_id");
		if (StringUtils.isBlank(provider_id)) {
			provider_id = "90"; // 播亦乐1
		}
		if (!StringUtils.isBlank(reqStr)) {
			BaseApp[] apps = null;
			List<BaseApp> applist = null;
			if (reqStr.startsWith("{")) {
				apps = new BaseApp[1];
				apps[0] = (BaseApp) JSONObject.toBean(JSONObject.fromObject(reqStr), BaseApp.class);
			} else {
				apps = (BaseApp[]) JSONArray.toArray(JSONArray.fromObject(reqStr), BaseApp.class);
			}
			if (apps != null) {
				applist = Arrays.asList(apps);
			}
			if (applist != null) {
				
				for (BaseApp app : applist) {
					item = importAppSaveOrUpdate(provider_id, app);
					items.add(item);
				}
			}
			result.put("success", true);
			result.put("info", "处理成功");
			result.put("items", items);
		} else {
			result.put("success", false);
			result.put("info", "请求报文格式非法");
		}
		logger.info("result=" + result.toString());
		pw.print(result);

		return null;
	}
	
	private JSONObject importAppSaveOrUpdate(String provider_id, BaseApp app){
		Cont cont = contService.findAppByPackageName(app.getPackage_name(), provider_id);
		if(cont!=null){
			return importAppUpdate(provider_id, app,cont);
		}else{
			return importAppSave(provider_id, app);
		}
	}
	
	private JSONObject importAppUpdate(String provider_id, BaseApp app,Cont cont){
		JSONObject json = new JSONObject();
		boolean succ = true;
		String app_img_id = "";
		Date active_time = null;
		Date deactive_time = null;
		if(!appValidate(app)){
			json.put("success", succ);
			json.put("info", "数据异常");
			return json;
		}
		try {
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+1);
			active_time = curr.getTime();
			curr.set(Calendar.YEAR, curr.get(Calendar.YEAR)+50);
			deactive_time = new SimpleDateFormat("yyyy-MM-dd").parse("2050-12-31");
		} catch (ParseException e1) {
			logger.error(e1);
			succ = false;
			json.put("success", succ);
			json.put("info", "数据异常");
			return json;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		if (cont != null) {
			cont.setName(app.getApp_name());
			cont.setPinyin(PinyinUtil
					.getHeadStringWithoutAnySymbol(app.getApp_name()));
			cont.setStatus(11);
			cont.setDescription(app.getDescrption());
			cont.setType(7);
			cont.setProvider_id(provider_id);
			cont.setActive_time(active_time);
			cont.setDeactive_time(deactive_time);

			contService.update(cont);
		}
		if (app.getIconUrlsMap() != null
				&& app.getIconUrlsMap().keySet().size() > 0) {
			List<Img> imgList = imgService.findAll(cont.getId(), "1");
			if (imgList != null && imgList.size() > 0) {
			String path = (String) app.getIconUrlsMap().keySet()
					.toArray()[0];
			String source = (String) app.getIconUrlsMap().values()
					.toArray()[0];
			Img img = new Img();
			img.setPlatgroup_id("1");
			img.setProvider_id(provider_id);
			img.setTarget_id(cont.getId());
			img.setUse_type("1");
			img.setUrl("");
			img.setUrl_little(path);
			img.setUrl_icon(path);
			img.setUrl_icon_source(source);
			// ************************************************************
			img.setUrl_4_squares("");
			img.setUrl_6_squares("");
			// ************************************************************
			img.setIs_url_used("2");
			img.setIntro("");
			img.setActive_time(active_time);
			img.setDeactive_time(deactive_time);
			imgService.update(img);
			}
		}

		ContVideo contVideo = contVideoService.findById(cont.getId());
		if (contVideo != null) {
			contVideo.setName(app.getApp_name());
			contVideo.setAlias(app.getApp_alias());
			contVideo.setDescription(app.getDescrption());
			contVideo.setProvider_id(app.getProvider_id());
			contVideo.setPackage_name(app.getPackage_name());
			contVideo.setSuperscript_id("");
			try {
				contVideo.setVol_update_time(sdf.parse(app.getModifyTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			contVideoService.update(contVideo);
		}

		ContAppstore contAppstore = contAppstoreService.findById(cont.getId());
		if (contAppstore != null) {
			contAppstore.setStaff(app.getStaff());
			contAppstore.setAdd_time(new Date());
			contAppstore.setApp_name(app.getApp_name());
			try {
				contAppstore.setVersion(app.getVersion());
				contAppstore.setCapacity(app.getCapacity());
				contAppstore.setVersion_code("");
				contAppstore.setPackage_name(app.getPackage_name());
				contAppstore.setMd5sum("");
				contAppstore.setDownload_url((String) app
						.getDownloadUrlMap().keySet().toArray()[0]);
				contAppstore.updateApkInfo();
			} catch (Exception e) {
				e.printStackTrace();
			}
			contAppstore.setProvider_id(provider_id);
			contAppstoreService.update(contAppstore);
		}
		// 更新截图
		//删除截图
		imgService.deleteAll(cont.getId(),"2");
		//重新插入
		if (app.getScreenShotsMap() != null
				&& app.getScreenShotsMap().keySet().size() > 0) {
			Iterator<String> i = app.getScreenShotsMap().keySet()
					.iterator();
			String path, source;
			int c =1;
			while (i.hasNext()) {
				if(c > 5){ //最多5张截图
					break ;
				}
				path = (String) i.next();
				source = app.getScreenShotsMap().get(path);
				Img img = new Img();
				img.setPlatgroup_id("1");
				img.setProvider_id(provider_id);
				img.setTarget_id(cont.getId());
				img.setUse_type("2");// 截图
				img.setUrl(path);
				img.setUrl_source(source);
				img.setActive_time(active_time);
				img.setDeactive_time(deactive_time);
				app_img_id += "," + imgService.saveAndReturnId(img);
				c++;
			}
		}
		json.put("app_img_ids", app_img_id);
		return json;
	}
	
	private JSONObject importAppSave(String provider_id, BaseApp app){
		JSONObject json = new JSONObject();
		boolean succ = true;
		String app_img_id = "";
		Date active_time = null;
		Date deactive_time = null;
		if(!appValidate(app)){
			json.put("success", succ);
			json.put("info", "数据异常");
			return json;
		}
		try {
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+1);
			active_time = curr.getTime();
			curr.set(Calendar.YEAR, curr.get(Calendar.YEAR)+50);
			deactive_time = new SimpleDateFormat("yyyy-MM-dd").parse("2050-12-31");
		} catch (ParseException e1) {
			logger.error(e1);
			succ = false;
			json.put("success", succ);
			json.put("info", "数据异常");
			return json;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Cont cont = new Cont();
			ContVideo contVideo = new ContVideo();

			cont.setName(app.getApp_name());
			cont.setPinyin(PinyinUtil.getHeadStringWithoutAnySymbol(app
					.getApp_name()));
			cont.setStatus(11);
			cont.setDescription(app.getDescrption());
			cont.setType(7);
			cont.setProvider_id(provider_id);
			cont.setActive_time(active_time);
			cont.setDeactive_time(deactive_time);

			String returnId = contService.saveAndReturnId(cont);
			json.put("cont_id", returnId);
			if (returnId != null && !"".equals(returnId)
					&& -1 < Integer.parseInt(returnId)) {

				if (app.getIconUrlsMap() != null
						&& app.getIconUrlsMap().keySet().size() > 0) {
					String path = (String) app.getIconUrlsMap().keySet()
							.toArray()[0];
					String source = (String) app.getIconUrlsMap().values()
							.toArray()[0];
					Img img = new Img();
					img.setPlatgroup_id("1");
					img.setProvider_id(provider_id);
					img.setTarget_id(returnId);
					img.setUse_type("1");
					img.setUrl("");
					img.setUrl_little(path);
					img.setUrl_icon(path);
					img.setUrl_icon_source(source);
					// ************************************************************
					img.setUrl_4_squares("");
					img.setUrl_6_squares("");
					// ************************************************************
					img.setIs_url_used("2");
					img.setIntro("");
					img.setActive_time(active_time);
					img.setDeactive_time(deactive_time);
					app_img_id = imgService.saveAndReturnId(img);
				}

				contVideo = new ContVideo();

				contVideo.setName(app.getApp_name());
				contVideo.setAlias(app.getApp_alias());
				contVideo.setDescription(app.getDescrption());
				contVideo.setC_id(returnId);
				contVideo.setProvider_id(provider_id);
				contVideo.setPackage_name(app.getPackage_name());
				contVideo.setSuperscript_id(null);
				try {
					contVideo.setVol_update_time(sdf.parse(app.getModifyTime()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				succ = contVideoService.save(contVideo);
				json.put("cont_video", succ);

				ContAppstore contAppstore = new ContAppstore();

				contAppstore.setC_id(returnId);
				contAppstore.setStaff(app.getStaff());
				contAppstore.setAdd_time(new Date());
				contAppstore.setApp_name(app.getApp_name());
				try {
					contAppstore.setVersion(app.getVersion());
					contAppstore.setCapacity(app.getCapacity());
					contAppstore.setVersion_code("");
					contAppstore.setPackage_name(app.getPackage_name());
					contAppstore.setMd5sum("");
					contAppstore.setDownload_url((String) app
							.getDownloadUrlMap().keySet().toArray()[0]);
					contAppstore.updateApkInfo();
				} catch (Exception e) {
					e.printStackTrace();
				}
				contAppstore.setProvider_id(provider_id);
				succ = contAppstoreService.save(contAppstore);
				json.put("cont_appstore", succ);
				// 保存截图
				if (app.getScreenShotsMap() != null
						&& app.getScreenShotsMap().keySet().size() > 0) {
					Iterator<String> i = app.getScreenShotsMap().keySet()
							.iterator();
					String path, source;
					int c =1;
					while (i.hasNext()) {
						if(c > 5){ //最多5张截图
							break ;
						}
						path = (String) i.next();
						source = app.getScreenShotsMap().get(path);
						Img img = new Img();
						img.setPlatgroup_id("1");
						img.setProvider_id(provider_id);
						img.setTarget_id(returnId);
						img.setUse_type("2");// 截图
						img.setUrl(path);
						img.setUrl_source(source);
						img.setActive_time(active_time);
						img.setDeactive_time(deactive_time);
						app_img_id += "," + imgService.saveAndReturnId(img);
						c++;
					}
				}
				json.put("app_img_ids", app_img_id);
				logger.info("save success ,app name " + app.getApp_name()
						+ ", cont id=" + returnId + " app_img_id= "
						+ app_img_id);
			} else {
				logger.error("save faild ,app name " + app.getApp_name());
				succ = false;
			}
		} catch (Exception e) {
			logger.error("save faild ,app name " + app.getApp_name()+"  " + e);
			succ= false;
		}
		json.put("succ", succ);
		return json;
	}

	private boolean appValidate(BaseApp app) {
		try{
			return StringUtils.isBlank(app.getApp_name()) || StringUtils.isBlank(app.getPackage_name()) 
				|| app.getDownloadUrlMap().keySet().size()> 0;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save_update_cont_app.do")
	public ModelAndView saveAndUpdateContAppstoreHandler(
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String result = "";
		try {
		logger.info("--:");

			String id = req.getParameter("id"); logger.info("---------id:" + id);
			String appName = req.getParameter("appName"); logger.info("---------appName:" + appName);
			String appAlias = req.getParameter("appAlias"); logger.info("---------appAlias:" + appAlias);
			int appStatus = Integer.parseInt(req.getParameter("appStatus")); logger.info("---------appStatus:" + appStatus);
			String appProvider = req.getParameter("appProvider"); logger.info("---------appProvider:" + appProvider);
			String activeTimeStr = req.getParameter("activeTime");
			activeTimeStr = activeTimeStr.replaceAll("T", " "); logger.info("---------activeTime:" + activeTimeStr);
			Date activeTime = BmUtil.parseDate(activeTimeStr);
			String deactiveTimeStr = req.getParameter("deactiveTime");
			deactiveTimeStr = deactiveTimeStr.replaceAll("T", " "); logger.info("---------deactiveTime:" + deactiveTimeStr);
			Date deactiveTime = BmUtil.parseDate(deactiveTimeStr);
			String appCapacity = req.getParameter("appCapacity"); logger.info("---------appCapacity:" + appCapacity);
			String appVersion = req.getParameter("appVersion");
			String appVersionCode = req.getParameter("appVersionCode"); logger.info("---------appVersion:" + appVersion);
			String tags = req.getParameter("tags"); logger.info("---------tags:" + tags);
			String superscript_id = req.getParameter("superscript_id"); logger.info("---------superscript_id:" + superscript_id);

			String appStaff = req.getParameter("appStaff"); logger.info("---------appStaff:" + appStaff);
			String img_locked = req.getParameter("img_locked"); logger.info("---------img_locked:" + img_locked);
			String base_locked = req.getParameter("base_locked"); logger.info("---------base_locked:" + base_locked);
			String appPackageName = req.getParameter("appPackageName"); logger.info("---------appPackageName:" + appPackageName);
			String appMd5 = req.getParameter("appMd5"); logger.info("---------appMd5:" + appMd5);
			String appDownloadUrl = req.getParameter("appDownloadUrl"); logger.info("---------appDownloadUrl:" + appDownloadUrl);
			String appDiscription = req.getParameter("appDiscription"); logger.info("---------appDiscription:" + appDiscription);
			String addTimeStr = req.getParameter("addTime");
			addTimeStr = addTimeStr.replaceAll("T", " "); logger.info("---------addTime:" + addTimeStr);
			Date addTime = BmUtil.parseDate(addTimeStr);
			String app_img_id = req.getParameter("app_img_id"); logger.info("---------app_img_id:" + app_img_id);
			String plat_groupId = req.getParameter("plat_groupId"); logger.info("---------plat_groupId:" + plat_groupId);
			String app_img_intro_form = req.getParameter("app_img_intro_form"); logger.info("---------app_img_intro_form:" + app_img_intro_form);
			String app_img_url = req.getParameter("app_img_url"); logger.info("---------app_img_url:" + app_img_url);
			String app_img_url_little = req.getParameter("app_img_url_little"); logger.info("---------app_img_url_little:" + app_img_url_little);
			String app_img_url_icon = req.getParameter("app_img_url_icon"); logger.info("---------app_img_url_icon:" + app_img_url_icon);
			// *****************************************************************************************************************************
			String app_img_url_4_squares = req.getParameter("app_img_url_4_squares"); logger.info("---------app_img_url_icon:" + app_img_url_4_squares);
			String app_img_url_6_squares = req.getParameter("app_img_url_6_squares"); logger.info("---------app_img_url_icon:" + app_img_url_6_squares);
			// *****************************************************************************************************************************

			String app_img_active_timeStr = req.getParameter("app_img_active_time");
			app_img_active_timeStr = app_img_active_timeStr.replaceAll("T", " "); logger.info("---------app_img_active_time:" + app_img_active_timeStr);
			Date app_img_active_time = BmUtil.parseDate(app_img_active_timeStr);
			String app_img_deactive_timeStr = req.getParameter("app_img_deactive_time");
			app_img_deactive_timeStr = app_img_deactive_timeStr.replaceAll("T", " "); logger.info("---------app_img_deactive_time:" + app_img_deactive_timeStr);
			Date app_img_deactive_time = BmUtil.parseDate(app_img_deactive_timeStr);
			String app_img_isurlused = req.getParameter("app_img_isurlused");
			app_img_isurlused = app_img_isurlused == null ? "0" : app_img_isurlused; logger.info("---------app_img_isurlused:" + app_img_isurlused);

			Cont cont;
			ContVideo contVideo;
			if ("-1".equals(id)) {
				cont = new Cont();

				cont.setName(appName);
				cont.setPinyin(PinyinUtil.getHeadStringWithoutAnySymbol(appName));
				cont.setStatus(appStatus);
				cont.setDescription(appDiscription);
				cont.setType(7);
				cont.setProvider_id(appProvider);
				cont.setActive_time(activeTime);
				cont.setDeactive_time(deactiveTime);
				cont.setLocked(base_locked);

				String returnId = contService.saveAndReturnId(cont);
				if (returnId != null && !"".equals(returnId) && -1 < Integer.parseInt(returnId)) {
					if ((app_img_url != null && !"".equals(app_img_url.trim()))
							|| (app_img_url_little != null && !""
									.equals(app_img_url_little.trim()))
							|| (app_img_url_icon != null && !""
									.equals(app_img_url_icon.trim()))
							|| (app_img_url_4_squares != null && !""
									.equals(app_img_url_4_squares.trim()))
							|| (app_img_url_6_squares != null && !""
									.equals(app_img_url_6_squares.trim()))) {
						Img img = new Img();
						img.setPlatgroup_id(plat_groupId);
						img.setProvider_id(appProvider);
						img.setTarget_id(returnId);
						img.setUse_type("1");
						img.setUrl(app_img_url);
						img.setUrl_little(app_img_url_little);
						img.setUrl_icon(app_img_url_icon);
						// ************************************************************
						img.setUrl_4_squares(app_img_url_4_squares);
						img.setUrl_6_squares(app_img_url_6_squares);
						// ************************************************************
						img.setIs_url_used(app_img_isurlused);
						img.setIntro(app_img_intro_form);
						img.setActive_time(app_img_active_time);
						img.setDeactive_time(app_img_deactive_time);
						img.setLocked(img_locked);
						app_img_id = imgService.saveAndReturnId(img);
					}

					contVideo = new ContVideo();

					contVideo.setName(appName);
					contVideo.setAlias(appAlias);
					contVideo.setDescription(appDiscription);
					contVideo.setC_id(returnId);
					contVideo.setProvider_id(appProvider);
					contVideo.setPackage_name(appPackageName);
					contVideo.setSuperscript_id(superscript_id);
					contVideo.setVol_update_time(new Date());
					contVideoService.save(contVideo);

					ContAppstore contAppstore = new ContAppstore();

					contAppstore.setC_id(returnId);
					contAppstore.setStaff(appStaff);
					//contAppstore.setPackage_name(appPackageName);//上传apk时再更新到库
					//contAppstore.setVersion(appVersion);
					//contAppstore.setVersion_code(appVersionCode);
					//contAppstore.setCapacity(appCapacity);
					//contAppstore.setMd5sum(appMd5);
					contAppstore.setAdd_time(addTime);
					contAppstore.setDownload_url(appDownloadUrl);
					contAppstore.setApp_name(appName);
					contAppstore.setProvider_id(appProvider);
					contAppstore.setTags(tags);
					contAppstoreService.save(contAppstore);

					result = "{success:true, info:'保存成功！', id:" + returnId + ", img_id:" + app_img_id + "}";
				} else {
					result = "{success:false, info:'保存失败！'}";
				}
			} else {
				cont = contService.findById(id);
				if (cont != null) {
					cont.setName(appName);
					cont.setPinyin(PinyinUtil
							.getHeadStringWithoutAnySymbol(appName));
					cont.setStatus(appStatus);
					cont.setDescription(appDiscription);
					cont.setType(7);
					cont.setProvider_id(appProvider);
					cont.setActive_time(activeTime);
					cont.setDeactive_time(deactiveTime);
					cont.setLocked(base_locked);

					contService.update(cont);
				}

				if ((app_img_url != null && !"".equals(app_img_url.trim()))
						|| (app_img_url_little != null && !""
								.equals(app_img_url_little.trim()))
						|| (app_img_url_icon != null && !""
								.equals(app_img_url_icon.trim()))
						|| (app_img_url_4_squares != null && !""
								.equals(app_img_url_4_squares.trim()))
						|| (app_img_url_6_squares != null && !""
								.equals(app_img_url_6_squares.trim()))) {
					if ("-1".equals(app_img_id.trim())) {
						Img img = new Img();
						img.setPlatgroup_id(plat_groupId);
						img.setProvider_id(appProvider);
						img.setTarget_id(id);
						img.setUse_type("1");
						img.setUrl(app_img_url);
						img.setUrl_little(app_img_url_little);
						img.setUrl_icon(app_img_url_icon);
						img.setUrl_4_squares(app_img_url_4_squares);
						img.setUrl_6_squares(app_img_url_6_squares);
						img.setIs_url_used(app_img_isurlused);
						img.setIntro(app_img_intro_form);
						img.setActive_time(app_img_active_time);
						img.setDeactive_time(app_img_deactive_time);
						img.setLocked(img_locked);
						app_img_id = imgService.saveAndReturnId(img);
					} else {
						List<Img> imgList = imgService.findAll(id, "1");
						if (imgList != null && imgList.size() > 0) {
							Img img = imgList.get(0);
							img.setPlatgroup_id(plat_groupId);
							img.setProvider_id(appProvider);
							img.setTarget_id(id);
							img.setUse_type("1");
							img.setUrl(app_img_url);
							img.setUrl_little(app_img_url_little);
							img.setUrl_icon(app_img_url_icon);
							img.setUrl_4_squares(app_img_url_4_squares);
							img.setUrl_6_squares(app_img_url_6_squares);
							img.setIs_url_used(app_img_isurlused);
							img.setIntro(app_img_intro_form);
							img.setActive_time(app_img_active_time);
							img.setDeactive_time(app_img_deactive_time);
							img.setLocked(img_locked);
							imgService.update(img);
						}
					}
				}

				contVideo = contVideoService.findById(id);
				if (contVideo != null) {
					contVideo.setName(appName);
					contVideo.setAlias(appAlias);
					contVideo.setDescription(appDiscription);
					contVideo.setProvider_id(appProvider);
					contVideo.setPackage_name(appPackageName);
					contVideo.setSuperscript_id(superscript_id);
					contVideo.setVol_update_time(new Date());

					contVideoService.update(contVideo);
				}

				ContAppstore contAppstore = contAppstoreService.findById(id);
				if (contAppstore != null) {
					contAppstore.setStaff(appStaff);
					contAppstore.setVersion(appVersion);
					contAppstore.setVersion_code(appVersionCode);
					contAppstore.setCapacity(appCapacity);
					contAppstore.setAdd_time(addTime);
					contAppstore.setDownload_url(appDownloadUrl);
					contAppstore.setPackage_name(appPackageName);
					contAppstore.setApp_name(appName);
					contAppstore.setMd5sum(appMd5);
					contAppstore.setProvider_id(appProvider);
					contAppstore.setTags(tags);
					contAppstoreService.update(contAppstore);

					result = "{success:true, info:'保存成功！', id:" + id + ", img_id:" + app_img_id + "}";
				} else {
					result = "{success:false, info:'保存失败！'}";
				}
			}

		} catch (Exception E) {
			E.printStackTrace();// ignore
		}
		logger.info("---------result=" + result);
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		pw.print(result);

		return null;
	}

	@RequestMapping("query_app_snapshotimg.do")
	public ModelAndView queryContAppSnapshotimgHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		String contid = req.getParameter("id"); logger.info("contid: " + contid);
		if (contid == null) {
			contid = "";
		}
		JSONObject json = new JSONObject();
		try {
			JSONArray jsonArray = new JSONArray();
			JSONObject ccJson = new JSONObject();
			List<Img> imgList = imgService.findAll(contid, "2");
			for (Img img : imgList) {
				String img_url = StringTool.null2Empty(img.getUrl());
				String img_url_little = StringTool.null2Empty(img.getUrl_little());
				if (StringTool.isEmpty(img_url))
					img_url = img_url_little;
                if (StringUtils.isBlank(img_url)){
                	img_url = "http://";
                }
				ccJson.put("app_img_url", img_url);
				ccJson.put("app_img_id", img.getId());
				ccJson.put("snapshotimg_locked", img.getLocked());

				if (StringTool.isEmpty(img_url))
					ccJson.put("app_img_url_show", "");
				else if (img_url.startsWith("http://")) {
					ccJson.put("app_img_url_show", img_url);
				} else {
					ccJson.put("app_img_url_show", InitManager.combineRootHttpPath(img_url));
				}
				jsonArray.add(ccJson);
			}

			json.put("success", true);
			json.put("results", imgList.size());
			json.put("datastr", jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("success", false);
			json.put("results", -1);
			json.put("datastr", "");
		}
		logger.info("json===================" + json.toString());
		pw.print(json);

		return null;
	}

	@RequestMapping("query_cont_app.do")
	public ModelAndView queryContAppstoreHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		String start = req.getParameter("start"); logger.info("--------------start:" + start);
		String limit = req.getParameter("limit"); logger.info("--------------limit:" + limit);

		String contType = req.getParameter("contType"); logger.info("--------------contType:" + contType);
		String contStatus = req.getParameter("contStatus"); logger.info("--------------contStatus:" + contStatus);
		String contName = req.getParameter("contName"); logger.info("--------------contName:" + contName);
		String providerIds = req.getParameter("providerId");
		if (InitManager.needAuth(req) && !InitManager.isValidValue(providerIds)) { // 没有指定providerId时，根据权限分配的查询
			Operator operator = (Operator) req.getSession()
					.getAttribute("user");
         providerIds = roleService.queryIdsWithAuth(operator, "provider");

		}

	//**** 
	       List<Cont> operatorContList = contService.findAllSuperscripts();
	//****
		
		int startInt = -1;
		if (start != null && !"".equals(start.trim())) {
			startInt = Integer.parseInt(start);
		}

		int limitInt = -1;
		if (limit != null && !"".equals(limit.trim())) {
			limitInt = Integer.parseInt(limit);
		}

		if (contType == null) {
			contType = "";
		}
		if (contStatus == null) {
			contStatus = "";
		}
		if (contName == null) {
			contName = "";
		}

		List<Cont> contList = contService.findAllForApp(startInt, startInt
				+ limitInt, contType, contStatus, contName, providerIds);
		int contNum = contService.countAllForApp(providerIds, contType,
				contStatus, contName); logger.info("contNum===================" + contNum);
		String ids = "";
		for(Cont cont: contList){
			ids += cont.getId() + ",";
		}
		if(ids.length()>0) {
			ids = ids.substring(0, ids.length()-1);
		}
		List<ContVideo>  contVideoList = contVideoService.findByIds(ids);
		Map<String, String> contVideoMap = new HashMap<String, String>();
		Map<String, String> operatorContMap = new HashMap<String, String>();
		for(ContVideo contVideo: contVideoList){
			contVideoMap.put(contVideo.getC_id(), contVideo.getSuperscript_id());
		}
		for(Cont operatorCont: operatorContList){
			operatorContMap.put(operatorCont.getId(), operatorCont.getName());
		}
		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "TYPE");
		Map<String, String> typeMap = new HashMap<String, String>();
		for(StatusDict statusDict: statusDictList){
			typeMap.put(createKey(statusDict.getStatus()).trim(), statusDict.getDescription());
		}
//		statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "AD_TYPE");
//		for(StatusDict statusDict: statusDictList){
//			typeMap.put("contAdTypeId_"+createKey(statusDict.getStatus()).trim(),
//					statusDict.getDescription());
//		}
		statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "STATUS");
		for(StatusDict statusDict: statusDictList){
			typeMap.put("contStatus_"+createKey(statusDict.getStatus()).trim(),
					statusDict.getDescription());
		}

		JSONArray jsonArray = new JSONArray();
		JSONObject contentJson = new JSONObject();
		for (Cont cont : contList) {
			contentJson.put("contID", cont.getId());
			contentJson.put("contName", cont.getName());
			contentJson.put("contStatus", typeMap.get("contStatus_"+createKey(""+cont.getStatus())));
			contentJson.put("contType", cont.getType());
			contentJson.put("contDescription", cont.getDescription());
			String modify_time = "";
			if(cont.getModify_time()!=null){
				modify_time = BmUtil.formatDate(cont.getModify_time());
			}
			contentJson.put("modify_time",modify_time);
			contentJson.put("cont_superscript", StringTool.null2Empty(operatorContMap.get(contVideoMap.get(cont.getId()))));

			jsonArray.add(contentJson);
		}

		JSONObject json = new JSONObject();
		json.put("results", contNum);
		json.put("datastr", jsonArray);
		logger.info("json===================" + json.toString());
		pw.print(json);

		return null;
	}

	@RequestMapping("query_cont_app_detail.do")
	public ModelAndView queryContAppstoreDetailHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		try {
			resp.setContentType("application/json; charset=UTF-8");
			PrintWriter pw = resp.getWriter();

			String id = req.getParameter("id");
			logger.info("id: " + id);

			Cont cont = contService.findById(id);

			JSONArray jsonArray = new JSONArray();
			JSONObject ccJson = new JSONObject();

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			Date beginDate = cal.getTime();
			cal.add(Calendar.YEAR, 50);
			Date endDate = cal.getTime();

			if (cont != null) {
				ccJson.put("contAppId_temp", id);
				ccJson.put("contStatusId", cont.getStatus());
				ccJson.put("base_locked", cont.getLocked());

				Date activiItem = cont.getActive_time() == null ? beginDate : cont.getActive_time();
				String activiItemS = "0000-00-00 00:00:00";
				if (activiItem != null) {
					activiItemS = BmUtil.formatDate(activiItem);
				}
				ccJson.put("contAppactivetime1", activiItemS);

				Date deactiviItem = cont.getDeactive_time() == null ? endDate : cont.getDeactive_time();
				String deactiviItemS = "0000-00-00 00:00:00";
				if (deactiviItem != null) {
					deactiviItemS = BmUtil.formatDate(deactiviItem);
				}
				ccJson.put("contAppdeactivetime1", deactiviItemS);
			}

			ContVideo contVideo = contVideoService.findById(id);

			if (contVideo != null) {
				ccJson.put("cont_app_name_f", contVideo.getName());
				ccJson.put("cont_app_alias", contVideo.getAlias());
				ccJson.put("cont_app_discription", contVideo.getDescription());
				ccJson.put("superscript_id", contVideo.getSuperscript_id());
				String superscript_name = "";
				if (null != (contService
						.findById(contVideo.getSuperscript_id()))) {
					try {
						superscript_name = contService.findById(
								contVideo.getSuperscript_id()).getName();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					superscript_name = "";
				}
				ccJson.put("superscript_name", superscript_name);
			}

			ContAppstore contAppstore = contAppstoreService.findById(id);
			if (contAppstore != null) {
				ccJson.put("providerId", contAppstore.getProvider_id());
				ccJson.put("cont_app_capacity", contAppstore.getCapacity());
				ccJson.put("cont_app_version", contAppstore.getVersion());
				ccJson.put("cont_app_version_code", contAppstore.getVersion_code());
				ccJson.put("cont_app_staff", contAppstore.getStaff());
				ccJson.put("tags", contAppstore.getTags());
				
				Date addtiem = contAppstore.getAdd_time() == null ? beginDate : contAppstore.getAdd_time();
				String addtiemS = "0000-00-00 00:00:00";
				if (addtiem != null) {
					addtiemS = BmUtil.formatDate(addtiem);
				}
				ccJson.put("cont_add_time", addtiemS);

				ccJson.put("cont_app_package_name", contAppstore
						.getPackage_name());
				ccJson.put("cont_app_md5", contAppstore.getMd5sum());
				ccJson.put("cont_app_download_url", contAppstore
						.getDownload_url());

				String download_url = StringTool.null2Empty(contAppstore
						.getDownload_url());
				if (StringTool.isEmpty(download_url))
					ccJson.put("cont_app_download_url_show", "");
				else if (download_url.startsWith("http://")) {
					ccJson.put("cont_app_download_url_show", download_url);
				} else {
					ccJson.put("cont_app_download_url_show", InitManager.combineRootHttpPath(download_url));
				}
			}

			List<Img> imgList = imgService.findAll(id, "1");
			if (imgList != null && imgList.size() > 0) {
				Img img = imgList.get(0);

				Date cont_img_active_time = img.getActive_time() == null ? beginDate : img.getActive_time();
				String cont_img_active_timeS = "0000-00-00 00:00:00";
				if (cont_img_active_time != null) {
					cont_img_active_timeS = BmUtil.formatDate(cont_img_active_time);
				}
				ccJson.put("app_img_active_time_1", cont_img_active_timeS);

				Date cont_img_deactive_time = img.getDeactive_time() == null ? endDate : img.getDeactive_time();
				String cont_img_deactive_timeS = "0000-00-00 00:00:00";
				if (cont_img_deactive_time != null) {
					cont_img_deactive_timeS = BmUtil.formatDate(cont_img_deactive_time);
				}
				ccJson.put("app_img_deactive_time_1", cont_img_deactive_timeS);

				ccJson.put("app_img_intro", img.getIntro());

				String img_url = StringTool.null2Empty(img.getUrl());
				String img_url_little = StringTool.null2Empty(img
						.getUrl_little());
				String img_url_icon = StringTool.null2Empty(img.getUrl_icon());
				String img_url_4_squares = StringTool.null2Empty(img
						.getUrl_4_squares());
				String img_url_6_squares = StringTool.null2Empty(img
						.getUrl_6_squares());
				ccJson.put("app_img_url", img_url);
				ccJson.put("app_img_url_little", img_url_little);
				ccJson.put("app_img_url_icon", img_url_icon);
				ccJson.put("app_img_url_4_squares", img_url_4_squares);
				ccJson.put("app_img_url_6_squares", img_url_6_squares);
				ccJson.put("app_img_isurlused", img.getIs_url_used());
				ccJson.put("app_img_id", img.getId());
				ccJson.put("plat_groupId", img.getPlatgroup_id());
				ccJson.put("img_locked", img.getLocked());
				if (StringTool.isEmpty(img_url_4_squares))
					ccJson.put("app_img_url_4_squares_show", "");
				else if (img_url_4_squares.startsWith("http://")) {
					ccJson.put("app_img_url_4_squares_show", img_url_4_squares);
				} else {
					ccJson.put("app_img_url_4_squares_show", InitManager.combineRootHttpPath(img_url_4_squares));
				}
				if (StringTool.isEmpty(img_url_6_squares))
					ccJson.put("app_img_url_6_squares_show", "");
				else if (img_url_6_squares.startsWith("http://")) {
					ccJson.put("app_img_url_6_squares_show", img_url_6_squares);
				} else {
					ccJson.put("app_img_url_6_squares_show", InitManager.combineRootHttpPath(img_url_6_squares));
				}
				if (StringTool.isEmpty(img_url))
					ccJson.put("app_img_url_show", "");
				else if (img_url.startsWith("http://")) {
					ccJson.put("app_img_url_show", img_url);
				} else {
					ccJson.put("app_img_url_show", InitManager.combineRootHttpPath(img_url));
				}

				if (StringTool.isEmpty(img_url_little))
					ccJson.put("app_img_url_little_show", "");
				else if (img_url_little.startsWith("http://")) {
					ccJson.put("app_img_url_little_show", img_url_little);
				} else {
					ccJson.put("app_img_url_little_show", InitManager.combineRootHttpPath(img_url_little));
				}
				if (StringTool.isEmpty(img_url_icon))
					ccJson.put("app_img_url_icon_show", "");
				else if (img_url_icon.startsWith("http://")) {
					ccJson.put("app_img_url_icon_show", img_url_icon);
				} else {
					ccJson.put("app_img_url_icon_show", InitManager.combineRootHttpPath(img_url_icon));
				}
			} else {
				ccJson.put("app_img_id", "-1");
			}

			jsonArray.add(ccJson);

			JSONObject json = new JSONObject();

			json.put("success", true);
			json.put("siteStructure_data", jsonArray);
			logger.info(json);
			pw.print(json);
		} catch (Exception e) {
			logger.error("");
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("delete_cont_app.do")
	public ModelAndView deleteContAppstoreHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		String id = req.getParameter("id"); logger.info("-----------id:"+id);
		if(id!=null) {
			List<Menu> menuList = menuService.findByShortCutId(id);
//			List<ContVideo> contVideoList = contVideoService.findbyid(id);
			List<RelaMenuAndCont> relaMenuAndContList = relaMenuAndContService.findby_id(id);
			String info="";
			String menu="";
			String relaMenuAndCont="";
			if(menuList!=null&&menuList.size()>0){
				for(int i=0;i<menuList.size();i++){
					menu+="<"+menuList.get(i).getId()+">";
				}
				info+="作为快捷方式使用不能删除，请先删除快捷方式！栏目Id《"+menu+"》";
			}
//			if(contVideoList!=null&&contVideoList.size()>0){
//				info+="作为角标使用不能删除，请先删除角标配置信息！";
//			}
			if(relaMenuAndContList!=null&&relaMenuAndContList.size()>0){
				for(int i=0;i<relaMenuAndContList.size();i++){
					relaMenuAndCont+="<栏目ID"+relaMenuAndContList.get(i).getMenu_id()+"资产ID"+relaMenuAndContList.get(i).getC_id()+">";
				}
				info+="作为绑定资产使用不能删除，请先解绑资产！绑定资产《"+relaMenuAndCont+"》";
			}
			if(!"".equals(info)){
	         logger.info("{success:false, c_id:" + id +",info:'"+info+"'}");
				pw.print("{success:false, c_id:" + id +",info:'"+info+"'}");
			}else{
				contService.delete(id);
				contVideoService.delete(id);
            imgService.deleteAll(id, "1");//用途类型: 0 栏目, 1 内容, 2应用截图/商品缩略图
            imgService.deleteAll(id, "2");
				appDownloadUrlService.deletebycid(id);
				contAppstoreService.delete(id);
            logger.info("{success:true, errors:{}, _id:" + id +"}");
				pw.print("{success:true, errors:{}, _id:" + id +"}");
			}

		} else {
			logger.info("{success:false, errors:{}, info:'删除失败！c_id=" + id + "'}");
			pw.print("{success:false, errors:{}, info:'删除失败！c_id=" + id + "'}");
		}

		return null;
	}
	@RequestMapping("query_app_download_url.do")
	public ModelAndView queryAppDownloadUrlHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String c_id = req.getParameter("c_id"); logger.info("--------------c_id:" + c_id);
		List<AppDownloadUrl> appDownloadUrlList = appDownloadUrlService.findByCid(c_id);
		int contNum = appDownloadUrlList!=null?appDownloadUrlList.size():0; logger.info("contNum===================" + contNum);
		
      String tableName = "ZL_APP_DOWNLOAD_URL";
      String fieldName = "URL_TYPE";
      List<StatusDict> statusDictList = statusDictService.queryStatusDict(tableName, fieldName);
      Map<String, String> statusDictMap = new HashMap<String, String>();
      for(StatusDict statusDict: statusDictList){
         statusDictMap.put(statusDict.getStatus(), statusDict.getDescription());
      }
		
		JSONArray jsonArray = new JSONArray();
		JSONObject contentJson = new JSONObject();
		if(appDownloadUrlList!=null){
			for (AppDownloadUrl appDownloadUrl : appDownloadUrlList) {
				contentJson.put("id", appDownloadUrl.getId());
				contentJson.put("c_id", appDownloadUrl.getC_id());
				contentJson.put("app_name", appDownloadUrl.getApp_name());
				contentJson.put("c_type", appDownloadUrl.getC_type());
				contentJson.put("package_name", appDownloadUrl.getPackage_name());
				contentJson.put("provider_id", appDownloadUrl.getProvider_id());
				contentJson.put("version", appDownloadUrl.getVersion());
				contentJson.put("version_code", appDownloadUrl.getVersion_code());
				contentJson.put("site", appDownloadUrl.getSite());
				contentJson.put("capacity", appDownloadUrl.getCapacity());
				contentJson.put("md5sum", appDownloadUrl.getMd5sum());

				String add_time = "";
				if(appDownloadUrl.getAdd_time()!=null){
					add_time = BmUtil.formatDate(appDownloadUrl.getAdd_time());
				}
				String create_time = "";
				if(appDownloadUrl.getCreate_time()!=null){
					create_time = BmUtil.formatDate(appDownloadUrl.getCreate_time());
				}
				String modify_time = "";
				if(appDownloadUrl.getModify_time()!=null){
					modify_time = BmUtil.formatDate(appDownloadUrl.getModify_time());
				}
				String expire_time = "";
				if(appDownloadUrl.getTemp_url_expire_time()!=null){
					expire_time = BmUtil.formatDate(appDownloadUrl.getTemp_url_expire_time());
				}
				contentJson.put("add_time", add_time);
				contentJson.put("create_time", create_time);
				contentJson.put("modify_time", modify_time);
				
				contentJson.put("url_type", appDownloadUrl.getUrl_type());
				contentJson.put("url_type_desc", statusDictMap.get(""+appDownloadUrl.getUrl_type()));
				contentJson.put("download_url", appDownloadUrl.getDownload_url());
				contentJson.put("share_password", appDownloadUrl.getShare_password());
				contentJson.put("temp_url_exepire_time", expire_time);
				if ("500".equals(appDownloadUrl.getUrl_type())){
					appDownloadUrl.setUpgrade_temp_url(ApkTools.computeSandTowerDownloadUrl(appDownloadUrl.getDownload_url()));
				}
				else
					appDownloadUrl.setUpgrade_temp_url(InitManager.combineApkHttpPath(appDownloadUrl.getDownload_url()));
				contentJson.put("upgrade_temp_url", appDownloadUrl.getUpgrade_temp_url());
				jsonArray.add(contentJson);
			}
		}
		JSONObject json = new JSONObject();
		json.put("results", contNum);
		json.put("datastr", jsonArray);
		logger.info("json===================" + json.toString());
		pw.print(json);

		return null;
	}

	@RequestMapping("cloud_download.do")
	public ModelAndView cloudDownloadHandler(HttpServletRequest req,
	      HttpServletResponse resp) throws Exception {
	   resp.setContentType("application/json; charset=UTF-8");
	   PrintWriter pw = resp.getWriter();
	   String result = "{'success':false,'info':'云盘上传到服务器失败!'}";
	   JSONObject json;
	   try {
		   String c_id = req.getParameter("c_id"); logger.info("--------------c_id:" + c_id);
		   String appDownloadUrl = req.getParameter("appDownloadUrl"); logger.info("--------------appDownloadUrl:" + appDownloadUrl);
		   String sharePasswd = req.getParameter("sharePasswd"); logger.info("--------------sharePasswd:" + sharePasswd);
		   String urlType = "3";//固定云盘

		   //1，从分享地址获取云盘实际下载地址（将更新到zl_app_download_url）
		   //2，下载到服务器本地
		   //3，提取apk的信息
		   CheckUpdate update = new CheckUpdate();
		   update.setId(c_id);
		   update.setUrl_Type(urlType);
		   update.setUpdate_url(appDownloadUrl);
		   update.setShare_password("" + sharePasswd);

		   List<CheckUpdate> updateList = new ArrayList<CheckUpdate>();
		   updateList.add(update);
		   update = CloudDownloadTools.computeUpdateUrl(updateList);
		   String tempDownloadUrl =update.getUpdate_url();

		   String apkUrl = "";
		   String fileUrl = "";
		   String separator = System.getProperties().getProperty("file.separator");
		   // 得到上传文件的名称,并截取
		   String extensionName = "apk";// 获取文件后缀名

		   apkUrl = "upload" +separator+(new SimpleDateFormat("yyyyMM").format(new Date()))+separator+ c_id + "." + extensionName;
		   if (InitManager.getRootLocalPath().endsWith("\\")|| InitManager.getRootLocalPath().endsWith("/")) {
			   fileUrl = InitManager.getRootLocalPath() + apkUrl;
		   } else {
			   fileUrl = InitManager.getRootLocalPath() + separator + apkUrl;
		   }
		   if ("\\".equals(separator)){
			   fileUrl = fileUrl.replace("/", separator);
		   }
		   logger.info("云盘上传到服务器:fileUrl="+fileUrl+",tempDownloadUrl="+tempDownloadUrl);

		   json = new JSONObject();
		   String tempFileUrl = fileUrl+"_temp";
		   try {
			   File delfile = new File(tempFileUrl);
			   if (delfile.exists()) delfile.delete();
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		   if (HttpUtils.downloadFile(tempDownloadUrl, tempFileUrl, true)) {
			   Map<String,String > info = ApkTools.getApkInfo(tempFileUrl);
			   logger.info("apkInfo "+ info);
			   if (info.get("package_name") == null){
				   json.put("success", false);
				   json.put("info", "云盘上传到服务器成功,但解析失败！未获取到包名.");
				   logger.info("云盘上传到服务器成功,但解析失败！未获取到包名.fileUrl="+fileUrl+",tempDownloadUrl="+tempDownloadUrl);
			   }
			   else {
				   //save to db//将同时更新到ZL_APP_DOWNLOAD_URL和ZL_CONT_APPSTORE
				   String resultStr = appDownloadUrlService.saveDownloadUrl(c_id,urlType,appDownloadUrl,sharePasswd,tempDownloadUrl,update.getTempUrlExpireTime(),info);
				   if (resultStr == null){
					   resultStr = appDownloadUrlService.saveDownloadUrl(c_id,"0",apkUrl,"","",null,info);
					   if (resultStr == null){
						   json.put("success", true);
						   json.put("info", "云盘上传到服务器成功!");
						   json.put("file_size", info.get("file_size"));
						   json.put("version", info.get("version"));
						   json.put("version_code",info.get("versionCode"));
						   json.put("md5sum", info.get("md5sum"));
						   json.put("package_name", info.get("package_name"));			
						   if(update.getTempUrlExpireTime()!=null){
							   json.put("temp_url_expire_time", BmUtil.formatDate(update.getTempUrlExpireTime()));
						   }
						   json.put("apk_url", tempDownloadUrl);

						   File oldFile = new File(fileUrl);
						   File newFile = new File(tempFileUrl);
						   oldFile.delete();
						   newFile.renameTo(oldFile);
					   }
				   }
				   if (resultStr != null){
					   json.put("success", false);
					   json.put("info", resultStr);
				   }
			   }
			   result = json.toString();
		   }
	   } catch (Exception e) {
		   e.printStackTrace();
		   String msg = ""+e.getMessage();msg = msg.replace("\n", "");
		   result = "{'success':false,'info':'云盘上传到服务器失败!'"+msg+"}";
	   }
	   logger.info(result);
	   pw.print(result);
	   
	   return null;
	}
	@RequestMapping("apk_http_download.do")
	public ModelAndView httpDownloadHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String c_id = req.getParameter("c_id"); logger.info("--------------c_id:" + c_id);
		String sandTowerCDN = req.getParameter("sand_tower"); logger.info("--------------sandTowerCDN:" + sandTowerCDN);
		String appDownloadUrl = req.getParameter("appDownloadUrl"); logger.info("--------------appDownloadUrl:" + appDownloadUrl);
		String urlType = "1";//普通下载地址
		boolean isSandTowerCDN = "1".equals(sandTowerCDN);
		if (isSandTowerCDN){
			urlType = "500";//沙塔CDN
		}
		//1，下载到服务器本地
		//2，提取apk的信息
		CheckUpdate update = new CheckUpdate();
		update.setId(c_id);
		update.setUrl_Type(urlType);
		update.setUpdate_url(appDownloadUrl);

		String apkUrl = "";
		String fileUrl = "";
		String separator = System.getProperties().getProperty("file.separator");
		// 得到上传文件的名称,并截取
		String extensionName = "apk";// 获取文件后缀名

		apkUrl = "upload" +separator+(new SimpleDateFormat("yyyyMM").format(new Date()))+separator+ c_id + "." + extensionName;
		if (InitManager.getRootLocalPath().endsWith("\\")|| InitManager.getRootLocalPath().endsWith("/")) {
			fileUrl = InitManager.getRootLocalPath() + apkUrl;
		} else {
			fileUrl = InitManager.getRootLocalPath() + separator + apkUrl;
		}
		if ("\\".equals(separator)){
			fileUrl = fileUrl.replace("/", separator);
		}
		logger.info("http上传到服务器:fileUrl="+fileUrl+",appDownloadUrl="+appDownloadUrl);

		JSONObject json = new JSONObject();
		String tempFileUrl = fileUrl+"_temp";
		try {
			File delfile = new File(tempFileUrl);
			if (delfile.exists()) delfile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean isDownloadOK = false;
		if (isSandTowerCDN){
			String appSanTowerDownloadUrl = ApkTools.computeSandTowerDownloadUrl(appDownloadUrl);//计算防盗链地址
			logger.info("--沙塔 sandTowerCDN:urlType="+urlType+",appSanTowerDownloadUrl=" + appSanTowerDownloadUrl);
			isDownloadOK = HttpUtils.downloadFile(appSanTowerDownloadUrl, tempFileUrl, true);
		}
		else
			isDownloadOK = HttpUtils.downloadFile(appDownloadUrl, tempFileUrl, true);
		if (isDownloadOK) {
			logger.info("--:ApkTools.AAPT=" + ApkTools.AAPT);
			Map<String,String > info = ApkTools.getApkInfo(tempFileUrl);
			logger.info("apkInfo "+ info);
			if (info.get("package_name") == null){
				json.put("success", false);
				json.put("info", "http方式上传到服务器成功,但解析失败！未获取到包名.");
				logger.info("http方式上传到服务器成功,但解析失败！未获取到包名.fileUrl="+fileUrl+",appDownloadUrl="+appDownloadUrl);
			}
			else {
				//save to db//将同时更新到ZL_APP_DOWNLOAD_URL和ZL_CONT_APPSTORE
				try {
					String resultStr = appDownloadUrlService.saveDownloadUrl(c_id,urlType,appDownloadUrl,null,appDownloadUrl,null,info);
					if (resultStr == null){
						resultStr = appDownloadUrlService.saveDownloadUrl(c_id,"0",apkUrl,"","",null,info);
						if (resultStr == null){
							json.put("success", true);
							json.put("info", "http上传到服务器成功!");
							json.put("file_size", info.get("file_size"));
							json.put("version", info.get("version"));
							json.put("version_code",info.get("versionCode"));
							json.put("md5sum", info.get("md5sum"));
							json.put("package_name", info.get("package_name"));			
							if(update.getTempUrlExpireTime()!=null){
								json.put("temp_url_expire_time", BmUtil.formatDate(update.getTempUrlExpireTime()));
							}
							json.put("apk_url", appDownloadUrl);

							File oldFile = new File(fileUrl);
							File newFile = new File(tempFileUrl);
							oldFile.delete();
							newFile.renameTo(oldFile);
						}
					}
					if (resultStr != null){
						json.put("success", false);
						json.put("info", resultStr);
					}
				} catch (Exception e) {
					e.printStackTrace();
					json.put("success", false);
					json.put("info", "http上传到服务器失败,请检查文件是否存在！"+e.getMessage());
				}
			}
		}
		else {
			json.put("success", false);
			json.put("info", "http上传到服务器失败,请检查文件是否存在！");
		}
		logger.info(json.toString());
		pw.print(json.toString());

		return null;
	}

	@RequestMapping("delete_download.do")
   public ModelAndView deleteDownloadUrlHandler(HttpServletRequest req,
         HttpServletResponse resp) throws Exception {

      logger.info("--:");
      resp.setContentType("application/json; charset=UTF-8");
      PrintWriter pw = resp.getWriter();

      String id = req.getParameter("id"); logger.info("-----------id:"+id);
      JSONObject json = new JSONObject();
      try {
         appDownloadUrlService.delete(id);
         
         json.put("success", true);
         json.put("info", "删除成功!");
         json.put("id", id);
      } catch (Exception e) {
         e.printStackTrace();
         
         json.put("success", false);
         json.put("info", "删除失败!");
         json.put("id", id);
      }
      logger.info(json.toString());
      pw.print(json.toString());
      
      return null;
   }
   private String createKey (String key) {
	   return "key_" + key;
	   //			String result = "0";
	   //	      try {
	   //	         result = ""+Integer.parseInt(key);
	   //	      } catch (NumberFormatException e) {
	   //	         e.printStackTrace();
	   //	      }
	   //	      result = "key_" + result;
	   //
	   //			return result;
   }
}
