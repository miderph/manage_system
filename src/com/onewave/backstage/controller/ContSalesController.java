package com.onewave.backstage.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import net.zhilink.tools.InitManager;
import net.zhilink.tools.PinyinUtil;
import net.zhilink.tools.StringTool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import net.zhilink.tools.ImageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.excel.ContSalesExcel;
import com.onewave.backstage.excel.ExcelUtil;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContHotInfo;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.model.Column;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.RelaMenuAndCont;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.model.ContentSalesBean.PayType;
import com.onewave.backstage.service.ContHotInfoService;
import com.onewave.backstage.service.ContSalesPayTypeService;
import com.onewave.backstage.service.ContSalesService;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoFileService;
import com.onewave.backstage.service.ContVideoService;
import com.onewave.backstage.service.ImgService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.RelaMenuAndContService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.backstage.service.UserGroupService;
import com.onewave.backstage.service.impl.ContProviderServiceImpl;
import com.onewave.backstage.util.BmUtil;

@Controller("contSalesController")
@RequestMapping("/sales/*")
public class ContSalesController extends MultiActionController {
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
	@Qualifier("statusDictService")
	private StatusDictService statusDictService;
	@Autowired
	@Qualifier("contProviderService")
	private ContProviderServiceImpl contProviderService;
	@Autowired
	@Qualifier("contSalesService")
	private ContSalesService contSalesService;
	@Autowired
	@Qualifier("contSalesPayTypeService")
	private ContSalesPayTypeService contSalesPayTypeService;
	@Autowired
	@Qualifier("menuService")
	private MenuService menuService;
	@Autowired
	@Qualifier("relaMenuAndContService")
	private RelaMenuAndContService relaMenuAndContService;
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
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		int firstResult = Integer.parseInt(req.getParameter("start"));logger.info("--------------firstResult:" + firstResult);
		int maxResults = firstResult + Integer.parseInt(req.getParameter("limit"));logger.info("--------------maxResults:" + maxResults);

		String providerId = req.getParameter("provider_id");logger.info("--------------providerId:" + providerId);
		String contStatus = req.getParameter("c_status");logger.info("--------------contStatus:" + contStatus);
		String contName = req.getParameter("c_name");logger.info("--------------contName:" + contName);
		String price_from = req.getParameter("c_price_from");logger.info("--------------price_from:" + price_from);
		String price_to = req.getParameter("c_price_to");logger.info("--------------price_to:" + price_to);

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

			List<PayType> contPayTypeList = contSalesPayTypeService.findAll();
			Map<String, String> contPayTypeMap = new HashMap<String, String>();
			for(PayType contPayType: contPayTypeList){
				String v = contPayType.getDescription()+(contPayType.getService_hotline()==null?"":contPayType.getService_hotline());
				contPayTypeMap.put(contPayType.getId(), v);
			}

			total = contService.countAllForSales(providerIds, "8", contStatus, contName, price_from, price_to);logger.info("contNum===================" + total);
			List<Cont> contList = contService.findAllForSales(firstResult, maxResults, "8", contStatus, contName, providerIds, price_from, price_to);

			String ids = "";
			for(Cont cont: contList){
				ids += cont.getId() + ",";
			}

			if(ids.length()>0) ids = ids.substring(0, ids.length()-1);

			List<ContVideo>  contVideoList = contVideoService.findByIds(ids);
			List<ContentSalesBean> contSalesList = contSalesService.findByIds(ids);

			Map<String, ContVideo> contVideoMap = new HashMap<String, ContVideo>();
			Map<String, ContentSalesBean> contSalesMap = new HashMap<String, ContentSalesBean>();

			for(ContVideo contVideo: contVideoList){
				contVideoMap.put(contVideo.getC_id(), contVideo);
			}
			for(ContentSalesBean contSales: contSalesList){
				contSalesMap.put(contSales.getC_id(), contSales);
			}

			JSONObject json = null;
			ContentSalesBean contSales = null;
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

				contSales = contSalesMap.get(cont.getId());
				if (contSales != null) {
					parserContSales(json, contSales);

					String ptIds = contSales.getPay_type_ids();
					if(ptIds==null) ptIds = "";

					ptIds = ptIds.trim();
					json.put("cs_pay_type_ids", ptIds);

					String[] ptIdsArr = ptIds.split(",");

					String ptNames = "";
					for(int i=0; i<ptIdsArr.length; i++) {
						ptNames += contPayTypeMap.get(ptIdsArr[i]) + ",";
					}

					if(ptNames.length() > 0) {
						ptNames = ptNames.substring(0, ptNames.length()-1);
					}

					json.put("cs_pay_type_names", ptNames);
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
			json.put("cv_play_url", StringTool.null2Empty(StringTool.null2Empty(contVideo.getPlay_url())));
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
			json.put("active_time", BmUtil.formatDate(cont.getActive_time()));
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
	private void parserContSales(JSONObject json, ContentSalesBean contSales) {
		if (contSales != null){
			json.put("cs_hot_info", StringTool.null2Empty(contSales.getHot_info()));
			json.put("cs_fake_price", StringTool.null2Empty(contSales.getFake_price()));
			json.put("cs_sale_price", StringTool.null2Empty(contSales.getSale_price()));
			json.put("cs_price_desc", StringTool.null2Empty(contSales.getPrice_desc()));
			json.put("cs_real_price", StringTool.null2Empty(contSales.getReal_price()));
			json.put("cs_post_price", StringTool.null2Empty(contSales.getPost_price()));
			json.put("cs_disaccount", StringTool.null2Empty(contSales.getDisaccount()));
			json.put("cs_cp_name", StringTool.null2Empty(contSales.getCp_name()));//正标题
			json.put("cs_sub_cp_name", StringTool.null2Empty(contSales.getSub_cp_name()));//副标题
			json.put("cs_sum_stock", StringTool.null2Empty(contSales.getSum_stock()));//库存数量
			json.put("cs_sales_no", StringTool.null2Empty(contSales.getSales_no()));//商品编号
			json.put("cs_key_words", StringTool.null2Empty(contSales.getKey_words()));
			json.put("cs_shop_id", StringTool.null2Empty(contSales.getShop_id()));
			json.put("cs_channel_id", StringTool.null2Empty(contSales.getChannel_id()));

			json.put("cs_gift", StringTool.null2Empty(contSales.getGift()));//赠品
			json.put("cs_sum_sale", StringTool.null2Empty(contSales.getSum_sale()));//销量
			json.put("cs_post_desc", StringTool.null2Empty(contSales.getPost_desc()));//运费描述
			json.put("cs_service_desc", StringTool.null2Empty(contSales.getService_desc()));//服务描述
			json.put("cs_cp_url", StringTool.null2Empty(contSales.getUrl()));//商家产品的url

			String serviceInfo = "";
			if (!StringUtils.isBlank(contSales.getGift())){
				serviceInfo += "\n赠品："+contSales.getGift();
			}
			if (!StringUtils.isBlank(contSales.getSum_sale())){
				serviceInfo += "\n销量："+contSales.getSum_sale();
			}
			if (!StringUtils.isBlank(contSales.getPost_desc())){
				serviceInfo += "\n运费："+contSales.getPost_desc();
			}
			if (!StringUtils.isBlank(contSales.getService_desc())){
				serviceInfo += "\n服务："+contSales.getService_desc();
			}
			if (!StringUtils.isBlank(serviceInfo)){
				serviceInfo = serviceInfo.substring(1);
			}
			json.put("cs_service_info",StringTool.null2Empty(serviceInfo));

			String is_support_pxgj = "0";//是否支持拍下改价：0 不支持，1 支持
			try {
				if ((Integer.parseInt(contSales.getBitmask_price())&2) == 2){
					is_support_pxgj = "1";
				}
			} catch (Exception e) {
				//ignore logger.error(e.getMessage(), e);
			}
			json.put("bitmask_price", is_support_pxgj);//拍下改价

			if (StringUtils.isBlank(contSales.getDetail_pic_file()))
				json.put("cs_detail_pic_list", "");
			else try{
				String filePath = InitManager.getRootLocalPath() + contSales.getDetail_pic_file();
				json.put("cs_detail_pic_list", FileUtils.readFileToString(new File(filePath), "UTF-8"));//服务描述
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				json.put("cs_detail_pic_list", "");
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
				logger.info("--:cs_base_info=" + req.getParameter("cs_base_info"));
				has_base_info = Integer.parseInt(req.getParameter("cs_base_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			Cont cont = new Cont();
			if(has_base_info) {

				updateCont(cont, req);
				ContentSalesBean contSales = new ContentSalesBean();
				updateContSales(contSales, req);
				if (!StringUtils.isEmpty(contSales.getSales_no())){
					List<ContentSalesBean> salesList = contSalesService.findBySalesNo(cont.getProvider_id(),contSales.getSales_no());
					if (salesList != null && salesList.size() > 0){//duolebo:zyf 2015-01-31//商品编号重复
						msg = "保存失败！商品编号重复:"+contSales.getSales_no();
						throw new Exception(msg) ;
					}
				}

				String c_id = contService.saveAndReturnId(cont);
				cont.setId(c_id);
				if (c_id != null && !"".equals(c_id.trim()) && -1 < Integer.parseInt(c_id)) {
					ContVideo contVideo = new ContVideo();
					updateContVideo(contVideo, req);
					contVideo.setC_id(c_id);
					issuc = contVideoService.save(contVideo);

					if(issuc) {
						contSales.setC_id(c_id);
						updateContSalesDetailPicFile(contSales, req);

						issuc = contSalesService.save(contSales);

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
					throw new Exception(msg) ;
				}
			}

			boolean has_img_info = false;
			try {
				logger.info("--:cs_img_info=" + req.getParameter("cs_img_info"));
				has_img_info = Integer.parseInt(req.getParameter("cs_img_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if(issuc && has_img_info) {
				issuc = imgController.saveOrUpdateImg(req, cont.getId(), cont.getProvider_id(), "1");
				if(!issuc) {
					msg = "添加图片信息失败";
					throw new Exception(msg) ;
				}
			}

		} catch (Exception e) {
			issuc = false;
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
				logger.info("--:cs_base_info=" + req.getParameter("cs_base_info"));
				has_base_info = Integer.parseInt(req.getParameter("cs_base_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			String c_id = req.getParameter("c_id");

			Cont cont = contService.findById(c_id);
			if(cont != null && has_base_info) {
				ContentSalesBean contSales = new ContentSalesBean();
				contSales.setC_id(c_id);
				updateContSales(contSales, req);
				if (!StringUtils.isEmpty(contSales.getSales_no())){
					List<ContentSalesBean> salesList = contSalesService.findBySalesNo(contSales.getProvider_id(),contSales.getSales_no());
					if (salesList != null && salesList.size() > 0){
						if (salesList.size()>1 || (!salesList.get(0).getC_id().equals(c_id))){//duolebo:zyf 2015-01-31//商品编号重复
							msg = "保存失败！商品编号重复:"+contSales.getSales_no();
							throw new Exception(msg) ;
						}
					}
				}

				updateCont(cont, req);
				issuc = contService.update(cont);

				if(!issuc) {
					msg = "修改基本信息失败";
					throw new Exception(msg) ;
				}

				ContVideo contVideo = contVideoService.findById(c_id);
				if(contVideo != null) {
					updateContVideo(contVideo, req);
					issuc = contVideoService.update(contVideo);
					
					if(!issuc) {
						msg = "修基本信息失败";
						throw new Exception(msg) ;
					}
				}
				
				contSales = contSalesService.findById(c_id);
				if(contSales != null) {
					updateContSales(contSales, req);
					updateContSalesDetailPicFile(contSales, req);
					issuc = contSalesService.update(contSales);
					
					if(!issuc) {
						msg = "修基本信息失败";
						throw new Exception(msg) ;
					}
				}
				else{
					contSales = new ContentSalesBean();
					contSales.setC_id(c_id);
					updateContSales(contSales, req);
					issuc = contSalesService.save(contSales);
					
					if(!issuc) {
						msg = "修基本信息失败";
						throw new Exception(msg) ;
					}
				}
			}

			boolean has_img_info = false;
			try {
				logger.info("--:cs_base_info=" + req.getParameter("cs_img_info"));
				has_img_info = Integer.parseInt(req.getParameter("cs_img_info")) == 1;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if(has_img_info && cont != null) {
				issuc = imgController.saveOrUpdateImg(req, cont.getId(), cont.getProvider_id(), "1");
				if(!issuc) {
					msg = "修改图片信息失败";
					throw new Exception(msg) ;
				}
			}

		} catch (Exception e) {
			issuc = false;
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
					contSalesService.delete(c_id);//不理会删除条数
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
		logger.info("--:here");

		try {
			cont.setName(req.getParameter("c_name"));
			cont.setPinyin(PinyinUtil.getHeadStringWithoutAnySymbol(req.getParameter("c_name")));
			cont.setStatus(Integer.parseInt(req.getParameter("c_status")));
			cont.setDescription("");
			cont.setType(8);
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
			contVideo.setDescription("");
			contVideo.setProvider_id(req.getParameter("provider_id"));
			contVideo.setPackage_name("");
			contVideo.setSuperscript_id(req.getParameter("superscript_id"));
			contVideo.setVol_update_time(new Date());
			contVideo.setPlay_url(req.getParameter("cv_play_url"));

		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void updateContSalesDetailPicFile(ContentSalesBean contSales, HttpServletRequest req) {
		logger.info("--:here");

		String piclist = req.getParameter("cs_detail_pic_list");
//		if (!StringUtils.isBlank(piclist))try{
		try{
			if (StringUtils.isBlank(piclist)) {
				piclist  = "";
			}
			String detail_pic_file = contSales.getDetail_pic_file();
			if (StringUtils.isBlank(detail_pic_file))
				detail_pic_file = "detail/"+contSales.getC_id()+".pics";
			String filePath = InitManager.getRootLocalPath() + detail_pic_file;
			File file = new File(filePath);
			if (!file.exists()){
				File parent = file.getParentFile();
				if (!parent.exists())
					parent.mkdirs();
			}
			FileUtils.writeStringToFile(file, piclist, "UTF-8");
			contSales.setDetail_pic_file(detail_pic_file);
		}catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}
	private void updateContSales(ContentSalesBean contSales, HttpServletRequest req) {
		logger.info("--:here");

		try {
			contSales.setProvider_id(req.getParameter("provider_id"));
			contSales.setName(req.getParameter("c_name"));
			contSales.setCp_name(req.getParameter("cs_cp_name"));
			contSales.setSub_cp_name(req.getParameter("cs_sub_cp_name"));
			contSales.setDisaccount(req.getParameter("cs_disaccount"));
			contSales.setFake_price(req.getParameter("cs_fake_price"));
			contSales.setSale_price(req.getParameter("cs_sale_price"));
			contSales.setPrice_desc(req.getParameter("cs_price_desc"));
			//contSales.setReal_price(req.getParameter("cs_real_price"));
			contSales.setPost_price(req.getParameter("cs_post_price"));
			contSales.setHot_info(req.getParameter("cs_hot_info"));
			contSales.setKey_words(req.getParameter("cs_key_words"));
			contSales.setPay_type_ids(req.getParameter("cs_pay_type_ids"));
			contSales.setSales_no(req.getParameter("cs_sales_no"));
			contSales.setSum_stock(req.getParameter("cs_sum_stock"));
			contSales.setGift(req.getParameter("cs_gift"));
			contSales.setSum_sale(req.getParameter("cs_sum_sale"));
			contSales.setPost_desc(req.getParameter("cs_post_desc"));
			contSales.setService_desc(req.getParameter("cs_service_desc"));
			contSales.setUrl(req.getParameter("cs_cp_url"));

			String temp = req.getParameter("bitmask_price");
			if ("1".equals(""+temp))
				contSales.setBitmask_price("2");
			else
				contSales.setBitmask_price("0");
			contSales.setShop_id(req.getParameter("cs_shop_id"));
			contSales.setChannel_id(req.getParameter("cs_channel_id"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@RequestMapping("save_excel.do")
	public void saveFromExcelHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "批量添加失败";
		try {
			req = BmUtil.resolveMultipart(req);

			File file = ExcelUtil.paserFile(req, "contsales_save");

			if(file != null && file.isFile()) {
				msg = ContSalesExcel.saveFromExcel(req, file.getPath(), 
						contService, contSalesService, contVideoService,
						contVideoFileService, roleService, statusDictService);

				if(msg != null) issuc = true;

				file.delete();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			resp.getWriter().print(rootJson);
		}
	}

	@RequestMapping("update_sum_stock_excel.do")
	public void updateSumStockFromExcelHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		String provider_id = req.getParameter("provider_id");

		boolean issuc = false;
		String msg = "批量更新购物资产库存失败";
		try {
			req = BmUtil.resolveMultipart(req);

			File file = ExcelUtil.paserFile(req, "contsales_update_da");

			if(StringUtils.isBlank(provider_id)) {
				msg += "\n供应商不能为空";
			} else if(file != null && file.isFile()) {
				msg = ContSalesExcel.updateFromExcel(req, file.getPath(), contSalesService, provider_id);

				if(msg != null) issuc = true;

				file.delete();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			resp.getWriter().print(rootJson);
		}
	}

	private String createKey (String key) {
		return "key_" + key;
		//			String result = "0";
		//	      try {
		//	         result = ""+Integer.parseInt(key);
		//	      } catch (NumberFormatException e) {
		//	         logger.error(e.getMessage(), e);
		//	      }
		//	      result = "key_" + result;
		//
		//			return result;
	}


}
