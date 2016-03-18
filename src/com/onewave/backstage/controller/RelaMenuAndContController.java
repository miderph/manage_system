package com.onewave.backstage.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.StringTool;

import org.apache.axis.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.RelaMenuAndCont;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoService;
import com.onewave.backstage.service.ImgService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.RecommendSyncService;
import com.onewave.backstage.service.RelaMenuAndContService;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.backstage.service.impl.ContProviderServiceImpl;
import com.zhilink.tv.util.ReadRelaObj;
import com.zhilink.tv.util.Readable;
import com.zhilink.tv.util.RelaObj;

@Controller("relaMenuAndContController")
@RequestMapping("/relamenu/*")
public class RelaMenuAndContController extends MultiActionController {

	private SimpleDateFormat dateformat1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	private RelaMenuAndContService relaMenuAndContService;
	@Autowired
	private ContService contService;
	@Autowired
	private StatusDictService statusDictService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private ImgService imgService;
	@Autowired
	private ContVideoService contVideoService;
	@Autowired
	private ContProviderServiceImpl contProviderService;


	@RequestMapping("save_rela_menu_cont.do")
	public ModelAndView SaveRelaMenuAndContHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String menuId = req.getParameter("menuId");
		if (menuId == null) {
			menuId = "";
		}
		Menu amenu = this.menuService.findById(menuId);
		int shortcutType = 0;
		try {
         shortcutType = Integer.parseInt(amenu.getIs_shortcut());
      } catch (Exception e) {
         e.printStackTrace();
      }
      switch (shortcutType){
      case 0:
         break;
      case 1://内容链接
      case 2://栏目链接
			String result = "{success:false, info:'保存失败！快捷方式栏目下不能编排资产，【栏目id="
					+ menuId + "】', catalog_id:" + menuId + "}";
			logger.info("---------result:" + result);
			pw.print(result);
			return null;
         //break;
      case 3://分渠道活动广告栏目
         break;
      }

		// -------------------资产编排
		String ids = req.getParameter("ids");
		if (ids == null) {
			ids = "";
		}
		ids = ids.trim();
		logger.info("---------ids:" + ids);

		if (ids.length() > 0) {
			String[] idsArr = ids.split(",");
			RelaMenuAndCont relaMenuAndCont;
			// int maxOrder = relaMenuAndContService.getMaxOrderNum(menuId);
			int minOrder = relaMenuAndContService.getMinOrderNum(menuId, InitManager.Defaut_Unselected_ID, 0);
			String cid = "";
			for (int i = 0, l = idsArr.length; i < l; i++) {
				// maxOrder = maxOrder +1;
				minOrder--;
				cid = idsArr[i];
				relaMenuAndCont = new RelaMenuAndCont();
				relaMenuAndCont.setC_id(cid);
				relaMenuAndCont.setMenu_id(menuId);
				relaMenuAndCont.setEpg_id("");
				// relaMenuAndCont.setOrder_num(""+maxOrder);
				relaMenuAndCont.setOrder_num("" + minOrder);
				relaMenuAndCont.setPrompt("");
				relaMenuAndCont.setAdmin_id("");
				relaMenuAndContService.save(relaMenuAndCont);
			}
			setVersion(menuId);
			// Menu menu = menuService.findById(menuId);
			// menu.setVersion((dateformat.format(new Date())).toString());
			// menuService.update(menu);
		}

		// ---------------------栏目混排
		String m_ids = req.getParameter("m_ids");
		if (m_ids == null) {
			m_ids = "";
		}
		m_ids = m_ids.trim();
		logger.info("---------m_ids:" + m_ids);

		if (m_ids.length() > 0) {
			String[] idsArr = m_ids.split(",");
			RelaMenuAndCont relaMenuAndCont;
			int minOrder = relaMenuAndContService.getMinOrderNum(menuId, InitManager.Defaut_Unselected_ID, 0);
			for (int i = 0, l = idsArr.length; i < l; i++) {
				String mid = idsArr[i];
				Menu menu = this.menuService.findById(mid);

				// Cont
				Cont cont = this.contService.findById(mid);
				if (cont == null) {
					cont = new Cont();
					cont.setId(menu.getId());
					cont.setName(menu.getTitle());
					cont.setType(17);
					cont.setStatus(11);
					cont.setProvider_id(menu.getProvider_id());
					cont.setActive_time(menu.getActive_time());
					cont.setDeactive_time(menu.getDeactive_time());
					this.contService.save(cont);
				} else {
					cont.setActive_time(menu.getActive_time());
					cont.setDeactive_time(menu.getDeactive_time());
					this.contService.update(cont);
				}

				// ContVideo
				ContVideo contVideo = this.contVideoService.findById(mid);
				if (contVideo == null) {
					contVideo = new ContVideo();
					contVideo.setC_id(menu.getId());
					contVideo.setName(menu.getTitle());
					contVideo.setHas_volume("0");
					cont.setProvider_id(menu.getProvider_id());
					this.contVideoService.save(contVideo);
				}

				minOrder--;
				relaMenuAndCont = new RelaMenuAndCont();
				relaMenuAndCont.setC_id(mid);
				relaMenuAndCont.setMenu_id(menuId);
				relaMenuAndCont.setEpg_id("");
				relaMenuAndCont.setOrder_num("" + minOrder);
				relaMenuAndCont.setPrompt("");
				relaMenuAndCont.setAdmin_id("");
				relaMenuAndContService.save(relaMenuAndCont);
			}
			setVersion(menuId);
			//处理下重复的orderNum
			relaMenuAndContService.deleteDuplicateOrderNum(menuId);
		}

		pw.print("{success:true, errors:{}}");
		return null;

	}

	public void setVersion(String menuId) {
		Menu menu = menuService.findById(menuId);
		menu.setVersion((dateformat.format(new Date())).toString());
		menuService.update(menu);
		if (menu.getParent_id() != null && !"0".equals(menu.getParent_id())) {
			setVersion(menu.getParent_id());
		}
	}

	@RequestMapping("query_cont.do")
	public ModelAndView queryRelaMenuAndContHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		try {
			String menuId = req.getParameter("menuId");
			// String menuId = "510";
			if (menuId == null) {
				menuId = "";
			}
			logger.info("---------menuId:" + menuId);

			int start = Integer.parseInt(req.getParameter("start"));
			logger.info("---------start:" + start);
			int limit = Integer.parseInt(req.getParameter("limit"));
			logger.info("---------limit:" + limit);
			String avalue = req.getParameter("status");
			int status = InitManager.Defaut_Unselected_ID;
			try {status = Integer.parseInt(avalue);} catch (Exception e) {}
			logger.info("---------status:" + status);
			
			avalue = req.getParameter("status_type");
			int status_type = 0;
			try {status_type = Integer.parseInt(avalue);} catch (Exception e) {}
			logger.info("---------status_type:" + status_type);
			
			int contNum = relaMenuAndContService.countAllForMAR(menuId, status, status_type);
			List<RelaMenuAndCont> relaMenuAndContList = relaMenuAndContService
					.findAllForMAR(start, start + limit, menuId, status, status_type);
			String ids = "";
			for (RelaMenuAndCont relaMenuAndCont : relaMenuAndContList) {
				ids += relaMenuAndCont.getC_id() + ",";
			}
			if (ids.length() > 0) {
				ids = ids.substring(0, ids.length() - 1);
			}

			List<StatusDict> statusDictList = this.statusDictService
					.queryStatusDict("ZL_CONT", "TYPE");
			Map<String, String> typeMap = new HashMap<String, String>();
			for (StatusDict statusDict : statusDictList) {
				typeMap.put(createKey(statusDict.getStatus()).trim(),
						statusDict.getDescription());
			}

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
			List<ContProvider> contProviderList = this.menuService
					.queryContProviderBySiteId("");
			Map<String, String> providerMap = new HashMap<String, String>();
			for (ContProvider contProvider_1 : contProviderList) {
				providerMap.put(createKey(contProvider_1.getId()).trim(),
						contProvider_1.getName());
			}
			Map<String, String> is_url_used_desc_Map = new HashMap<String, String>();
			is_url_used_desc_Map.put("0", "竖图");
			is_url_used_desc_Map.put("1", "横图");
			is_url_used_desc_Map.put("2", "小方图");
			is_url_used_desc_Map.put("3", "四格图");
			is_url_used_desc_Map.put("4", "六格图");
			is_url_used_desc_Map.put("null", "六格图");

			logger.info("contNum===================" + contNum);
			logger.info("relaMenuAndContList.toString()===================" + relaMenuAndContList.toString());

			Map<String, Cont> contMap = new HashMap<String, Cont>();
			Map<String, ContVideo> contVideoMap = new HashMap<String, ContVideo>();
			Map<String, String> imgMap = new HashMap<String, String>();
			Map<String, String> contSuperscriptMap = new HashMap<String, String>();

			List<Cont> contList = contService.findAllInIds(ids);
			for (Cont cont : contList) {
				contMap.put(cont.getId(), cont);
			}
			List<ContVideo> contVideoList = contVideoService.findByIds(ids);
			for (ContVideo contVideo : contVideoList) {
				contVideoMap.put(contVideo.getC_id(), contVideo);
			}
			List<Img> imgList = imgService.findByIds(ids);
			for (Img img : imgList) {
				imgMap.put(img.getTarget_id(), img.getIs_url_used());
			}
			List<Cont> contSuperscriptList = contService.findAllSuperscripts();
			for (Cont contSuperscript : contSuperscriptList) {
				contSuperscriptMap.put(contSuperscript.getId(),
						contSuperscript.getName());
			}

			java.util.Date now = java.util.Calendar.getInstance().getTime();
			JSONArray jsonArray = new JSONArray();
			JSONObject contentJson = new JSONObject();
			for (RelaMenuAndCont relaMenuAndCont : relaMenuAndContList) {
				Cont mCont = contMap.get(relaMenuAndCont.getC_id());

				if (mCont != null) {
					String id = mCont.getId();
					contentJson.put("mar_s_relaId", relaMenuAndCont.getId());
					contentJson.put("mar_s_contId", id);
					contentJson.put("mar_s_contName", mCont.getName());
					contentJson.put("mar_s_status", typeMap.get("contStatus_"+createKey(""+mCont.getStatus())));
					boolean isValid = mCont.getStatus()>=10 && now.after(mCont.getActive_time()) && now.before(mCont.getDeactive_time());
					contentJson.put("mar_s_status_valid", isValid ? "1":"0");
					
					if (relaMenuAndCont.getLocked() == 1)
						contentJson.put("locked", "是");
					else
						contentJson.put("locked", "");
					String adType = "";
					if (mCont.getType() == 13){//广告
						adType = typeMap.get("contAdTypeId_" + createKey(""+mCont.getAd_type()));
						if (adType == null)
							adType = "";
						else {
							adType = "【" + adType + "】";
						}
					}
					contentJson.put("mar_s_contType",
							typeMap.get(createKey("" + mCont.getType()).trim())
							+adType);
					contentJson.put("mar_s_contProvider", providerMap
							.get(createKey("" + mCont.getProvider_id()).trim()));
					contentJson.put("mar_s_contIntro", mCont.getDescription());
					contentJson.put("is_url_used", is_url_used_desc_Map
							.get(relaMenuAndCont.getIs_url_used()));
					String temp = relaMenuAndCont.getIs_url_used();
					contentJson.put("is_url_used_id", (temp == null ? "" : temp));
					contentJson.put("cont_is_url_used",
							is_url_used_desc_Map.get(imgMap.get(id)));
					temp = (String) imgMap.get(id);
					contentJson.put("cont_is_url_used_id", (temp == null ? ""
							: temp));
					if (contVideoMap.get(id) != null) try {
						contentJson.put("cont_superscript",
								contSuperscriptMap.get(contVideoMap.get(id).getSuperscript_id()));
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					
					java.util.Date date = relaMenuAndCont.getDate_time();
					String strTemp = "";
					if (date != null) {
						strTemp = dateformat1.format(date);
					}
					contentJson.put("mar_s_date_time", strTemp);
					strTemp = "";date = mCont.getActive_time();
					if (date != null) {
						strTemp = dateformat1.format(date);
					}
					contentJson.put("mar_s_active_time",strTemp);
					strTemp = "";date = mCont.getDeactive_time();
					if (date != null) {
						strTemp = dateformat1.format(date);
					}
					contentJson.put("mar_s_deactive_time",strTemp);
					strTemp = "";date = mCont.getCreate_time();
					if (date != null) {
						strTemp = dateformat1.format(date);
					}
					contentJson.put("mar_s_create_time",strTemp);
					strTemp = "";date = mCont.getModify_time();
					if (date != null) {
						strTemp = dateformat1.format(date);
					}
					contentJson.put("mar_s_modify_time",strTemp);

					jsonArray.add(contentJson);
				}
			}

			JSONObject json = new JSONObject();
			json.put("results", contNum);
			json.put("datastr", jsonArray);

			logger.info("json===================" + json.toString());
			pw.print(json);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			pw.print("{success:false, errors:{}}");
		}

		return null;

	}

	@RequestMapping("modify_order.do")
	public ModelAndView modifyRelaMenuAndContOrderNumHandler(
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		
		try {
			String menuId = req.getParameter("menuId");
			logger.info("---------menuId:" + menuId);
			String id = req.getParameter("id");
			logger.info("---------id:" + id);
			String tag = req.getParameter("tag");
			logger.info("---------tag:" + tag);
			int status = InitManager.Defaut_Unselected_ID;
			String avalue = req.getParameter("status");
			try {status = Integer.parseInt(avalue);} catch (Exception e) {}
			logger.info("---------status:" + status);

			avalue = req.getParameter("status_type");
			int status_type = 0;
			try {status_type = Integer.parseInt(avalue);} catch (Exception e) {}
			logger.info("---------status_type:" + status_type);

			if (menuId == null || "".equals(menuId.trim())) {
				pw.print("{success:false, errors:{}}");
			} else {
				try {//清除掉无效的编排//目前只返回有效的编排
					//relaMenuAndContService.deleteInvalidRelaMenuAndCont(menuId);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				//先处理下重复的orderNum
				relaMenuAndContService.deleteDuplicateOrderNum(menuId);

				RelaMenuAndCont relaMenuAndCont = relaMenuAndContService.findById(id, menuId);
				if (relaMenuAndCont != null) {
					String orderNum = relaMenuAndCont.getOrder_num();
					int order = 9999;
					try {
						order = Integer.parseInt(orderNum);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String orderStr = "";
					if ("up".equals(tag.trim())) {
						orderStr = ""
								+ (relaMenuAndContService.getPrevOrderNum(menuId,
										"" + order, status, status_type));
					} else if ("down".equals(tag.trim())) {
						orderStr = ""
								+ (relaMenuAndContService.getNextOrderNum(menuId,
										"" + order, status, status_type));
					} else if ("top".equals(tag.trim())) {
						orderStr = ""
								+ (relaMenuAndContService.getMinOrderNum(menuId, status, status_type) - 1);
					} else if ("bottom".equals(tag.trim())) {
						orderStr = ""
								+ (relaMenuAndContService.getMaxOrderNum(menuId, status, status_type) + 1);
					}

					logger.info("---------orderStr:" + orderStr);
					if (!orderStr.equals(orderNum)) {// 如果已经是第一个/最后一个则直接返回
						RelaMenuAndCont tempRelaMenuAndCont = relaMenuAndContService
								.find(menuId, orderStr);
						if (tempRelaMenuAndCont != null) {
							tempRelaMenuAndCont.setOrder_num(orderNum);
							relaMenuAndContService.update(tempRelaMenuAndCont);
						}

						relaMenuAndCont.setOrder_num(orderStr);
						relaMenuAndContService.update(relaMenuAndCont);
					}
					pw.print("{success:true, errors:{}}");
					
					//最后再处理下重复的orderNum
					relaMenuAndContService.deleteDuplicateOrderNum(menuId);
				} else {
					pw.print("{success:false, errors:{}}");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			pw.print("{success:false, errors:{}}");
		}

		return null;

	}

	@RequestMapping("is_url_used_rela_menu_cont.do")
	public ModelAndView modifyRelaMenuAndContIsUrlUsedHandler(
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String ids = req.getParameter("ids");
		logger.info("---------ids:" + ids);
		String comboBox = req.getParameter("comboBox");
		logger.info("---------comboBox:" + comboBox);
		if ("-1".equals(comboBox))
			comboBox = "";
		String menuId = req.getParameter("menuId");
		logger.info("---------menuId:" + menuId);
		Boolean bool = false;
		if (menuId == null || "".equals(menuId.trim())) {
			bool = false;
		} else {
			String[] idsArray = ids.split(",");
			for (int i = 0; i < idsArray.length; i++) {
				RelaMenuAndCont relaMenuAndCont = relaMenuAndContService
						.findById(idsArray[i], menuId);
				if (relaMenuAndCont != null) {
					relaMenuAndCont.setIs_url_used(comboBox);
					relaMenuAndContService.update(relaMenuAndCont);
					// Menu menu = menuService.findById(menuId);
					// menu.setVersion((dateformat.format(new
					// Date())).toString());
					// menuService.update(menu);
					bool = true;
				} else {
					bool = false;
					break;
				}
			}
		}
		if (bool == true) {
			pw.print("{success:true, errors:{}}");
		} else {
			pw.print("{success:false, errors:{}}");
		}
		return null;
	}

	@RequestMapping("preview.do")
	public ModelAndView previewRelaMenuAndContHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String result = "";
		try {
			String menuId;
			menuId = req.getParameter("menuId");
			if(StringUtils.isEmpty(menuId)) {
				menuId = "-1";
			}
			logger.info("---------menuId:"+menuId);
			
			int start = 0;logger.info("---------start:"+start);
			int limit = 100;logger.info("---------limit:"+limit);
			int status = InitManager.Defaut_Unselected_ID;
			String avalue = req.getParameter("status");
			try {status = Integer.parseInt(avalue);} catch (Exception e) {}
			logger.info("---------status:" + status);
			
			avalue = req.getParameter("status_type");
			int status_type = 0;
			try {status_type = Integer.parseInt(avalue);} catch (Exception e) {}
			logger.info("---------status_type:" + status_type);
			
			Menu menu = menuService.findById(menuId);
			if ("2".equals(menu.getIs_shortcut())){//【栏目链接】
				menuId = menu.getShortcut_contid();
				logger.info("---------Shortcut_contid:"+menuId);
			}
			
			List<RelaMenuAndCont> relaMenuAndContList = relaMenuAndContService.findAllForMAR(start, start+limit, menuId, status, status_type);
			String ids = "";
			for(RelaMenuAndCont relaMenuAndCont: relaMenuAndContList){
				ids += relaMenuAndCont.getC_id() + ",";
			}
			if(ids.length()>0) {
				ids = ids.substring(0, ids.length()-1);
			}

			Map<String, Img> imgMap = new HashMap<String, Img>();
			List<Img> imgList = imgService.findByIds(ids);
			for(Img img: imgList){
				imgMap.put(img.getTarget_id(), img);
			}
			Map<String, Cont> contMap = new HashMap<String, Cont>();
			List<Cont> contList = contService.findAllInIds(ids);
			for(Cont cont: contList){
				contMap.put(cont.getId(), cont);
			}

			List<RelaObj> relaObjList = new ArrayList<RelaObj>();
			for(RelaMenuAndCont relaMenuAndCont: relaMenuAndContList){
				Img img = imgMap.get(relaMenuAndCont.getC_id());
				RelaObj relaObj = new RelaObj();
				relaObj.setIs_url_used(0);//默认为0竖图
				if (img != null){
					relaObj.setPortraitUrl(img.getUrl_little());
					relaObj.setLandscapeUrl(img.getUrl());
					relaObj.setIconUrl(img.getUrl_icon());
					relaObj.setUrl_4_squares(img.getUrl_4_squares());
					relaObj.setUrl_6_squares(img.getUrl_6_squares());
					if (!StringTool.isEmpty(img.getIs_url_used())){
						relaObj.setIs_url_used(Integer.parseInt(img.getIs_url_used()));
					}
				}
				if (relaMenuAndCont.getIs_url_used() != null){
					relaObj.setIs_url_used(Integer.parseInt(relaMenuAndCont.getIs_url_used()));
				}

				relaObj.setId(Long.parseLong(relaMenuAndCont.getC_id()));
				relaObjList.add(relaObj);
			}
			String styleList = Readable.generateStyle(relaObjList, ReadRelaObj.class, "");//与协议共用，稍有改动
			for (RelaObj relaObj : relaObjList){
				String src = "";
				switch(relaObj.getIs_url_used()){
				case 0: src = relaObj.getPortraitUrl();break;
				case 1: src = relaObj.getLandscapeUrl();break;
				case 2: src = relaObj.getIconUrl();break;
				case 3: src = relaObj.getUrl_4_squares();break;
				case 4: src = relaObj.getUrl_6_squares();break;
				}
				if (StringTool.isEmpty(src)){
					styleList = styleList.replace("{"+relaObj.getId()+"}", "empty.JPG");//管理后台目录下的空白图片
				}
				else{
					styleList = styleList.replace("{"+relaObj.getId()+"}", InitManager.combineRootHttpPath(src));//正式协议返回的图片地址
				}
				Cont cont = contMap.get(""+relaObj.getId());
				String contDesc = "不存在";
				if (cont != null){
					contDesc = "id="+cont.getId()+",name="+cont.getName()+",type="+cont.getType()+",status="+cont.getStatus()+",Is_url_used="+relaObj.getIs_url_used();
				}
				styleList = styleList.replace("{title"+relaObj.getId()+"}", "资产:"+contDesc);
			}
			/*
			默认小方格宽和高相等，像素为300，设定为单位1，其他图的宽和高是小方图的固定倍数.
			1，小方图：宽*高=1*1
			2，竖图：宽*高=1.5*2
			3，横图：宽*高=2*1
			4，四方图：宽*高=2*2
			5，六方图：宽*高=3*2
			 */
			int blockSize = 150;//默认小方格的大小，宽和高大小相等，竖图宽为小方格的1.5倍，其他都是整数倍
			styleList = styleList
					.replace("{colspan1x1}", ""+blockSize*1)//小方格图
					.replace("{colspan1x2}", ""+(blockSize*3/2))//竖图： 宽为小方格的1.5倍
					.replace("{colspan2x1}", ""+blockSize*2)//横图
					.replace("{colspan2x2}", ""+blockSize*2)//四格图
					.replace("{colspan3x3}", ""+blockSize*3)//六格图
					.replace("{rowspan1}", ""+blockSize*1)//格子合并：1行
					.replace("{rowspan2}", ""+blockSize*2)//格子合并：2行
					.replace(" rowspan='1'", "")//去掉非合并格子：1行
					.replace(" colspan='1'", "")//去掉非合并格子：1列
					;
            styleList = styleList.replace("{0}", "app/resources/images/null.JPG").replace("{title0}", "未编排资产");
			result = "{success:true, cells:\""+styleList+"\"}";
		} catch (Exception e) {
			result = "{success:false,cells:''}";
			e.printStackTrace();
		}
		finally{
			
		}
		logger.info("---------result"+result);
		pw.print(result);
		return null;
	}

	@RequestMapping("delete_rela_menu_cont.do")
	public ModelAndView deleteRelaMenuAndContHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String menuId = req.getParameter("menuId");
		if (menuId == null) {
			menuId = "";
		}

		String ids = req.getParameter("ids");
		if (ids == null) {
			ids = "";
		}
		ids = ids.trim();
		logger.info("---------ids:" + ids);

		boolean result = relaMenuAndContService.deleteRelaMenuAndCont(menuId,
				ids);

		if (result) {
			// Menu menu = menuService.findById(menuId);
			// menu.setVersion((dateformat.format(new Date())).toString());
			// menuService.update(menu);//解绑不更新version
			pw.print("{success:true, errors:{}}");
		} else {
			pw.print("{success:false, errors:{}}");
		}

		return null;

	}
	@RequestMapping("lock_rela_menu_cont.do")
	public ModelAndView lockRelaMenuAndContHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		logger.info("---------here:");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String menuId = req.getParameter("menuId");
		if (menuId == null) {
			menuId = "";
		}

		String ids = req.getParameter("ids");
		if (ids == null) {
			ids = "";
		}
		ids = ids.trim();
		logger.info("---------ids:" + ids);
		String locked = req.getParameter("locked");
		if (!"1".equals(locked)) {
			locked = "0";
		}
		
		boolean result = relaMenuAndContService.lockRelaMenuAndCont(menuId,ids,locked);

		if (result) {
			pw.print("{success:true, errors:{}}");
			logger.info("---------here:{success:true, errors:{}}");
		} else {
			pw.print("{success:false, errors:{}}");
			logger.info("---------here:{success:false, errors:{}}");
		}

		return null;

	}

	private String createKey(String key) {
		if (key == null) {
			key = "0";
		}
		String result = key;

		int intKey = Integer.parseInt(key);
		if (intKey < 0) {
			intKey = -intKey;
			result = "key_" + intKey;
			;
		}

		return result;
	}
}
