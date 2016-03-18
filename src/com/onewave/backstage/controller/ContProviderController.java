package com.onewave.backstage.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.StringTool;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.ContentSalesBean.PayType;
import com.onewave.backstage.service.ContProviderService;
import com.onewave.backstage.service.ContSalesPayTypeService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.util.BmUtil;

@Controller("contProviderController")
@RequestMapping("/contprovider/*")
public class ContProviderController extends MultiActionController {

	private static final Logger logger = Logger
			.getLogger(ContProviderController.class);

	@Autowired
	@Qualifier("contProviderService")
	private ContProviderService contProviderService;
	@Autowired
	@Qualifier("imgController")
	private ImgController imgController;
	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;
	@Autowired
	@Qualifier("contSalesPayTypeService")
	private ContSalesPayTypeService contSalesPayTypeService;

	@RequestMapping("query_with_auth.do")
	public void queryWithAuthHandler(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		String is_combo = req.getParameter("is_combo");
		logger.info("--:is_combo="+is_combo);

		JSONArray rootArr = new JSONArray();
		try {
			Operator operator =(Operator) req.getSession().getAttribute("user");

			List<ContProvider> list = null;
			list = roleService.queryProviderListWithAuth(operator, BmUtil.isAdminOperator(req));

			JSONObject colJson = null;
			if ("1".equals(is_combo)){
				colJson = new JSONObject();
				colJson.put("cp_id", InitManager.Defaut_Unselected_ID);
				colJson.put("cp_name", "请选择");
				rootArr.add(colJson);
			}

			if(list != null && list.size() > 0) {
				for(ContProvider contProvider: list) {
					if(contProvider == null) continue;

					colJson = new JSONObject();

					colJson.put("cp_id", contProvider.getId());
					colJson.put("cp_name", contProvider.getName());
					rootArr.add(colJson);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JSONObject root = new JSONObject();
			root.put("data", rootArr);

			logger.info(root);
			resp.getWriter().print(root);
		}
	}

	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		int firstResult = Integer.parseInt(req.getParameter("start"));
		int maxResults = firstResult
				+ Integer.parseInt(req.getParameter("limit"));
		String contName = req.getParameter("c_name");logger.info("--------------contName:" + contName);

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			List<PayType> contPayTypeList = contSalesPayTypeService.findAll();
			Map<String, String> contPayTypeMap = new HashMap<String, String>();
			for(PayType contPayType: contPayTypeList){
				String v = contPayType.getDescription()+(contPayType.getService_hotline()==null?"":contPayType.getService_hotline());
				contPayTypeMap.put(contPayType.getId(), v);
			}

			List<ContProvider> list = contProviderService.findAll(firstResult,
					maxResults);
			if (list != null && list.size() > 0) {
				List<ContProvider> cpList = this.contProviderService.findAll();
				Map<String, String> cpMap = new HashMap<String, String>();
				for (ContProvider cp : cpList) {
					cpMap.put(cp.getId(), cp.getName());
				}

				for (ContProvider contProvider : list) {
					if (contProvider == null)
						continue;

					rootArr.add(parserContProvider(contProvider, cpMap, contPayTypeMap));
				}
			}

			total = contProviderService.countAll();

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
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "添加失败";
		try {
			req = BmUtil.resolveMultipart(req);

			if (BmUtil.isAdminOperator(req)) {
				ContProvider contproviderold = contProviderService.findContProviderByName(req.getParameter("name"));
				if (contproviderold != null) {
					msg = "添加失败！已有相同提供商名称！";
				} else {
					ContProvider cp = new ContProvider();
					updateContProvider(cp, req);
					String cpId = contProviderService.saveAndReturnId(cp);
					cp.setId(cpId);

					Img img = new Img();
					imgController.downloadImg(req, img, cp.getId(), false);
					cp.setIcon_url(img.getUrl_icon());

					issuc = contProviderService.update(cp, req.getParameter("stb_prefix_ids"));
				}
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
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "修改失败";
		try {
			req = BmUtil.resolveMultipart(req);

			if (BmUtil.isAdminOperator(req)) {
				String cpId = req.getParameter("cp_id");
				ContProvider cp = contProviderService.findById(cpId);
				updateContProvider(cp, req);

				Img img = new Img();
				img.setUrl_icon(cp.getIcon_url());
				imgController.downloadImg(req, img, cp.getId(), false);
				cp.setIcon_url(img.getUrl_icon());

				issuc = contProviderService.update(cp, req.getParameter("stb_prefix_ids"));
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
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "删除失败";
		try {

			if (BmUtil.isAdminOperator(req)) {
				issuc = contProviderService.delete(req.getParameter("cp_id"));
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

	private JSONObject parserContProvider(ContProvider cp,
			Map<String, String> contProviderMap, Map<String, String> contPayTypeMap) {
		logger.info("--:here");

		if (cp == null)
			return null;

		JSONObject colJson = new JSONObject();

		colJson.put("cp_id", cp.getId());
		colJson.put("name", StringTool.null2Empty(cp.getName()));
		colJson.put("description", StringTool.null2Empty(cp.getDescription()));
		colJson.put("site_id", cp.getSite_id());
		colJson.put("isdefault", StringTool.null2Empty(cp.getIsdefault()));
		colJson.put("icon_url", StringTool.null2Empty(BmUtil.createImgUrl(cp.getIcon_url())));
		colJson.put("c_img_icon_url", StringTool.null2Empty(BmUtil.createImgUrl(cp.getIcon_url())));
		colJson.put("hot_info", StringTool.null2Empty(cp.getHot_info()));
		colJson.put("pay_type_ids", StringTool.null2Empty(cp.getPay_type_ids()));

		if (StringUtils.isNotBlank(cp.getPay_type_ids())){
			String[] ptIdsArr = cp.getPay_type_ids().split(",");
			String ptNames = "";
			for(int i=0; i<ptIdsArr.length; i++) {
				ptNames += contPayTypeMap.get(ptIdsArr[i]) + ",";
			}
			if(ptNames.length() > 0) {
				ptNames = ptNames.substring(0, ptNames.length()-1);
			}

			colJson.put("pay_type_names", StringTool.null2Empty(ptNames));
		}else{
			colJson.put("pay_type_names", "");
		}

		colJson.put("is_apk_prior", StringTool.null2Empty(cp.getIs_apk_prior()));
		colJson.put("cont_provider_id", StringTool.null2Empty(cp.getCont_provider_id()));
		colJson.put("cont_provider_id_name",
				contProviderMap.get(cp.getCont_provider_id()));
		String a = "";
		if (cp.getCont_provider_ids() != null) {
			String[] cont_provider_ids = cp.getCont_provider_ids().split(",");
			for (int i = 0; i < cont_provider_ids.length; i++) {
				a = a + contProviderMap.get(cont_provider_ids[i]) + ",";
			}
		}
		colJson.put("cont_provider_names", a);
		colJson.put("cont_provider_ids", StringTool.null2Empty(cp.getCont_provider_ids()));
		colJson.put("chn_provider_id", StringTool.null2Empty(cp.getChn_provider_id()));
		colJson.put("chn_provider_name",
				contProviderMap.get(cp.getChn_provider_id()));
		String b = "";
		if (cp.getEpg_provider_ids() != null) {
			String[] epg_provider_ids = cp.getEpg_provider_ids().split(",");
			for (int i = 0; i < epg_provider_ids.length; i++) {
				b = b + contProviderMap.get(epg_provider_ids[i]) + ",";
			}
		}
		colJson.put("epg_provider_names", b);
		colJson.put("epg_provider_ids", StringTool.null2Empty(cp.getEpg_provider_ids()));

		colJson.put("epg_priority", StringTool.null2Empty(cp.getEpg_priority()));
		colJson.put("xmpp_index", StringTool.null2Empty(cp.getXmpp_index()));
		colJson.put("need_check_uap", StringTool.null2Empty(cp.getNeed_check_uap()));
		colJson.put("can_switchtv", StringTool.null2Empty(cp.getCan_switchtv()));
		colJson.put("can_playvideo", StringTool.null2Empty(cp.getCan_playvideo()));

		colJson.put("can_download", StringTool.null2Empty(cp.getCan_download()));
		colJson.put("can_recording", StringTool.null2Empty(cp.getCan_recording()));
		colJson.put("can_timeshift", StringTool.null2Empty(cp.getCan_timeshift()));
		colJson.put("can_playback", StringTool.null2Empty(cp.getCan_playback()));
		colJson.put("p_status", cp.getP_status());

		colJson.put("can_switchtv_pids", StringTool.null2Empty(cp.getCan_switchtv_pids()));
		String c = "";
		if (cp.getCan_switchtv_pids() != null) {
			String[] can_switchtv_pids = cp.getCan_switchtv_pids().split(",");
			for (int i = 0; i < can_switchtv_pids.length; i++) {
				c = c + contProviderMap.get(can_switchtv_pids[i]) + ",";
			}
		}
		colJson.put("can_switchtv_names", c);

		colJson.put("can_playvideo_pids", StringTool.null2Empty(cp.getCan_playvideo_pids()));
		String d = "";
		if (cp.getCan_playvideo_pids() != null) {
			String[] can_playvideo_pids = cp.getCan_playvideo_pids().split(",");
			for (int i = 0; i < can_playvideo_pids.length; i++) {
				d = d + contProviderMap.get(can_playvideo_pids[i]) + ",";
			}
		}
		colJson.put("can_playvideo_names", d);

		colJson.put("can_download_pids", StringTool.null2Empty(cp.getCan_download_pids()));
		String e = "";
		if (cp.getCan_download_pids() != null) {
			String[] can_download_pids = cp.getCan_download_pids().split(",");
			for (int i = 0; i < can_download_pids.length; i++) {
				e = e + contProviderMap.get(can_download_pids[i]) + ",";
			}
		}
		colJson.put("can_download_names", e);

		colJson.put("can_recording_pids", StringTool.null2Empty(cp.getCan_recording_pids()));
		String f = "";
		if (cp.getCan_recording_pids() != null) {
			String[] can_recording_pids = cp.getCan_recording_pids().split(",");
			for (int i = 0; i < can_recording_pids.length; i++) {
				f = f + contProviderMap.get(can_recording_pids[i]) + ",";
			}
		}
		colJson.put("can_recording_names", f);

		colJson.put("can_timeshift_pids", StringTool.null2Empty(cp.getCan_timeshift_pids()));
		String g = "";
		if (cp.getCan_timeshift_pids() != null) {
			String[] can_timeshift_pids = cp.getCan_timeshift_pids().split(",");
			for (int i = 0; i < can_timeshift_pids.length; i++) {
				g = g + contProviderMap.get(can_timeshift_pids[i]) + ",";
			}
		}
		colJson.put("can_timeshift_names", g);

		colJson.put("can_playback_pids", StringTool.null2Empty(cp.getCan_playback_pids()));
		String h = "";
		if (cp.getCan_playback_pids() != null) {
			String[] can_playback_pids = cp.getCan_playback_pids().split(",");
			for (int i = 0; i < can_playback_pids.length; i++) {
				h = h + contProviderMap.get(can_playback_pids[i]) + ",";
			}
		}
		colJson.put("can_playback_names", h);

		colJson.put("stb_prefix", StringTool.null2Empty(cp.getStb_prefix()));
		colJson.put("stb_prefix_ids", StringTool.null2Empty(cp.getStb_prefix_ids()));
		colJson.put("stb_prefix_names", StringTool.null2Empty(cp.getStb_prefix_names()));
		colJson.put("create_time", StringTool.null2Empty(BmUtil.formatDate(cp.getCreate_time())));
		colJson.put("modify_time", StringTool.null2Empty(BmUtil.formatDate(cp.getModify_time())));

		return colJson;
	}

	private void updateContProvider(ContProvider cp, HttpServletRequest req) {
		logger.info("--:here");

		try {

			cp.setName(req.getParameter("name"));
			cp.setDescription(req.getParameter("description"));
			cp.setSite_id(req.getParameter("site_id"));
			cp.setIsdefault(req.getParameter("isdefault"));

			cp.setIs_apk_prior(req.getParameter("is_apk_prior"));
			cp.setCont_provider_id(req.getParameter("cont_provider_id"));
			cp.setCont_provider_ids(req.getParameter("cont_provider_ids"));
			cp.setChn_provider_id(req.getParameter("chn_provider_id"));// 需要更改
			cp.setEpg_provider_ids(req.getParameter("epg_provider_ids"));// 需要更改

			int i = 0;
			try {
				i = Integer.parseInt(req.getParameter("epg_priority"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			cp.setEpg_priority(String.valueOf(i));// 需要更改
			cp.setXmpp_index(req.getParameter("xmpp_index"));
			cp.setNeed_check_uap(req.getParameter("need_check_uap"));
			cp.setCan_switchtv(req.getParameter("can_switchtv"));
			cp.setCan_playvideo(req.getParameter("can_playvideo"));

			cp.setCan_download(req.getParameter("can_download"));
			cp.setCan_recording(req.getParameter("can_recording"));
			cp.setCan_timeshift(req.getParameter("can_timeshift"));
			cp.setCan_playback(req.getParameter("can_playback"));
			cp.setP_status(req.getParameter("p_status"));

			cp.setCan_switchtv_pids(req.getParameter("can_switchtv_pids"));
			cp.setCan_playvideo_pids(req.getParameter("can_playvideo_pids"));
			cp.setCan_download_pids(req.getParameter("can_download_pids"));
			cp.setCan_recording_pids(req.getParameter("can_recording_pids"));
			cp.setCan_timeshift_pids(req.getParameter("can_timeshift_pids"));

			cp.setCan_playback_pids(req.getParameter("can_playback_pids"));
			cp.setStb_prefix(req.getParameter("stb_prefix"));

			//cp.setIcon_url(req.getParameter("icon_url"));
			cp.setHot_info(req.getParameter("hot_info"));
			cp.setPay_type_ids(req.getParameter("pay_type_ids"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
