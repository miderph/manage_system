package com.onewave.backstage.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContentVideoFileBean;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoFileService;
import com.onewave.backstage.service.StatusDictService;

@Controller("contVideoFileController")
@RequestMapping("/videofiles/*")
public class ContVideoFileController extends MultiActionController {
   @Autowired
   @Qualifier("contService")
   private ContService contService;
   @Autowired
   @Qualifier("contVideoFileService")
   private ContVideoFileService contVideoFileService;
   @Autowired
   @Qualifier("statusDictService")
   private StatusDictService statusDictService;


	@RequestMapping("queryVideoDistinct.do")
   public ModelAndView queryDistinctHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      
      logger.info("--:");
      
      resp.setContentType("application/json; charset=UTF-8");
      PrintWriter pw = resp.getWriter();
      
      List<StatusDict> statusDictList = this.statusDictService.queryStatusDict("ZL_CONT_VIDEO_FILE", "RATE_TAG");
      
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
//
//	@RequestMapping("delete.do")
//	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp)
//			throws Exception {
//
//		logger.info("--:");
//		resp.setContentType("application/json; charset=UTF-8");
//		PrintWriter pw = resp.getWriter();
//		boolean bflag = false;
//		String retMsg = "删除失败";
//		try {
//         String fileid = req.getParameter("videofile_id");
//         logger.info("json===================fileid="+fileid);
//
//         bflag = contVideoFileService.delete(fileid);
//
//		} catch (Exception E) {
//			bflag = false;
//			E.printStackTrace();
//		} finally {
//			if (bflag) {
//				pw.print("{success:true, msg:'删除成功'}");
//            logger.info("{success:true, msg:'删除成功'}");
//			} else {
//				pw.print("{success:false, msg:'" + retMsg + "'}");
//				logger.info("{success:false, msg:'" + retMsg + "'}");
//			}
//		}
//
//		return null;
//	}
	
	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		
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
			return ;
		}
		
		int firstResult = Integer.parseInt(req.getParameter("start"));
		int maxResults = Integer.parseInt(req.getParameter("limit"));
		String c_id = req.getParameter("c_id"); logger.info("c_id===================" + c_id);
		String c_name = req.getParameter("c_name"); logger.info("c_name===================" + c_name);
		
		int total = 0;
		JSONArray rootArr = new JSONArray();
		try {
			
			total = contVideoFileService.countAll(c_id);
			logger.info("total===================" + total);
			
			List<ContentVideoFileBean> videoFileList= contVideoFileService.findAll(firstResult, maxResults, c_id);
			
			JSONObject json = null;
			for(ContentVideoFileBean videoFile: videoFileList) {
				json = new JSONObject();
				
				json.put("vf_id", videoFile.getId());
				json.put("c_id", videoFile.getC_id());
				json.put("c_name", StringTool.null2Empty(c_name));
				json.put("bit_rate", StringTool.null2Empty(videoFile.getBit_rate()));
				json.put("order_num", StringTool.null2Empty(videoFile.getOrder_num()));
				json.put("play_url", StringTool.null2Empty(videoFile.getPlay_url()));
				json.put("rate_tag", StringTool.null2Empty(videoFile.getRate_tag()));
				json.put("rate_tag_eng", StringTool.null2Empty(videoFile.getRate_tag_eng()));
				json.put("provider_id", StringTool.null2Empty(videoFile.getProvider_id()));
				json.put("title", StringTool.null2Empty(videoFile.getTitle()));
				
				rootArr.add(json);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JSONObject root = new JSONObject();
			root.put("total", total);
			root.put("data", rootArr);
			logger.info("json===================" + root.toString());
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
			ContentVideoFileBean videoFile = new ContentVideoFileBean();
			videoFile.setId(null);
			updateContentVideoFile(videoFile, req);
			issuc = contVideoFileService.save(videoFile);
			
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
			
			String vf_id = req.getParameter("vf_id");
			ContentVideoFileBean videoFile = contVideoFileService.findById(vf_id);
			updateContentVideoFile(videoFile, req);
			
			issuc = contVideoFileService.update(videoFile);
			
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

			resp.getWriter().print(rootJson);
		}
	}
	
	@RequestMapping("delete.do")
	public void delHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "删除失败";
		try {
			
			issuc = contVideoFileService.delete(req.getParameter("vf_id"));
				
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

			resp.getWriter().print(rootJson);
		}
	}
	
	private void updateContentVideoFile(ContentVideoFileBean videoFile, HttpServletRequest req) {
		try {
			
			videoFile.setC_id(req.getParameter("c_id"));
			videoFile.setProvider_id(req.getParameter("provider_id"));
			videoFile.setOrder_num(req.getParameter("order_num"));
			videoFile.setBit_rate(req.getParameter("bit_rate"));
			videoFile.setRate_tag(req.getParameter("rate_tag"));
			videoFile.setRate_tag_eng(req.getParameter("rate_tag_eng"));
			videoFile.setPlay_url(req.getParameter("play_url"));
         videoFile.setTitle(req.getParameter("title"));
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
