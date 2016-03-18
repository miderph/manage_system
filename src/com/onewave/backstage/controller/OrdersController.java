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

@Controller("ordersController")
@RequestMapping("/orders/*")
public class OrdersController extends MultiActionController {
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

	@RequestMapping("query_cont.do")
	public void querySalesHandler(HttpServletRequest req, HttpServletResponse resp)
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

			List<PayType> contPayTypeList = contSalesPayTypeService.findAll();
			Map<String, String> contPayTypeMap = new HashMap<String, String>();
			for(PayType contPayType: contPayTypeList){
				String v = contPayType.getDescription()+(contPayType.getService_hotline()==null?"":contPayType.getService_hotline());
				contPayTypeMap.put(contPayType.getId(), v);
			}

			total = contService.countAllForSales(providerIds, "8", contStatus, contName, "", "");logger.info("contNum===================" + total);
			List<Cont> contList = contService.findAllForSales(firstResult, maxResults, "8", contStatus, contName, providerIds,"","");

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

				//order info
				json.put("order_id", "");//订单号
				json.put("channel_order_id", "");//渠道
				json.put("order_pay_time", "");//订单支付时间
				json.put("order_cont_amount", "1");//商品数量
				json.put("cs_channel_id", contSales.getChannel_id());
				json.put("cs_channel_name", "");//渠道名称
				json.put("rewards", "0");//积分
				json.put("tiket_no", "");//优惠券编码
				json.put("order_price", contSales.getSale_price());
				json.put("tiket_reward", "");

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
	@RequestMapping("query_call.do")
	public void queryCallLogHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			JSONObject json = null;

			json = new JSONObject();
			json.put("c_id", "1");
			json.put("user_id", "1");
			json.put("user_name", "王五");
			json.put("call_from", "13612345678");
			json.put("call_to", "133xxxx");
			json.put("call_time", "2015-12-12 10:10:10");
			json.put("call_in_time", "2015-12-12 10:10:12");
			json.put("call_off_time", "2015-12-12 10:11:17");
			json.put("call_length", "65秒");
			json.put("isnew", "老用户");
			json.put("memo", "");
			json.put("oper_id", "1019");
			rootArr.add(json);

			json = new JSONObject();
			json.put("c_id", "2");
			json.put("user_id", "1");
			json.put("user_name", "王五");
			json.put("call_from", "13687654321");
			json.put("call_to", "133xxxx");
			json.put("call_time", "2015-12-12 11:10:10");
			json.put("call_in_time", "");
			json.put("call_off_time", "2015-12-12 11:11:18");
			json.put("call_length", "66秒");
			json.put("isnew", "老用户");
			json.put("memo", "未接听，已回复");
			json.put("oper_id", "1019");
			rootArr.add(json);

			json = new JSONObject();
			json.put("c_id", "3");
			json.put("user_id", "1");
			json.put("user_name", "王五");
			json.put("call_from", "136xxxx");
			json.put("call_to", "8711xxx");
			json.put("call_time", "2015-12-12 10:12:10");
			json.put("call_in_time", "2015-12-12 12:10:12");
			json.put("call_off_time", "2015-12-12 12:11:19");
			json.put("call_length", "67秒");
			json.put("isnew", "新用户");
			json.put("memo", "");
			json.put("oper_id", "1019");
			rootArr.add(json);

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
	@RequestMapping("query_addr.do")
	public void queryAddressHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			JSONObject json = null;

			json = new JSONObject();
			json.put("a_id", "1");
			json.put("user_name", "王五");
			json.put("contact_addr", "北京市丰台区南四环中路11号某大厦1011室");
			json.put("mobile_no", "136xxxx");
			json.put("contact_no", "133xxxx");
			json.put("contact_no_other", "12345678");
			json.put("memo", "");
			rootArr.add(json);

			json = new JSONObject();
			json.put("a_id", "2");
			json.put("user_name", "王五");
			json.put("contact_addr", "北京市海淀区北四环中路225号海泰大厦1717室");
			json.put("mobile_no", "136xxxx");
			json.put("contact_no", "133xxxx");
			json.put("contact_no_other", "12345678");
			json.put("memo", "");
			rootArr.add(json);

			json = new JSONObject();
			json.put("a_id", "3");
			json.put("user_name", "王五");
			json.put("contact_addr", "内蒙古自治区呼和浩特市某区某路某号某小区某号楼1022室");
			json.put("mobile_no", "136xxxx");
			json.put("contact_no", "133xxxx");
			json.put("contact_no_other", "12345678");
			json.put("memo", "");
			rootArr.add(json);

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
	@RequestMapping("query_tiket.do")
	public void queryTiketHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			JSONObject json = null;
//			1、各个站点的编码是否一 一对应，详见如下详细编码：
//			1)        购好东西-官方：31000
//			2)        购好东西-海信：31010
//			3)        购好东西-欢网：31020
//			4)        购好东西-网讯：31030
//			5)        购好东西-创维数字：31040
//			6)        购好东西-康佳-爱家市场：31050
//			7)        购好东西-羽禾直播：31060

			json = new JSONObject();
			json.put("t_id", "1");
			json.put("tiket_code", "31000");
			json.put("tiket_desc", "官方|31010|31元");
			json.put("reward", "1");
			json.put("memo", "");
			rootArr.add(json);

			json = new JSONObject();
			json.put("t_id", "1");
			json.put("tiket_code", "31010");
			json.put("tiket_desc", "海信|31010|31元");
			json.put("reward", "2");
			json.put("memo", "");
			rootArr.add(json);

			json = new JSONObject();
			json.put("t_id", "1");
			json.put("tiket_code", "31020");
			json.put("tiket_desc", "欢网|31010|31元");
			json.put("reward", "3");
			json.put("memo", "");
			rootArr.add(json);

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
			json.put("cv_alias", contVideo.getAlias());
			json.put("superscript_id", contVideo.getSuperscript_id());
			json.put("cv_play_url", StringTool.null2Empty(contVideo.getPlay_url()));
		}
	}
	private void parserCont(JSONObject json, Cont cont) {
		if (cont != null) {
			json.put("c_id", cont.getId());
			json.put("c_name", cont.getName());
			json.put("c_type", cont.getType());
			json.put("ad_type", cont.getAd_type());
			json.put("pinyin", cont.getPinyin());
			json.put("c_status", cont.getStatus());
			json.put("is_locked", cont.getLocked());
			json.put("provider_id", cont.getProvider_id());
			json.put("c_description", cont.getDescription());
			json.put("active_time", BmUtil.formatDate(cont.getActive_time()));
			json.put("deactive_time", BmUtil.formatDate(cont.getDeactive_time()));
			json.put("create_time", BmUtil.formatDate(cont.getCreate_time()));
			json.put("modify_time", BmUtil.formatDate(cont.getModify_time()));
			json.put("usergroup_ids_mac", cont.getUsergroup_ids_mac());
			json.put("usergroup_ids_model", cont.getUsergroup_ids_model());
			json.put("usergroup_ids_zone", cont.getUsergroup_ids_zone());
			json.put("usergroup_ids_channel", cont.getUsergroup_ids_channel());
			json.put("usergroup_ids_mac2", cont.getUsergroup_ids_mac2());
			json.put("usergroup_ids_model2", cont.getUsergroup_ids_model2());
			json.put("usergroup_ids_zone2", cont.getUsergroup_ids_zone2());
			json.put("usergroup_ids_channel2", cont.getUsergroup_ids_channel2());
			json.put("c_video_seg_time", cont.getVideo_seg_time());
		}
	}
	private void parserContSales(JSONObject json, ContentSalesBean contSales) {
		if (contSales != null){
			json.put("cs_hot_info", contSales.getHot_info());
			json.put("cs_fake_price", contSales.getFake_price());
			json.put("cs_sale_price", contSales.getSale_price());
			json.put("cs_price_desc", contSales.getPrice_desc());
			json.put("cs_real_price", contSales.getReal_price());
			json.put("cs_post_price", contSales.getPost_price());
			json.put("cs_disaccount", contSales.getDisaccount());
			json.put("cs_cp_name", contSales.getCp_name());//正标题
			json.put("cs_sub_cp_name", contSales.getSub_cp_name());//副标题
			json.put("cs_sum_stock", contSales.getSum_stock());//库存数量
			json.put("cs_sales_no", contSales.getSales_no());//商品编号
			json.put("cs_key_words", contSales.getKey_words());
			json.put("cs_shop_id", contSales.getShop_id());
			json.put("cs_channel_id", contSales.getChannel_id());

			json.put("cs_gift", contSales.getGift());//赠品
			json.put("cs_sum_sale", contSales.getSum_sale());//销量
			json.put("cs_post_desc", contSales.getPost_desc());//运费描述
			json.put("cs_service_desc", contSales.getService_desc());//服务描述
			json.put("cs_cp_url", contSales.getUrl());//商家产品的url

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
			json.put("cs_service_info",serviceInfo);

			String is_support_pxgj = "0";//是否支持拍下改价：0 不支持，1 支持
			try {
				if ((Integer.parseInt(contSales.getBitmask_price())&2) == 2){
					is_support_pxgj = "1";
				}
			} catch (Exception e) {
				//ignore logger.error(e.getMessage(), e);
			}
			json.put("bitmask_price", is_support_pxgj);//拍下改价

			if (!StringUtils.isBlank(contSales.getDetail_pic_file()))try{
				//String filePath = InitManager.getRootLocalPath() + contSales.getDetail_pic_file();
				//json.put("cs_detail_pic_list", FileUtils.readFileToString(new File(filePath), "UTF-8"));//服务描述
				json.put("cs_detail_pic_list", InitManager.combineRootHttpPath(contSales.getDetail_pic_file()));//服务描述
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
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
		JSONObject rootJson = new JSONObject();
		try {
			req = BmUtil.resolveMultipart(req);

			rootJson.put("order_id","1001");
			rootJson.put("c_id",""+req.getParameter("c_id"));
			rootJson.put("addr_id",""+req.getParameter("addr_id"));
			rootJson.put("tiket_no",""+req.getParameter("tiket_no"));
			
			issuc = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (issuc) {
				msg = "添加成功";
			}

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
		if (!StringUtils.isBlank(piclist))try{
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
