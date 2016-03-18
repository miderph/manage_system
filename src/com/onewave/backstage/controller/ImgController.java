package com.onewave.backstage.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.ImageUtils;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.StringTool;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.onewave.backstage.model.Img;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ImgService;
import com.onewave.backstage.util.BmUtil;

@Controller("imgController")
@RequestMapping("/img/*")
public class ImgController extends MultiActionController {
	public static Logger logger = Logger.getLogger(ImgController.class);
	public static final java.text.SimpleDateFormat sdf_yyyyMMddHHmmss = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	@Qualifier("imgService")
	private ImgService imgService;
	@Autowired
	@Qualifier("contService")
	private ContService contService;

//	@RequestMapping("query_img.do")
//	public ModelAndView queryImgHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception
//    {return null;}

	@RequestMapping("img_delete.do")
	public ModelAndView deleteImgHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		String imgUrl = "";
		String date_str = req.getParameter("date");
		String obj_id = req.getParameter("obj_id");
		String tag = req.getParameter("tag");
		logger.info("obj_id="+obj_id+",tag="+tag+",date_str="+date_str);

		if(tag==null) {
			tag = "";
		}
		if ("delete".equals(tag))//delete
		{
			Img img = imgService.findById(obj_id);
			imgService.deleteById(obj_id);
			try {

				if (img!=null&&!img.getUrl().startsWith("http://")) {
					imgUrl = img.getUrl();
					String fileUrl = InitManager.getRootLocalPath() + imgUrl;

					File imgFile = new File(fileUrl);
					File srcFile = new File(imgFile.getAbsolutePath().replace(".", "_ori."));
					if (imgFile.exists()) {
						imgFile.delete();
					}
					if (srcFile.exists()) {
						srcFile.delete();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			logger.info("{success:true,message:'删除成功',img_url:'',img_url_show:'',img_id:'"+obj_id+"'}");
			pw.println("{success:true,message:'删除成功',img_url:'',img_url_show:'',img_id:'"+obj_id+"'}");

			return null;
		}
		logger.info("{success:true,message:'删除失败',img_url:'',img_url_show:'',img_id:'"+obj_id+"'}");
		pw.println("{success:true,message:'删除失败',img_url:'',img_url_show:''}");

		return null;
	}

	@RequestMapping("img_updatelocked.do")
	public ModelAndView updateLockedImgHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		String targetId = req.getParameter("targetId");
		String useType = req.getParameter("useType");
		String locked = req.getParameter("locked");
		boolean result = imgService.updatelocked(targetId, useType, locked);
		if(result==true){
			pw.println("{success:true,message:'操作成功'}");
		}else{
			pw.println("{success:false,message:'操作失败'}");
		}
		return null;
	}
	@RequestMapping("query_by_cid.do")
	public void queryImgByCidHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "查询图片失败";
		JSONObject imgData = new JSONObject();
		try {
			String cid = req.getParameter("c_id");
			String user_type = req.getParameter("type");
			if (StringUtils.isBlank(user_type)){
				user_type = "1";
			}
			List<Img> imgList = imgService.findAll(cid, user_type);

			if(imgList!=null && imgList.size()>0) {
				Img img = imgList.get(0);

				imgData.put("c_img_id", img.getId());
				imgData.put("c_img_rec_postion", StringTool.null2Empty(img.getIs_url_used()));
				imgData.put("c_img_plat_group", StringTool.null2Empty(img.getPlatgroup_id()));
				imgData.put("c_img_intro", StringTool.null2Empty(img.getIntro()));
				imgData.put("c_img_locked",StringTool.null2Empty( img.getLocked()));
				imgData.put("c_img_url", StringTool.null2Empty(BmUtil.createImgUrl(img.getUrl())));
				imgData.put("c_img_little_url", StringTool.null2Empty(BmUtil.createImgUrl(img.getUrl_little())));
				imgData.put("c_img_icon_url", StringTool.null2Empty(BmUtil.createImgUrl(img.getUrl_icon())));
				imgData.put("c_img_4_squares_url", StringTool.null2Empty(BmUtil.createImgUrl(img.getUrl_4_squares())));
				imgData.put("c_img_6_squares_url", StringTool.null2Empty(BmUtil.createImgUrl(img.getUrl_6_squares())));
				imgData.put("c_img_active_time", StringTool.null2Empty(BmUtil.formatDate(img.getActive_time())));
				imgData.put("c_img_deactive_time", StringTool.null2Empty(BmUtil.formatDate(img.getDeactive_time())));

				issuc = true;
			} else {
				imgData.put("c_img_id", "-1");
				msg = "没有图片";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (issuc) {
				msg = "查询图片成功";
			}

			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("imgData", imgData);
			rootJson.put("msg", msg);

			logger.info("--:rootJson="+rootJson);
			resp.getWriter().print(rootJson);
		}
	}
	public boolean saveOrUpdateImg(HttpServletRequest req, String c_id, String provider_id, String use_type) {
		boolean result = false;
		logger.info("--:here");

		try {
			String c_img_id = req.getParameter("c_img_id");
			boolean isSave = false;
			if("-1".equals(c_img_id.trim())) isSave = true;

			Img img = null;
			if(isSave) {
				img = new Img();
			} else {
				List<Img> imgList = imgService.findAll(c_id, use_type);
				if(imgList!=null && imgList.size()>0) {
                    if (imgList.size() > 1){//删除多余的图片,留下最新的一条//推荐图只能有一条记录//截图可以有多条
                    	while (imgList.size() > 1){
                    		imgService.delete(imgList.get(0));
                    	}
                    }
					img = imgList.get(0);
				}
			}

			if(img != null) {
				String used_url = req.getParameter("c_img_rec_postion");
				logger.info("-----iyadi-----used_url: " + used_url);
				//used_url = used_url == null ? "0" : used_url;
				if (StringUtils.isNotBlank(used_url)){//当used_url为空时，取上传的图为默认图
					img.setIs_url_used(used_url);
				}
				
				int contTypeId = -1;
				try{
					contTypeId = Integer.parseInt(req.getParameter("c_type"));//资产类型
				}catch(Exception e){
					logger.error(e);
					contTypeId = -1;
				}
				downloadImg(req, img, c_id, contTypeId!=13);//资产类型为广告的图不缩放

				img.setTarget_id(c_id);
				img.setUse_type(use_type);//用途类型: 0 栏目, 1 内容, 2应用截图/商品缩略图
				img.setLocked(req.getParameter("c_img_locked"));
				img.setPlatgroup_id(req.getParameter("c_img_plat_group"));
				img.setProvider_id(provider_id);
				img.setIntro(req.getParameter("c_img_intro"));

				img.setActive_time(BmUtil.parseDate(req.getParameter("c_img_active_time")));
				img.setDeactive_time(BmUtil.parseDate(req.getParameter("c_img_deactive_time")));
			} else {
				return result;
			}

			if(isSave) {
				result = imgService.save(img);
			} else {
				result = imgService.update(img);
			}
		} catch(Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}
	public void downloadImg(HttpServletRequest req, Img img, String c_id) throws IllegalStateException, IOException {
		downloadImg(req, img, c_id, true);
	}
	@RequestMapping("img_file_upload.do")
	public ModelAndView imgFileUploadHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		logger.info("--:");

		resp.setContentType("application/json; charset=utf-8");
		PrintWriter pw = resp.getWriter();

		String tag = req.getParameter("tag");
		String obj_id = req.getParameter("obj_id");
		String result = "{success:true,message:'删除失败',img_url:'',img_url_show:'',img_id:''}";
		try {
			if(tag==null) {
				tag = "";
			}
			if ("delete".equals(tag))//delete
			{
				imgService.deleteById(obj_id);
				
				logger.info("{success:true,message:'删除成功',img_url:'',img_url_show:'',img_id:'"+obj_id+"'}");
				pw.println("{success:true,message:'删除成功',img_url:'',img_url_show:''}");
				return null;
			}
		} catch (Exception e) {
			logger.error(e);
			return null;
		}

		result = "{success:false,message:'上传失败',img_url:'',img_url_show:''}";;
		try {
			req = BmUtil.resolveMultipart(req);

			String date_str = req.getParameter("date");
			String locked = req.getParameter("locked");
			String imgUrl = "";
			int contTypeId = -1;
			try{
				contTypeId = Integer.parseInt(req.getParameter("contTypeId"));
			}catch(Exception e){
				logger.error(e);
				contTypeId = -1;
			}
			Cont cont = contService.findById(obj_id);

			//竖图大小：400x605
			//横图大小：605x300
			//小方图大小：300x300
			//四方图大小：600x600
			//六方图大小：900x600
			//在这判断，当资产类型为【视频、应用和商品】时，等比例缩放，应用截图按横图比例处理//13 是广告//加入缩放contTypeId可以缩放
			Img img = new Img();
			downloadImg(req, img, obj_id, contTypeId!=13);
			if ("snapshot".equals(tag)){//截图
				imgUrl = img.getUrl();
				img.setPlatgroup_id("1");
				img.setProvider_id("25");
				img.setTarget_id(obj_id);
				img.setUse_type("2");//截图
				java.util.Calendar startDate = java.util.Calendar.getInstance();startDate.add(java.util.Calendar.YEAR, -10);
				java.util.Calendar endDate = java.util.Calendar.getInstance();endDate.add(java.util.Calendar.YEAR, 50);
				img.setActive_time(startDate.getTime());
				img.setDeactive_time(endDate.getTime());

				List<Img> anotherSnapshotImgs = imgService.findAll(obj_id);
				if (anotherSnapshotImgs != null && anotherSnapshotImgs.size() > 0){
					img.setLocked(anotherSnapshotImgs.get(0).getLocked());				
				}
				else{
					img.setLocked(locked);				
				}

				String app_img_id = imgService.saveAndReturnId(img);
			}else if ("c_img_file".equals(tag)){//横图
				imgUrl = img.getUrl();
			}else if ("c_img_little_file".equals(tag)){//竖图
				imgUrl = img.getUrl_little();
			}else if ("c_img_icon_file".equals(tag)){//小方图
				imgUrl = img.getUrl_icon();
			}else if ("c_img_4_squares_file".equals(tag)){//四方图
				imgUrl = img.getUrl_4_squares();
			}else if ("c_img_6_squares_file".equals(tag)){//六方图
				imgUrl = img.getUrl_6_squares();
			}

			String httpUrl = InitManager.getRootHttpPath()+imgUrl;
			result = "{success:true,message:'上传成功',img_url:'"+imgUrl+"',img_url_show:'"+httpUrl+"'}";
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("result="+result);
		pw.println(result);

		return null;
	}
	public void downloadImg(HttpServletRequest req, Img img, String c_id, boolean needScale) throws IllegalStateException, IOException {
		logger.info("--:here");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				req.getSession().getServletContext());

		//判断req是否有文件上传,即多部分请求
		if(multipartResolver.isMultipart(req)) {
			//转换成多部分req
			MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest)req;
			//取得req中的所有文件名
			Iterator<String> iter = multiReq.getFileNames();
			while(iter.hasNext()){
				//取得上传文件 
				MultipartFile multiFile = multiReq.getFile(iter.next());
				if(null != multiFile) {
					//取得当前上传文件的文件名称
					String fileName = multiFile.getOriginalFilename();
					String tag = multiFile.getName();

					if(null != fileName && !"".equals(fileName.trim())) {
						String extName = fileName.substring(fileName.lastIndexOf("."));
						String imgName = "pic/" + c_id + "_" + tag + sdf_yyyyMMddHHmmss.format(java.util.Calendar.getInstance().getTime()) + extName;

						String filePath = InitManager.getRootLocalPath() + imgName;
						File file = new File(filePath);
						if (!file.exists()){
							File parent = file.getParentFile();
							if (!parent.exists())
								parent.mkdirs();
						}
						multiFile.transferTo(file);

						//竖图大小：400x605
						//横图大小：605x300
						//小方图大小：300x300
						//四方图大小：600x600
						//六方图大小：900x600
						if("c_img_little_file".equals(tag)) {
							if (needScale){
								imgName = "pic/" + ImageUtils.scaleImage(file, 400);//2014-05-21 //scale but not stretch           
							}
							img.setUrl_little(imgName);
							if (StringUtils.isBlank(img.getIs_url_used())){
								img.setIs_url_used("0");
							}
						} else if("c_img_file".equals(tag)) {
							if (needScale){
								imgName = "pic/" + ImageUtils.scaleImage(file, 605);//2014-05-21 //scale but not stretch           
							}
							img.setUrl(imgName);
							if (StringUtils.isBlank(img.getIs_url_used())){
								img.setIs_url_used("1");
							}
						} else if("c_img_icon_file".equals(tag)) {
							if (needScale){
								imgName = "pic/" + ImageUtils.scaleImage(file, 300);//2014-05-21 //scale but not stretch           
							}
							img.setUrl_icon(imgName);
							if (StringUtils.isBlank(img.getIs_url_used())){
								img.setIs_url_used("2");
							}
						} else if("c_img_4_squares_file".equals(tag)) {
							//imgName = scaleImage(file, 600);//2014-05-21 //scale but not stretch
							//img.setUrl_4_squares(imgName);
							if (needScale){
								int[] imgWidths = new int[]{600,300};//上传四方图同时生成小方图
								String[] imgNames = ImageUtils.scaleImage(file, imgWidths);//2014-05-21 //scale but not stretch            
								img.setUrl_4_squares("pic/" + imgNames[0]);
								img.setUrl_icon("pic/" + imgNames[1]);
							}
							else{
								img.setUrl_4_squares("pic/" + imgName);
							}
							if (StringUtils.isBlank(img.getIs_url_used())){
								img.setIs_url_used("3");
							}
						} else if("c_img_6_squares_file".equals(tag)) {
							if (needScale){
								imgName = "pic/" + ImageUtils.scaleImage(file, 900);//2014-05-21 //scale but not stretch           
							}
							img.setUrl_6_squares(imgName);
							if (StringUtils.isBlank(img.getIs_url_used())){
								img.setIs_url_used("4");
							}
						} else if("snapshot".equals(tag)) {
							if (false){//截图不缩放
								imgName = "pic/" + ImageUtils.scaleImage(file, 605);//2014-05-21 //scale but not stretch           
							}
							img.setUrl(imgName);
							img.setUse_type("2");//截图
							img.setIs_url_used("1");
						}
					}
				}
			}
		}
	}
}
