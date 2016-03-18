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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.Channel;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.ChannelService;
import com.onewave.backstage.service.UserGroupService;
import com.onewave.backstage.util.BmUtil;

@Controller("contChannelController.java")
@RequestMapping("/contchannel/*")
public class ContChannelController extends MultiActionController {

	@Autowired
	@Qualifier("channelService")
	private ChannelService channelService;
	@Autowired
	@Qualifier("userGroupService")
	private UserGroupService userGroupService;

	@RequestMapping("query_all.do")
	public void queryAllChannel(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			Operator operator =(Operator) req.getSession().getAttribute("user");
			String type = req.getParameter("type");

			List<Channel> list = channelService.findAll(type);

//			JSONObject colJson = null;
//			colJson = new JSONObject();
//			colJson.put("s_id", InitManager.Defaut_Unselected_ID);
//			colJson.put("s_name", "请选择");
//			rootArr.add(colJson);

			if(list != null && list.size() > 0) {
				total = list.size();
				for(Channel channel: list) {
					if(channel == null) continue;

					rootArr.add(parserChannel(channel));
				}
			}

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
			List<Channel> list = channelService.findAll(firstResult, maxResults);
			if(list != null && list.size() > 0) {
				for(Channel channel: list) {
					if(channel == null) continue;

					rootArr.add(parserChannel(channel));
				}
			}

			total = channelService.countAll();

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

			Channel channel = new Channel();
			updateChannel(channel, req);
			//String id = channelService.saveAndReturnId(channel);

			issuc = channelService.save(channel);
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
            String id = req.getParameter("s_id");
            Channel oldChannel = channelService.findById(id);

            Channel newChannel = new Channel();
            newChannel.setId(id);
			updateChannel(newChannel, req);
            
			issuc = channelService.update(newChannel);
			if (issuc
					&& "apk".equals(oldChannel.getType())
					&& StringUtils.isNotBlank(oldChannel.getChannel())
					){
				String oldValue = ","+oldChannel.getChannel()+",";
				String newValue = newChannel.getChannel();
				if (!"apk".equals(newChannel.getType())){
					newValue = "";
				}
				if (StringUtils.isNotBlank(newValue)){
					newValue = ","+newValue+",";
				}
				
				if (!oldValue.equals(newValue)){//需要更新到测试组：渠道类型的
					List<UserGroup> groups = userGroupService.findAll("channel");
					for (UserGroup group : groups){
						if (StringUtils.isNotBlank(group.getRaw_value()) && (","+group.getIds_value()+",").indexOf(","+id+",")>=0){
							String temp = ","+group.getRaw_value()+",";
							temp = temp.replace(oldValue, newValue);
							if (temp.startsWith(",")){
								temp = temp.substring(1);
							}
							if (temp.endsWith(",")){
								temp = temp.substring(0, temp.length()-1);
							}
							group.setRaw_value(temp);
							userGroupService.update(group);
						}
					}
				}
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

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "删除失败";
		try {

			issuc = channelService.delete(req.getParameter("s_id"));

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

	private JSONObject parserChannel(Channel channel) {

		if(channel == null) return null;

		JSONObject colJson = new JSONObject();

		colJson.put("s_id", channel.getId());
		colJson.put("name", StringTool.null2Empty(channel.getName()));
		colJson.put("type", StringTool.null2Empty(channel.getType()));
		colJson.put("channel", StringTool.null2Empty(channel.getChannel()));
		//colJson.put("icon_url", BmUtil.createImgUrl(channel.getIcon_url()));
		//colJson.put("c_img_icon_url", BmUtil.createImgUrl(channel.getIcon_url()));
		colJson.put("intro", StringTool.null2Empty(channel.getIntro()));
		colJson.put("create_time", StringTool.null2Empty(BmUtil.formatDate(channel.getCreate_time())));
		colJson.put("modify_time", StringTool.null2Empty(BmUtil.formatDate(channel.getModify_time())));

		return colJson;
	}

	private void updateChannel(Channel channel, HttpServletRequest req) {

		try {
			channel.setName(req.getParameter("name"));
			channel.setType(req.getParameter("type"));
			channel.setChannel(req.getParameter("channel"));
			//channel.setIcon_url(req.getParameter("icon_url"));
			channel.setIntro(req.getParameter("intro"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
