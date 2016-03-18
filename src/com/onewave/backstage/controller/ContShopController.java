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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Shop;
import com.onewave.backstage.service.ShopService;
import com.onewave.backstage.util.BmUtil;

@Controller("shopController")
@RequestMapping("/contshop/*")
public class ContShopController extends MultiActionController {
	
	@Autowired
	@Qualifier("shopService")
	private ShopService shopService;
	@Autowired
	@Qualifier("imgController")
	private ImgController imgController;
	
	@RequestMapping("query_all.do")
	public void queryAllShop(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		try {
			Operator operator =(Operator) req.getSession().getAttribute("user");
			
			List<Shop> list = shopService.findAll();
			
			JSONObject colJson = null;
         colJson = new JSONObject();
         colJson.put("s_id", InitManager.Defaut_Unselected_ID);
         colJson.put("s_name", "请选择");
         rootArr.add(colJson);

			if(list != null && list.size() > 0) {
				for(Shop shop: list) {
					if(shop == null) continue;
					
					colJson = new JSONObject();
					
					colJson.put("s_id", shop.getId());
					colJson.put("s_name", shop.getName());
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
			List<Shop> list = shopService.findAll(firstResult, maxResults);
			if(list != null && list.size() > 0) {
				for(Shop shop: list) {
					if(shop == null) continue;
					
					rootArr.add(parserShop(shop));
				}
			}
			
			total = shopService.countAll();
			
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
			req = BmUtil.resolveMultipart(req);

			Shop shop = new Shop();
			updateShop(shop, req);
			String id = shopService.saveAndReturnId(shop);
			shop.setId(id);

         Img img = new Img();
         imgController.downloadImg(req, img, shop.getId());
         shop.setIcon_url(img.getUrl_icon());

         issuc = shopService.update(shop);
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
			req = BmUtil.resolveMultipart(req);
			
			Shop shop = new Shop();
			shop.setId(req.getParameter("s_id"));
			updateShop(shop, req);
			
         Img img = new Img();
         imgController.downloadImg(req, img, shop.getId());
         shop.setIcon_url(img.getUrl_icon());
         
			issuc = shopService.update(shop);
		   
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
			
			issuc = shopService.delete(req.getParameter("s_id"));

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

	private JSONObject parserShop(Shop shop) {
		
		if(shop == null) return null;
		
		JSONObject colJson = new JSONObject();
		
		colJson.put("s_id", shop.getId());
		colJson.put("name", StringTool.null2Empty(shop.getName()));
		colJson.put("credit", StringTool.null2Empty(shop.getCredit()));
		colJson.put("hot_info", StringTool.null2Empty(shop.getHot_info()));
		colJson.put("icon_url", StringTool.null2Empty(BmUtil.createImgUrl(shop.getIcon_url())));
		colJson.put("c_img_icon_url", StringTool.null2Empty(BmUtil.createImgUrl(shop.getIcon_url())));
		colJson.put("intro", StringTool.null2Empty(shop.getIntro()));
		colJson.put("create_time", BmUtil.formatDate(shop.getCreate_time()));
		colJson.put("modify_time", BmUtil.formatDate(shop.getModify_time()));
		
		return colJson;
	}
	
	private void updateShop(Shop shop, HttpServletRequest req) {
		
		try {
			shop.setName(req.getParameter("name"));
         shop.setCredit(req.getParameter("credit"));
         shop.setHot_info(req.getParameter("hot_info"));
         //shop.setIcon_url(req.getParameter("icon_url"));
         shop.setIntro(req.getParameter("intro"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
