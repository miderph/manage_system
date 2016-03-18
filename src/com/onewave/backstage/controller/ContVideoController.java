package com.onewave.backstage.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.PinyinUtil;
import net.zhilink.tools.StringTool;
import net.zhilink.tools.UpdatePinyinTest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.PlatGroup;
import com.onewave.backstage.model.RelaMenuAndCont;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoFileService;
import com.onewave.backstage.service.ContVideoService;
import com.onewave.backstage.service.ImgService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.PlatGroupService;
import com.onewave.backstage.service.RelaMenuAndContService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.backstage.service.UserGroupService;
import com.onewave.backstage.service.impl.ContProviderServiceImpl;
import com.onewave.backstage.util.BmUtil;

@Controller("contVideoController")
@RequestMapping("/contvideo/*")
public class ContVideoController extends MultiActionController {
	@Autowired
	@Qualifier("contService")
	private ContService contService;

	@Autowired
	@Qualifier("contVideoService")
	private ContVideoService contVideoService;
	@Autowired
	@Qualifier("statusDictService")
	private StatusDictService statusDictService;
	@Autowired
	@Qualifier("imgService")
	private ImgService imgService;
	@Autowired
	@Qualifier("imgController")
	private ImgController imgController;
	@Autowired
	@Qualifier("menuService")
	private MenuService menuService;
	@Autowired
	@Qualifier("platGroupService")
	private PlatGroupService platGroupService;
	@Autowired
	@Qualifier("relaMenuAndContService")
	private RelaMenuAndContService relaMenuAndContService;
	@Autowired
	@Qualifier("contProviderService")
	private ContProviderServiceImpl contProviderService;
	@Autowired
	@Qualifier("contVideoFileService")
	private ContVideoFileService contVideoFileService;
	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;
	@Autowired
	@Qualifier("userGroupService")
	private UserGroupService userGroupService;

	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		int firstResult = Integer.parseInt(req.getParameter("start"));logger.info("--------------firstResult:" + firstResult);
		int maxResults = firstResult + Integer.parseInt(req.getParameter("limit"));logger.info("--------------maxResults:" + maxResults);

		String providerId = req.getParameter("provider_id");logger.info("--------------providerId:" + providerId);
		String contType = req.getParameter("c_type");logger.info("--------------contType:" + contType);
		String contStatus = req.getParameter("c_status");logger.info("--------------contStatus:" + contStatus);
		String contName = req.getParameter("c_name");logger.info("--------------contName:" + contName);

		contStatus = contStatus == null ? "" : contStatus;
		contType = contType == null ? "" : contType;
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
			total = contService.countAll(providerIds, contType, contStatus, contName);logger.info("contNum===================" + total);
			List<Cont> contList = contService.findAll(firstResult, maxResults, providerIds, contType, contStatus, contName);

			String ids = "";
			for(Cont cont: contList){
				ids += cont.getId() + ",";
			}

			if(ids.length()>0) ids = ids.substring(0, ids.length()-1);

			List<ContVideo>  contVideoList = contVideoService.findByIds(ids);

			Map<String, ContVideo> contVideoMap = new HashMap<String, ContVideo>();
			for(ContVideo contVideo: contVideoList){
				contVideoMap.put(contVideo.getC_id(), contVideo);
			}

			Map<String, String> typeMap = new HashMap<String, String>();
			List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "TYPE");
			if(statusDictList != null) {
				for(StatusDict statusDict: statusDictList) {
					typeMap.put(createKey(statusDict.getStatus()).trim(), statusDict.getDescription());
				}
			}

			statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "AD_TYPE");
			if(statusDictList != null) {
				for(StatusDict statusDict: statusDictList) {
					typeMap.put("ca_"+createKey(statusDict.getStatus()).trim(), statusDict.getDescription());
				}
			}

			JSONObject json = null;
			ContVideo contVideo = null;
			for (Cont cont : contList) {
				json = new JSONObject();
				parserCont(json, cont);

				int c_type = cont.getType();
				int ad_type = cont.getAd_type();
				json.put("c_type", c_type);
				String adType = "";
				if (cont.getType() == 13){//广告
					adType = typeMap.get("ca_" + createKey(""+ad_type));
					if (adType == null) {
						adType = "";
					} else {
						adType = "【" + adType + "】";
					}
				}
				json.put("c_type_str", StringTool.null2Empty(typeMap.get(createKey(""+c_type).trim()) + adType));

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

					String link_url = "";
					String zip_download_url="";
					if (cont.getType()==4 || cont.getType()==13 || cont.getType()==16){
						link_url = contVideo.getPlay_url();
						if((cont.getType()==4)) {
							if(StringUtils.isEmpty(link_url) || "http://".equals(link_url)){
								link_url=InitManager.getLinkServerUri()+"?contentid="+cont.getId();
								contVideo.setPlay_url("");
							}
						}
						if (StringUtils.isNotBlank(link_url)){
							link_url = InitManager.combineRootHttpPath(link_url);
						}
						String tempZipUrl = "html/" + cont.getId() + ".zip";
						if (new File(InitManager.getRootLocalPath()+tempZipUrl).exists()){
							zip_download_url = InitManager.combineRootHttpPath(tempZipUrl);
						}
					}

					json.put("link_url",link_url);
					json.put("zip_download_url_show", StringTool.null2Empty(zip_download_url));
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
			json.put("cv_year", contVideo.getT_show_years());
			json.put("cv_month", contVideo.getT_show_months());
			json.put("cv_duration", contVideo.getDuration());
			json.put("cv_region", contVideo.getT_region_id());
			json.put("cv_cont_type", contVideo.getT_conttype_id());
			json.put("cv_quality_type", contVideo.getT_quality_type_id());
			json.put("cv_actors", StringTool.null2Empty(contVideo.getActors()));
			json.put("cv_director", StringTool.null2Empty(contVideo.getDirector()));
			json.put("cv_screenwriter", StringTool.null2Empty(contVideo.getScreenwriter()));
			json.put("cv_language", StringTool.null2Empty(contVideo.getLanguage()));
			json.put("superscript_id", StringTool.null2Empty(contVideo.getSuperscript_id()));
			json.put("cv_has_volume", StringTool.null2Empty(contVideo.getHas_volume()));
			json.put("cv_prog_type", StringTool.null2Empty(contVideo.getProg_type()));
			json.put("cv_vol_total", StringTool.null2Empty(contVideo.getVol_total()));
			json.put("cv_vol_update_time", BmUtil.formatDate(contVideo.getVol_update_time()));
			json.put("cv_play_url", StringTool.null2Empty(contVideo.getPlay_url()));
			//json.put("cv_description", StringTool.null2Empty(contVideo.getDescription()));
		}
	}
	private void parserCont(JSONObject json, Cont cont) {
		if (cont != null) {
			json.put("c_id", cont.getId());
			json.put("c_name", StringTool.null2Empty(cont.getName()));
			json.put("c_type", cont.getType());
			json.put("ad_type", cont.getAd_type());
			json.put("pinyin", StringTool.null2Empty(cont.getPinyin()));
			json.put("c_status", cont.getStatus());
			json.put("is_locked", StringTool.null2Empty(cont.getLocked()));
			json.put("provider_id", StringTool.null2Empty(cont.getProvider_id()));
			json.put("c_description", StringTool.null2Empty(cont.getDescription()));
			json.put("active_time", StringTool.null2Empty(BmUtil.formatDate(cont.getActive_time())));
			json.put("deactive_time", BmUtil.formatDate(cont.getDeactive_time()));
			json.put("create_time", BmUtil.formatDate(cont.getCreate_time()));
			json.put("modify_time", BmUtil.formatDate(cont.getModify_time()));
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
					contVideo.setC_id(c_id);
					updateContVideo(contVideo, req, c_id, cont.getType());
					issuc = contVideoService.save(contVideo);

					if(!issuc) {
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
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "修改失败";
		try {
			req = BmUtil.resolveMultipart(req);
			boolean has_base_info = false;
			try {
				logger.info("--:cv_base_info=" + req.getParameter("cv_base_info"));
				has_base_info = Integer.parseInt(req.getParameter("cv_base_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
			String c_id = req.getParameter("c_id");
			Cont cont = contService.findById(c_id);
			if(cont != null && has_base_info ) {
				updateCont(cont, req);
				issuc = contService.update(cont);

				if(!issuc) {
					msg = "修改基本信息失败";
					return;
				}
			}

			ContVideo contVideo = contVideoService.findById(c_id);
			if(contVideo != null) {
				updateContVideo(contVideo, req, c_id, cont.getType());
				issuc = contVideoService.update(contVideo);

				if(!issuc) {
					msg = "修基本信息失败";
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
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "删除失败";
		try {
			String c_id = req.getParameter("c_id");logger.info("-----------id:" + c_id);
			if(c_id != null) {
				List<Menu> menuList = menuService.findByShortCutId(c_id);
				List<ContVideo> contVideoList = contVideoService.findbyid(c_id);
				List<RelaMenuAndCont> relaMenuAndContList = relaMenuAndContService.findby_id(c_id);
				String info="";
				String menu="";
				String contVideo="";
				String relaMenuAndCont="";
				if(menuList!=null && menuList.size()>0){
					for(int i=0;i<menuList.size();i++){
						menu+="<"+menuList.get(i).getId()+">";
					}

					info += "作为快捷方式使用不能删除，请先删除快捷方式！栏目Id《" + menu + "》";
				}

				if(contVideoList!=null&&contVideoList.size()>0){
					for(int i=0;i<contVideoList.size();i++){
						contVideo+="<"+contVideoList.get(i).getC_id()+">";
					}

					info+="作为角标使用不能删除，请先删除删除角标配置信息！使用资产Id《"+contVideo+"》";
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
					imgService.deleteAll(c_id, "");//用途类型: 0 栏目, 1 内容, 2应用截图/商品缩略图
					contVideoFileService.deleteAll(c_id);
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

		try {
			cont.setName(req.getParameter("c_name"));
			cont.setDescription(req.getParameter("c_description"));
			cont.setType(Integer.parseInt(req.getParameter("c_type")));
			cont.setProvider_id(req.getParameter("provider_id"));
			cont.setLocked(req.getParameter("is_locked"));

			cont.setUsergroup_ids_mac(req.getParameter("usergroup_ids_mac"));
			cont.setUsergroup_ids_zone(req.getParameter("usergroup_ids_zone"));
			cont.setUsergroup_ids_model(req.getParameter("usergroup_ids_model"));
			cont.setUsergroup_ids_channel(req.getParameter("usergroup_ids_channel"));
			cont.setUsergroup_ids_mac2(req.getParameter("usergroup_ids_mac2"));
			cont.setUsergroup_ids_zone2(req.getParameter("usergroup_ids_zone2"));
			cont.setUsergroup_ids_model2(req.getParameter("usergroup_ids_model2"));
			cont.setUsergroup_ids_channel2(req.getParameter("usergroup_ids_channel2"));

			cont.setStatus(Integer.parseInt(req.getParameter("c_status")));
			cont.setActive_time(BmUtil.parseDate(req.getParameter("active_time")));
			cont.setDeactive_time(BmUtil.parseDate(req.getParameter("deactive_time")));
			cont.setPinyin(PinyinUtil.getHeadStringWithoutAnySymbol(req.getParameter("c_name")));
			try {
				cont.setAd_type(Integer.parseInt(req.getParameter("ad_type")));
			} catch (Exception e) {
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void updateContVideo(ContVideo contVideo, HttpServletRequest req, String c_id, int type) {

		try {
			contVideo.setC_id(c_id);
			contVideo.setName(req.getParameter("c_name"));
			contVideo.setAlias(req.getParameter("cv_alias"));
			contVideo.setDescription(req.getParameter("c_description"));
			contVideo.setProvider_id(req.getParameter("provider_id"));

			contVideo.setActors(req.getParameter("cv_actors"));
			contVideo.setSuperscript_id(req.getParameter("superscript_id"));
			contVideo.setDirector(req.getParameter("cv_director"));
			contVideo.setScreenwriter(req.getParameter("cv_screenwriter"));
			contVideo.setLanguage(req.getParameter("cv_language"));
			contVideo.setHas_volume(req.getParameter("cv_has_volume"));
			contVideo.setVol_total(req.getParameter("cv_vol_total"));
			contVideo.setDuration(req.getParameter("cv_duration"));
			contVideo.setProg_type(req.getParameter("cv_prog_type"));

			String playUrl = req.getParameter("cv_play_url");
			if(type==4) {
				if(playUrl==null||"".equals(playUrl)||"http://".equals(playUrl)){
					contVideo.setLink_url(InitManager.getLinkServerUri()+"?contentid=" + c_id);
				} else {
					contVideo.setLink_url(InitManager.combineRootHttpPath(playUrl));
				}

				if(playUrl.equals(InitManager.getLinkServerUri()+"?contentid=" + c_id)){
					playUrl=null;
				}
			} else {
				contVideo.setLink_url(InitManager.combineRootHttpPath(playUrl));
			}
			contVideo.setPlay_url(playUrl);

			contVideo.setVol_update_time(BmUtil.parseDate(req.getParameter("cv_vol_update_time")));
			try {
				contVideo.setT_region_id(Integer.parseInt(req.getParameter("cv_region")));
			} catch (Exception e) {
			}
			try {
				contVideo.setT_conttype_id(Integer.parseInt(req.getParameter("cv_cont_type")));
			} catch (Exception e) {
			}
			try {
				contVideo.setT_quality_type_id(Integer.parseInt(req.getParameter("cv_quality_type")));
			} catch (Exception e) {
			}
			try {
				contVideo.setT_show_years(Integer.parseInt(req.getParameter("cv_year")));
			} catch (Exception e) {
			}
			try {
				contVideo.setT_show_months(Integer.parseInt(req.getParameter("cv_month")));
			} catch (Exception e) {
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@RequestMapping("cont_adtype.do")
	public ModelAndView queryContAdTypeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.info("--:");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "AD_TYPE");

		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();

		groupJson.put("contAdTypeId", InitManager.Defaut_Unselected_ID);
		groupJson.put("contAdTypeName", "请选择");
		jsonArray.add(groupJson);
		int i=1;
		for(StatusDict statusDict: statusDictList){
			groupJson.put("contAdTypeId", statusDict.getStatus());
			groupJson.put("contAdTypeName", StringTool.null2Empty(statusDict.getDescription()));
			jsonArray.add(groupJson);
		}

		JSONObject json = new JSONObject();

		json.put("results", statusDictList.size());
		json.put("contAdType_data", jsonArray);
		logger.info(json);
		pw.print(json);

		return null;
	}

	@RequestMapping("region_cont_video.do")
	public ModelAndView queryContVideoRegionHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.info("--:");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT_VIDEO", "T_REGION_ID");

		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();

		groupJson.put("regionId", InitManager.Defaut_Unselected_ID);
		groupJson.put("regionName", "请选择");
		jsonArray.add(groupJson);
		int i=1;
		for(StatusDict statusDict: statusDictList){
			groupJson.put("regionId", statusDict.getStatus());
			groupJson.put("regionName", StringTool.null2Empty(statusDict.getDescription()));
			jsonArray.add(groupJson);
		}

		JSONObject json = new JSONObject();

		json.put("results", statusDictList.size());
		json.put("region_data", jsonArray);
		logger.info(json);
		pw.print(json);

		return null;
	}

	@RequestMapping("conttype_cont_video.do")
	public ModelAndView queryContVideoConttypeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.info("--:");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT_VIDEO", "T_CONTTYPE_ID");

		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();

		groupJson.put("conttypeId", InitManager.Defaut_Unselected_ID);
		groupJson.put("conttypeName", "请选择");
		jsonArray.add(groupJson);
		int i=1;
		for(StatusDict statusDict: statusDictList){
			groupJson.put("conttypeId", statusDict.getStatus());
			groupJson.put("conttypeName", StringTool.null2Empty(statusDict.getDescription()));
			jsonArray.add(groupJson);
		}

		JSONObject json = new JSONObject();

		json.put("results", statusDictList.size());
		json.put("conttype_data", jsonArray);
		logger.info(json);
		pw.print(json);

		return null;
	}

	@RequestMapping("has_volume_cont_video.do")
	public ModelAndView queryContVideoHasVolumeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.info("--:");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		//List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT_VIDEO", "T_QUALITY_TYPE_ID");

		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();

		groupJson.put("hasVolumeId", "0");
		groupJson.put("hasVolumeName", "没剧集");
		jsonArray.add(groupJson);
		groupJson.put("hasVolumeId", "1");
		groupJson.put("hasVolumeName", "有剧集");
		jsonArray.add(groupJson);

		/*int i=1;
		for(StatusDict statusDict: statusDictList){
			groupJson.put("qualityTypeId", statusDict.getStatus());
			groupJson.put("qualityTypeName", statusDict.getDescription());
			jsonArray.add(groupJson);
		}*/

		JSONObject json = new JSONObject();

		json.put("results", "2");
		json.put("has_volume_data", jsonArray);
		logger.info(json);
		pw.print(json);

		return null;
	}

	@RequestMapping("prog_type_cont_video.do")
	public ModelAndView queryContVideoProgTypeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.info("--:");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT_VIDEO", "PROG_TYPE");

		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();

		groupJson.put("progTypeId", InitManager.Defaut_Unselected_ID);
		groupJson.put("progTypeName", "请选择");
		jsonArray.add(groupJson);
		int i=1;
		for(StatusDict statusDict: statusDictList){
			groupJson.put("progTypeId", statusDict.getStatus());
			groupJson.put("progTypeName", StringTool.null2Empty(statusDict.getDescription()));
			jsonArray.add(groupJson);
		}

		JSONObject json = new JSONObject();

		json.put("results", statusDictList.size());
		json.put("prog_type_data", jsonArray);
		logger.info(json);
		pw.print(json);

		return null;
	}

	@RequestMapping("quality_type_cont_video.do")
	public ModelAndView queryContVideoQualityTypeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.info("--:");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT_VIDEO", "T_QUALITY_TYPE_ID");

		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();

		groupJson.put("qualityTypeId", InitManager.Defaut_Unselected_ID);
		groupJson.put("qualityTypeName", "请选择");
		jsonArray.add(groupJson);
		int i=1;
		for(StatusDict statusDict: statusDictList){
			groupJson.put("qualityTypeId", statusDict.getStatus());
			groupJson.put("qualityTypeName", StringTool.null2Empty(statusDict.getDescription()));
			jsonArray.add(groupJson);
		}

		JSONObject json = new JSONObject();

		json.put("results", statusDictList.size());
		json.put("quality_type_data", jsonArray);
		logger.info(json);
		pw.print(json);

		return null;
	}

	@RequestMapping("plat_group_cont_video.do")
	public ModelAndView queryContVideoPlatGroupHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.info("--:");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		List<PlatGroup> platGroupList = platGroupService.findAll();

		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();

		groupJson.put("plat_groupId", InitManager.Defaut_Unselected_ID);
		groupJson.put("plat_groupName", "请选择");
		jsonArray.add(groupJson);
		int i=1;
		for(PlatGroup platGroup: platGroupList){
			groupJson.put("plat_groupId", ""+platGroup.getId());
			groupJson.put("plat_groupName", StringTool.null2Empty(platGroup.getName()));
			jsonArray.add(groupJson);
		}

		JSONObject json = new JSONObject();

		json.put("results", platGroupList.size());
		json.put("plat_group_data", jsonArray);
		logger.info(json);
		pw.print(json);

		return null;
	}

	@RequestMapping("query_cont_for_mar.do")
	public ModelAndView queryContForMARHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		JSONObject json = new JSONObject();
		try {
			int start = Integer.parseInt(req.getParameter("start"));
			logger.info("---------start:"+start);
			int limit = Integer.parseInt(req.getParameter("limit"));
			logger.info("---------limit:"+limit);

			String contType = req.getParameter("contType");
			if(contType==null || "-1".equals(contType)) {
				contType = "";
			}
			logger.info("---------contType:"+contType);
			String contStatus = req.getParameter("contStatus");
			logger.info("--------------contStatus:" + contStatus);
			String providerId = req.getParameter("contProvider");
			Operator operator = (Operator)req.getSession().getAttribute("user");
			if (operator == null){
				logger.info("用户没有登录！");
				return null;
			}
			String providerIds = roleService.queryIdsWithAuth(operator,"provider");
			if(providerId==null || "-1".equals(providerId) || (""+InitManager.Defaut_Unselected_ID).equals(providerId)) {
				providerId = providerIds;
			}
			else if ((","+providerIds+",").indexOf(","+providerId+",")==-1){
				providerId = "-1";
				logger.info("用户【"+operator.getName()+"】没有供应商数据的权限，providerId=" + providerId);
			}
			logger.info("---------contProvider:"+providerId);

			String keyWord = req.getParameter("keyWord");
			logger.info("---------keyWord:"+keyWord);

			String startTime = req.getParameter("startTime");
			logger.info("---------startTime:"+startTime);
			String endTime = req.getParameter("endTime");
			logger.info("---------endTime:"+endTime);

			if(startTime == null){
				startTime = "";
			}

			if(endTime == null){
				endTime = "";
			}
			String menuId = req.getParameter("menuId");logger.info("---------menuId:"+menuId);

			List<Cont> contList = contService.findAllForMAR(start, start+limit, contType, providerId, keyWord, contStatus, startTime, endTime, menuId);
			logger.info("contList.size()=" + contList.size());

			int contNum = contService.countAllForMAR(contType, providerId, keyWord, contStatus, startTime, endTime, menuId);
			logger.info("contNum===================" + contNum);
			//****
			List<Cont> operatorContList = contService.findAllSuperscripts();
			//****
			List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "TYPE");
			Map<String, String> typeMap = new HashMap<String, String>();
			for(StatusDict statusDict: statusDictList){
				typeMap.put(createKey(statusDict.getStatus()).trim(), StringTool.null2Empty(statusDict.getDescription()));
			}
			logger.info("statusDictList.size()=" + statusDictList.size());
			statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "AD_TYPE");
			for(StatusDict statusDict: statusDictList){
				typeMap.put("contAdTypeId_"+createKey(statusDict.getStatus()).trim(),
						statusDict.getDescription());
			}
			statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "STATUS");
			for(StatusDict statusDict: statusDictList){
				typeMap.put("contStatus_"+createKey(statusDict.getStatus()).trim(),
						statusDict.getDescription());
			}

			List<ContProvider> contProviderList = this.menuService.queryContProviderBySiteId("");
			Map<String, String> providerMap = new HashMap<String, String>();
			for(ContProvider contProvider_1: contProviderList){
				providerMap.put(createKey(contProvider_1.getId()).trim(), StringTool.null2Empty(contProvider_1.getName()));
			}
			logger.info("contProviderList.size()=" + contProviderList.size());

			logger.info("contList.size()=" + contList.size());
			Map<String, String> is_url_used_Map = new HashMap<String, String>();
			is_url_used_Map.put("0", "竖图");
			is_url_used_Map.put("1", "横图");
			is_url_used_Map.put("2", "小方图");
			is_url_used_Map.put("3", "四格图");
			is_url_used_Map.put("4", "六格图");
			is_url_used_Map.put("null", "六格图");
			String ids = "";
			for(Cont cont: contList){
				ids += cont.getId() + ",";
			}
			if(ids.length()>0) {
				ids = ids.substring(0, ids.length()-1);
			}
			//		***********
			List<ContVideo>  contVideoList = contVideoService.findByIds(ids);
			Map<String, String> contVideoMap = new HashMap<String, String>();
			Map<String, String> operatorContMap = new HashMap<String, String>();
			for(ContVideo contVideo: contVideoList){
				contVideoMap.put(contVideo.getC_id(), contVideo.getSuperscript_id());
			}
			for(Cont operatorCont: operatorContList){
				operatorContMap.put(operatorCont.getId(), operatorCont.getName());
			}
			//		***********
			List<Img>   imgList = imgService.findByIds(ids);
			Map<String, String> imgMap = new HashMap<String, String>();
			for(Img img: imgList){
				imgMap.put(img.getTarget_id(), img.getIs_url_used());
			}
			JSONArray jsonArray = new JSONArray();
			JSONObject contentJson = new JSONObject();
			for(Cont cont: contList){
				contentJson.put("mar_s_contId", cont.getId());
				contentJson.put("mar_s_contName", StringTool.null2Empty(cont.getName()));
				contentJson.put("mar_s_status", typeMap.get("contStatus_"+createKey(""+cont.getStatus())));
				String adType = "";
				if (cont.getType() == 13){//广告
					adType = typeMap.get("contAdTypeId_" + createKey(""+cont.getAd_type()));
					if (adType == null)
						adType = "";
					else {
						adType = "【" + adType + "】";
					}
				}
				contentJson.put("mar_s_contType", StringTool.null2Empty(typeMap.get(createKey(""+cont.getType()).trim())+adType));
				contentJson.put("mar_s_contProvider", StringTool.null2Empty(providerMap.get(createKey(cont.getProvider_id()).trim())));
				contentJson.put("mar_s_contIntro", StringTool.null2Empty(cont.getDescription()));
				contentJson.put("cont_is_url_used",is_url_used_Map.get(imgMap.get(cont.getId())));
				contentJson.put("cont_superscript", StringTool.null2Empty(operatorContMap.get(contVideoMap.get(cont.getId()))));
				jsonArray.add(contentJson);
			}

			json.put("results", contNum);
			json.put("datastr", jsonArray);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.info("json===================" + json.toString());
		pw.print(json);

		return null;
	}
	@RequestMapping("query_superscript.do")
	public ModelAndView querySuperscriptHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		JSONObject json = new JSONObject();
		try {
			int start = Integer.parseInt(req.getParameter("start"));
			logger.info("---------start:"+start);
			int limit = Integer.parseInt(req.getParameter("limit"));
			logger.info("---------limit:"+limit);
			//根据权限分配的查询
			Operator operator = (Operator)req.getSession().getAttribute("user");
			if (operator == null){
				logger.info("用户没有登录！");
				return null;
			}
			String providerIds = roleService.queryIdsWithAuth(operator,"provider");
			logger.info("---------contProvider:"+providerIds);
			List<Cont> contList = contService.findAllByRoleForMAR(start, start+limit,providerIds);
			logger.info("contList.size()=" + contList.size());

			int contNum = contService.countAllByRoleForMAR(providerIds);
			logger.info("contNum===================" + contNum);

			List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT", "TYPE");
			Map<String, String> typeMap = new HashMap<String, String>();
			for(StatusDict statusDict: statusDictList){
				typeMap.put(createKey(statusDict.getStatus()).trim(), StringTool.null2Empty(statusDict.getDescription()));
			}
			logger.info("statusDictList.size()=" + statusDictList.size());

			List<ContProvider> contProviderList = this.menuService.queryContProviderBySiteId("");
			Map<String, String> providerMap = new HashMap<String, String>();
			for(ContProvider contProvider_1: contProviderList){
				providerMap.put(createKey(contProvider_1.getId()).trim(), StringTool.null2Empty(contProvider_1.getName()));
			}
			logger.info("contProviderList.size()=" + contProviderList.size());
			JSONArray jsonArray = new JSONArray();
			JSONObject contentJson = new JSONObject();
			for(Cont cont: contList){
				contentJson.put("mar_s_contId", cont.getId());
				contentJson.put("mar_s_contName", StringTool.null2Empty(cont.getName()));
				contentJson.put("mar_s_contType", StringTool.null2Empty(typeMap.get(createKey(""+cont.getType()).trim())));
				contentJson.put("mar_s_contProvider", StringTool.null2Empty(providerMap.get(createKey(cont.getProvider_id()).trim())));
				contentJson.put("mar_s_contIntro", StringTool.null2Empty(cont.getDescription()));
				jsonArray.add(contentJson);
			}

			json.put("results", contNum);
			json.put("datastr", jsonArray);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.info("json===================" + json.toString());
		pw.print(json);
		return null;
	}
	private String createKey (String key) {
		return "key_" + key;
		//		String result = "0";
		//      try {
		//         result = ""+Integer.parseInt(key);
		//      } catch (NumberFormatException e) {
		//         logger.error(e.getMessage(), e);
		//      }
		//      result = "key_" + result;
		//
		//		return result;
	}
//	@RequestMapping("init.do")
//	public ModelAndView init(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		UpdatePinyinTest.main(null);
//		return null;
//	}

	@RequestMapping("superscript_cont_video.do")
	public ModelAndView modifySuperscriptHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String ids = req.getParameter("ids");
		logger.info("---------ids:"+ids);
		String contId = req.getParameter("contId");
		logger.info("---------contId:"+contId);
		boolean flag = true;
		flag = contVideoService.saveSuperscript(ids, contId);
		if(flag==true) {
			pw.print("{success:true, errors:{}}");
		}
		else{
			pw.print("{success:false, errors:{}}");
		}

		return null;
	}
	@RequestMapping("delete_cont_superscript.do")
	public ModelAndView deleteSuperscriptHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String ids = req.getParameter("ids");
		logger.info("---------ids:"+ids);
		String contId = null;
		logger.info("---------contId:"+contId);
		boolean flag = true;
		flag = contVideoService.saveSuperscript(ids, contId);
		if(flag==true) {
			pw.print("{success:true, errors:{}}");
		}
		else{
			pw.print("{success:false, errors:{}}");
		}	
		return null;
	}


}
