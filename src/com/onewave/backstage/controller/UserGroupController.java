package com.onewave.backstage.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.StringTool;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.Channel;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.ChannelService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.service.UserGroupService;
import com.onewave.backstage.util.BmUtil;

@Controller("userGroupController")
@RequestMapping("/usergroup/*")
public class UserGroupController extends MultiActionController {

	@Autowired
	@Qualifier("userGroupService")
	private UserGroupService userGroupService;

	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;

	@Autowired
	@Qualifier("channelService")
	private ChannelService channelService;

	@RequestMapping("query_all.do")
	public void queryAllUserGroup(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		String type = req.getParameter("type");
		logger.info("--:type="+type);

		JSONArray rootArr = new JSONArray();
		try {
			List<UserGroup> list = null;

			Operator operator =(Operator) req.getSession().getAttribute("user");
			list = roleService.queryUserGroupListWithAuth(operator, type, BmUtil.isAdminOperator(req));

			if(list != null && list.size() > 0) {
				JSONObject colJson = null;
				for(UserGroup userGroup: list) {
					if(userGroup == null) continue;

					colJson = new JSONObject();

					colJson.put("ug_id", userGroup.getId());
					colJson.put("ug_name", userGroup.getName());
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

		String tg_type = req.getParameter("tg_type");logger.info("--:tg_type="+tg_type);
		String tg_name = req.getParameter("tg_name");logger.info("--:tg_name="+tg_name);

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			Map<String, Channel> cpMap = getChannelMap();

			total = userGroupService.countAll(tg_type, tg_name);
			List<UserGroup> list = userGroupService.findAll(tg_type, tg_name, firstResult, maxResults);
			if(list != null && list.size() > 0) {
				for(UserGroup userGroup: list) {
					if(userGroup == null) continue;

					rootArr.add(parserUserGroup(userGroup, cpMap));
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

	@RequestMapping("save.do")
	public void saveHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "添加失败";
		try {
			Map<String, Channel> cpMap = getChannelMap();
			UserGroup userGroup = new UserGroup();
			updateUserGroup(userGroup, req, cpMap);
			issuc = userGroupService.save(userGroup);

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
			Map<String, Channel> cpMap = getChannelMap();
			UserGroup userGroup = new UserGroup();
			userGroup.setId(req.getParameter("ug_id"));
			updateUserGroup(userGroup, req, cpMap);
			issuc = userGroupService.update(userGroup);

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

	private Map<String, Channel> getChannelMap() {
		List<Channel> cpList = this.channelService.findAll();
		Map<String, Channel> cpMap = new HashMap<String, Channel>();
		for (Channel cp : cpList) {
			cpMap.put(cp.getId(), cp);
		}
		return cpMap;
	}

	@RequestMapping("del.do")
	public void delHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "删除失败";
		try {

			issuc = userGroupService.delete(req.getParameter("ug_id"));

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

	private JSONObject parserUserGroup(UserGroup userGroup, Map<String, Channel> cpMap) {

		if(userGroup == null) return null;

		JSONObject colJson = new JSONObject();

		colJson.put("ug_id", userGroup.getId());
		colJson.put("name", StringTool.null2Empty(userGroup.getName()));
		colJson.put("type", StringTool.null2Empty(userGroup.getType()));
		colJson.put("raw_value", StringTool.null2Empty(userGroup.getRaw_value()));
		colJson.put("ids_value", StringTool.null2Empty(userGroup.getIds_value()));
		colJson.put("intro", StringTool.null2Empty(userGroup.getDescription()));
		colJson.put("create_time", BmUtil.formatDate(userGroup.getCreate_time()));
		colJson.put("modify_time", BmUtil.formatDate(userGroup.getModify_time()));

		if ("channel".equals(userGroup.getType())){
			String a = "";
			if (userGroup.getIds_value() != null) {
				String[] cont_provider_ids = userGroup.getIds_value().split(",");
				for (int i = 0; i < cont_provider_ids.length; i++) {
					a = a + "," + cpMap.get(cont_provider_ids[i]).getName();
				}
				if (StringUtils.isNotBlank(a)){
					a = a.substring(1);
				}
				colJson.put("raw_value", a);
			}
		}
		
		return colJson;
	}

	private void updateUserGroup(UserGroup userGroup, HttpServletRequest req, Map<String, Channel> cpMap) {

		try {
			userGroup.setName(req.getParameter("name"));
			userGroup.setType(req.getParameter("type"));
			userGroup.setRaw_value(req.getParameter("raw_value"));
			userGroup.setIds_value(req.getParameter("ids_value"));
			userGroup.setDescription(req.getParameter("intro"));

			if ("channel".equals(userGroup.getType())){
				String a = "";
				if (userGroup.getIds_value() != null) {
					String[] cont_provider_ids = userGroup.getIds_value().split(",");
					for (int i = 0; i < cont_provider_ids.length; i++) {
						a = a + "," + cpMap.get(cont_provider_ids[i]).getChannel();
					}
					if (StringUtils.isNotBlank(a)){
						a = a.substring(1);
					}
					userGroup.setRaw_value(a);
				}
			}
			if (StringUtils.isNotBlank(userGroup.getRaw_value())){
				userGroup.setRaw_value(userGroup.getRaw_value().replace("\n", ","));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
