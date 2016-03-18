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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContHotInfo;
import com.onewave.backstage.model.ContentVideoFileBean;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.service.ContHotInfoService;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoFileService;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.backstage.util.BmUtil;

@Controller("contHotInfoController")
@RequestMapping("/hotinfos/*")
public class ContHotInfoController extends MultiActionController {
	@Autowired
	@Qualifier("contHotInfoService")
	private ContHotInfoService contHotInfoService;

	@RequestMapping("query_hotinfo.do")
	public void queryHotInfoHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:here");
		resp.setContentType("application/json; charset=UTF-8");
		
		int firstResult = 0;
		try {
			firstResult = Integer.parseInt(req.getParameter("start"));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		int limit = 20;
		try {
			limit = Integer.parseInt(req.getParameter("limit"));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		logger.info("--------------firstResult:" + firstResult);
		int maxResults = firstResult + limit;
		logger.info("--------------maxResults:" + maxResults);

		String c_id = req.getParameter("c_id");logger.info("--------------c_id:" + c_id);
		String channel = req.getParameter("channel");logger.info("--------------channel:" + channel);
		
		JSONArray rootArr = new JSONArray();
		JSONObject json = null;
		long total = 0;
		try {
			total = contHotInfoService.countHotInfos(c_id, channel);
			List<ContHotInfo> contHotInfoList = contHotInfoService.findHotInfos(c_id, channel);
			for (ContHotInfo cont : contHotInfoList) {
				json = new JSONObject();

				json.put("id", cont.getId());
				json.put("c_id", cont.getC_id());
				json.put("type", StringTool.null2Empty(cont.getType()));
				json.put("channel", StringTool.null2Empty(cont.getChannel()));
				json.put("hot_info", StringTool.null2Empty(cont.getHot_info()));
				json.put("icon_url", StringTool.null2Empty(cont.getIcon_url()));
				json.put("description", StringTool.null2Empty(cont.getDescription()));
				json.put("create_time", StringTool.null2Empty(BmUtil.formatDate(cont.getCreate_time())));
				json.put("modify_time", StringTool.null2Empty(BmUtil.formatDate(cont.getModify_time())));

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
	
	@RequestMapping("save.do")
	public void saveHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "添加失败";
		try {
			ContHotInfo videoFile = new ContHotInfo();
			updateHotInfo(videoFile, req);
			videoFile.setId(null);

            if (checkExists(videoFile))
            	msg = "添加失败！渠道号【"+videoFile.getChannel()+"】的促销信息已存在！";
            else {
            	issuc = contHotInfoService.save(videoFile);
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

			resp.getWriter().print(rootJson);
		}
	}
    private boolean checkExists(ContHotInfo videoFile){
		boolean isExists = false;

		List<ContHotInfo> contHotInfoList = contHotInfoService.findHotInfos(videoFile.getC_id(), videoFile.getChannel());
        for (ContHotInfo contHotInfo : contHotInfoList){
        	if (StringUtils.equalsIgnoreCase(contHotInfo.getChannel(), videoFile.getChannel())
        			&& !StringUtils.equals(contHotInfo.getId(), videoFile.getId())){
        		isExists = true;
        		break;
        	}
        }
        return isExists;
    }
	@RequestMapping("update.do")
	public void updateHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "修改失败";
		try {
			
			String vf_id = req.getParameter("id");
			ContHotInfo videoFile = contHotInfoService.findById(vf_id);
			updateHotInfo(videoFile, req);

            if (checkExists(videoFile))
            	msg = "修改失败！渠道号【"+videoFile.getChannel()+"】的促销信息已存在！";
            else {
            	issuc = contHotInfoService.update(videoFile);
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

			resp.getWriter().print(rootJson);
		}
	}
	private void updateHotInfo(ContHotInfo videoFile, HttpServletRequest req){
		try {
			
			videoFile.setC_id(req.getParameter("c_id"));
			videoFile.setId(req.getParameter("id"));
			videoFile.setType(req.getParameter("type"));
			videoFile.setChannel(req.getParameter("channel"));
			videoFile.setHot_info(req.getParameter("hot_info"));
			videoFile.setDescription(req.getParameter("description"));
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("delete.do")
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		boolean bflag = false;
		String retMsg = "删除失败";
		try {
         String fileid = req.getParameter("id");
         logger.info("json===================id="+fileid);

         bflag = contHotInfoService.delete(fileid);

		} catch (Exception E) {
			bflag = false;
			E.printStackTrace();
		} finally {
			if (bflag) {
				pw.print("{success:true, msg:'删除成功'}");
            logger.info("{success:true, msg:'删除成功'}");
			} else {
				pw.print("{success:false, msg:'" + retMsg + "'}");
				logger.info("{success:false, msg:'" + retMsg + "'}");
			}
		}

		return null;
	}
}
