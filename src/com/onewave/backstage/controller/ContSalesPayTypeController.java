package com.onewave.backstage.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.onewave.backstage.model.ContentSalesBean.PayType;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.service.ContSalesPayTypeService;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.backstage.util.BmUtil;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller("contSalesPayTypeController")
@RequestMapping("/paytypes/*")
public class ContSalesPayTypeController extends MultiActionController {
   @Autowired
   @Qualifier("contSalesPayTypeService")
   private ContSalesPayTypeService contSalesPayTypeService;
   @Autowired
   @Qualifier("statusDictService")
   private StatusDictService statusDictService;
   @Autowired
   @Qualifier("imgController")
   private ImgController imgController;

   @RequestMapping("queryStatus.do")
   public ModelAndView queryPayTypeStatusHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      
      logger.info("--:");
      
      resp.setContentType("application/json; charset=UTF-8");
      PrintWriter pw = resp.getWriter();
      
      List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_SALES_PAY_TYPE", "PAY_TYPE");
      
      JSONArray jsonArray = new JSONArray();
      JSONObject groupJson=new JSONObject();
      
      groupJson.put("contTypeId", InitManager.Defaut_Unselected_ID);
      groupJson.put("contTypeName", "请选择");
      jsonArray.add(groupJson);
      int i=1;
      for(StatusDict statusDict: statusDictList){
         groupJson.put("contTypeId", statusDict.getStatus());
         groupJson.put("contTypeName", StringTool.null2Empty(statusDict.getDescription()));
         jsonArray.add(groupJson);
      }
      
      JSONObject json = new JSONObject();
      
      json.put("results", statusDictList.size());
      json.put("contType_data", jsonArray);
      logger.info(json);
      pw.print(json);
      
      return null;
   }

	@RequestMapping("query_all.do")
	public ModelAndView queryPayTypesHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		logger.info("--:here");

		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		List<PayType> contPayTypeList = this.contSalesPayTypeService.findAll();

		JSONArray jsonArray = new JSONArray();
		JSONObject contentJson = new JSONObject();
		contentJson.put("id", InitManager.Defaut_Unselected_ID);
		contentJson.put("description", "请选择");
		jsonArray.add(contentJson);
		for (PayType cont : contPayTypeList) {
			contentJson.put("id", cont.getId());
			//contentJson.put("pay_type", cont.getPay_type());
			//contentJson.put("service_hotline", cont.getService_hotline());
			//contentJson.put("has_qrcode", cont.getHas_qrcode());
			contentJson.put("description", cont.getDescription()+(cont.getService_hotline()==null?"":cont.getService_hotline()));

			jsonArray.add(contentJson);
		}

		JSONObject json = new JSONObject();
		json.put("results", contPayTypeList.size());
		json.put("data", jsonArray);

		logger.info("json===================" + json.toString());
		pw.print(json);

		return null;
	}

	@RequestMapping("query.do")
	public ModelAndView query(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		Object adminObj = req.getSession().getAttribute("user");
		Operator admin = null;

		if (adminObj != null) {
			admin = (Operator) adminObj;
		}

		String name = null;
		if (admin != null) {
			name = admin.getName();
		}

		if (name == null) {
			return null;
		}
		String start = req.getParameter("start");
		String limit = req.getParameter("limit");
		
		List<PayType> payTypeList= contSalesPayTypeService.findAll();
		logger.info("PayType===================" + payTypeList.size());
		
      List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_SALES_PAY_TYPE", "PAY_TYPE");
      Map<String, String> statusDictMap = new HashMap<String, String>();
      for(StatusDict statusDict: statusDictList){
         statusDictMap.put(statusDict.getStatus(), statusDict.getDescription());
      }
		
		
		JSONArray jsonArray = new JSONArray();
		JSONObject itemJson = new JSONObject();
		for (PayType payType : payTypeList) {
			itemJson.put("id", payType.getId());
			itemJson.put("name", StringTool.null2Empty(payType.getName()));
			itemJson.put("pay_type", StringTool.null2Empty(payType.getPay_type()));
			itemJson.put("pay_type_name", StringTool.null2Empty(statusDictMap.get(""+payType.getPay_type())));
			itemJson.put("has_qrcode", StringTool.null2Empty(payType.getHas_qrcode()));
			itemJson.put("has_qrcode_name", "1".equals(payType.getHas_qrcode())?"是":"否");
			itemJson.put("service_hotline", StringTool.null2Empty(payType.getService_hotline()));
			itemJson.put("description", StringTool.null2Empty(payType.getDescription()));

			itemJson.put("icon_url", BmUtil.createImgUrl(payType.getIcon_url()));
			itemJson.put("c_img_icon_url", BmUtil.createImgUrl(payType.getIcon_url()));
			
			jsonArray.add(itemJson);
		}
		JSONObject json = new JSONObject();
		json.put("results", payTypeList.size());
		json.put("datastr", jsonArray);
		logger.info("json===================" + json.toString());
		pw.print(json);
		return null;
	}
	@RequestMapping("save.do")
	public ModelAndView save(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		boolean issuc = false;
		String msg = "保存失败";
		try {
			req = BmUtil.resolveMultipart(req);

			String has_qrcode = req.getParameter("has_qrcode");
			logger.info("json===================has_qrcode="+has_qrcode);

			PayType paytype = new PayType();
			int id = -1;
			try{
				id = Integer.parseInt(req.getParameter("id"));
			}catch(Exception E){}
			if (id != -1){
				paytype.setId(""+id);
				if (contSalesPayTypeService.findById(""+id) != null){
					msg = "id已存在！id="+id;
					throw new Exception(msg);
				}
			}

			paytype.setName(req.getParameter("name"));
			paytype.setPay_type(req.getParameter("pay_type"));
			paytype.setHas_qrcode("1".equals(has_qrcode)?"1":"0");
			paytype.setService_hotline(req.getParameter("service_hotline"));
			paytype.setDescription(req.getParameter("description"));

			Img img = new Img();
			img.setUrl_icon(paytype.getIcon_url());
			imgController.downloadImg(req, img, paytype.getId(), false);
			paytype.setIcon_url(img.getUrl_icon());

			issuc = contSalesPayTypeService.save(paytype);
		} catch (Exception E) {
			logger.error(E.getMessage(),E);
		} finally {
			if (issuc) {
				msg = "保存成功";
			}
			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", msg);

			logger.info(rootJson);
			resp.getWriter().print(rootJson);
		}
      logger.info("json===================");

		return null;
	}

	@RequestMapping("update.do")
	public ModelAndView update(HttpServletRequest req, HttpServletResponse resp)
	      throws Exception {

	   logger.info("--:");
	   resp.setContentType("application/json; charset=UTF-8");
	   PrintWriter pw = resp.getWriter();

	   boolean issuc = false;
	   String msg = "保存失败";
	   try {
		   req = BmUtil.resolveMultipart(req);

		   String id = req.getParameter("id");
		   String has_qrcode = req.getParameter("has_qrcode");
		   logger.info("json===================id="+id+",has_qrcode="+has_qrcode);

		   PayType paytype = contSalesPayTypeService.findById(id);
		   if (paytype != null){
			   paytype.setName(req.getParameter("name"));
			   paytype.setPay_type(req.getParameter("pay_type"));
			   paytype.setHas_qrcode("1".equals(has_qrcode)?"1":"0");
			   paytype.setService_hotline(req.getParameter("service_hotline"));
			   paytype.setDescription(req.getParameter("description"));
			   
			   Img img = new Img();
			   img.setUrl_icon(paytype.getIcon_url());
			   imgController.downloadImg(req, img, paytype.getId(), false);
			   paytype.setIcon_url(img.getUrl_icon());

			   issuc = contSalesPayTypeService.update(paytype);
		   }
		   else{

		   }
	   } catch (Exception E) {
		   logger.error(E.getMessage(),E);
	   } finally {
		   if (issuc) {
			   msg = "保存成功";
		   }
		   JSONObject rootJson = new JSONObject();
		   rootJson.put("success", true);
		   rootJson.put("issuc", issuc);
		   rootJson.put("msg", msg);

		   logger.info(rootJson);
		   resp.getWriter().print(rootJson);
	   }
	   return null;

	}

	@RequestMapping("delete.do")
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp)
         throws Exception {

      logger.info("--:");
      resp.setContentType("application/json; charset=UTF-8");
      PrintWriter pw = resp.getWriter();
      boolean issuc = false;
      String msg = "删除失败";
      try {
         PayType paytype = new PayType();
         paytype.setId(req.getParameter("id"));
         boolean ret = contSalesPayTypeService.isUsing(paytype.getId());
         if (ret){
        	 msg = "删除失败！此支付方式已在使用，不能删除！";
         }
         else {
        	 issuc = contSalesPayTypeService.delete(paytype.getId());
         }         
      } catch (Exception E) {
    	  logger.error(E.getMessage(),E);
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

      return null;
   }
}
