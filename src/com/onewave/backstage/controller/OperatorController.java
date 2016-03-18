package com.onewave.backstage.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhilink.tools.StringTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.Operator;
import com.onewave.backstage.service.OperatorService;
import com.onewave.backstage.util.BmUtil;

@Controller("operatorController")
@RequestMapping("/operator/*")
public class OperatorController extends MultiActionController {
	
	@Autowired
	@Qualifier("operatorService")
	private OperatorService operatorService;

	public OperatorService getOperatorService() {
		return operatorService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
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
			
			if (BmUtil.isAdminOperator(req)) {
				List<Operator> list = operatorService.findAll(firstResult, maxResults);
				if(list != null && list.size() > 0) {
					for(Operator operator: list) {
						if(operator == null) continue;
						
						rootArr.add(parserOperator(operator));
					}
				}
				
				total = operatorService.countAll();
			} else {
				Object obj = req.getSession().getAttribute("user");
				Operator user = null;
				
				if(obj != null && obj instanceof Operator) {
					user = (Operator) obj;
					user = operatorService.findById(user.getId());
				}
				
				if(user != null) {
					rootArr.add(parserOperator(user));
					total = 1;
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
			Operator admin = (Operator) req.getSession().getAttribute("admin");
			if(admin != null) {
				System.out.println("------------admin name: " + admin.getName());
			}
			if (admin == null || !"admin".equals(admin.getName())) {// 只有admin能增加和删除帐号
				msg = "添加失败！只有admin能增加和删除帐号！";
			} else {
				String name = req.getParameter("name");
				
				if(operatorService.isExist(name)) {
					msg = "添加失败，用户【" + name + "】已经存在。";
				} else {
					Operator operator = new Operator();
					updateOperator(operator, req);
					operator.setStatus(req.getParameter("status"));
					issuc = operatorService.save(operator);
				}
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

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		
		boolean issuc = false;
		String msg = "修改失败";
		try {
		   Operator admin = (Operator) req.getSession().getAttribute("user");
		   String name = req.getParameter("name");
		   
		   if(admin != null && ("admin".equals(admin.getName()) || admin.getName().equals(name))) {
			   String id = req.getParameter("o_id");
			   Operator operator = operatorService.findById(id);
			   
			   updateOperator(operator, req);
			   if (!admin.getId().equals(id)) {
				   operator.setStatus(req.getParameter("status"));
			   }
			   
			   issuc = operatorService.update(operator);
		   } else {
			   msg = "修改失败！请重新登录！";
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
			String id = req.getParameter("o_id");
			Operator admin = (Operator) req.getSession().getAttribute("admin");
			
			if (admin == null || !"admin".equals(admin.getName())) {// 只有admin能增加和删除帐号
				msg = "删除失败！只有admin能增加和删除帐号！";
			} else {
				if(admin.getId().equals(id)) {
					msg = "删除失败！不能删除admin！";
				} else {
					issuc = operatorService.delete(id);
				}
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
	
	@RequestMapping("change_password.do")
	public void changePasswordHandler(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		boolean issuc = false;
		String msg = "修改密码失败";
		try {
			Operator admin = (Operator) req.getSession().getAttribute("user");

			String id = req.getParameter("o_id");

			Operator operator = operatorService.findById(id);
			if (!"admin".equals(admin.getName()) && !admin.getName().equals(operator.getName())) {//admin可修改所有用户信息，用户只能修改自己的信息
				msg = "修改密码失败！请重新登录！";
			} else {
				operator.setPassword(req.getParameter("psd"));
				issuc = operatorService.update(operator);
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
	
	@RequestMapping("login.do")
	public void loginHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
	      
	      logger.info("--:");
	      resp.setContentType("application/json; charset=UTF-8");
	      
	      req.getSession().removeAttribute("admin");//当为admin是有值
	      req.getSession().removeAttribute("user");//当前登录用户

	      String msg = "";
	      String url = "";
	      boolean issuc = false;
	      try{
	         String username = req.getParameter("username").trim();
	         String password = req.getParameter("password").trim();
	         
	         logger.info("------username: " + username);
	         logger.info("------password: " + password);

	         Operator user = operatorService.findByName(username);
	         if (user == null){
	            msg = "登录账号不存在";
	            return;
	         }
	         
	         req.getSession().setAttribute("user", user);
	         
	         if(Integer.parseInt(user.getStatus()) <= 0) {
	        	 msg = "登录账号无效";
	        	 return;
	         }
	         
	         if(password == null || !password.equals(user.getPassword())) {
	        	 msg = "登录密码不正确";
	         } else {
	        	 if ("admin".equals(user.getName())) {
		        	 req.getSession().setAttribute("admin", user);//当为admin是有值
	 	         }
	        	 
	        	 url = "index.jsp";
		         issuc = true;
	         }

	      } catch (Exception E) {
	         E.printStackTrace();
	         msg = "登录失败";
	      } finally {
	    	 JSONObject rootJson = new JSONObject();
	    	 rootJson.put("success", true);
	    	 rootJson.put("issuc", issuc);
	    	 rootJson.put("url", url);
	    	 rootJson.put("msg", msg);
	    	 
	         logger.info(rootJson);
	         resp.getWriter().println(rootJson);
	      }
	}
	
	@RequestMapping("unlogin.do")
	public void unloginHandler(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
	      
	      logger.info("--:");
	      resp.setContentType("application/json; charset=UTF-8");
	      String msg = "";
	      String url = "";
	      boolean issuc = false;
	      
	      try{
	    	  //清除session
	          @SuppressWarnings("unchecked")
	          Enumeration<String> em = req.getSession().getAttributeNames();
	          
	          while(em.hasMoreElements()){
	        	  req.getSession().removeAttribute(em.nextElement().toString());
	          }
	          req.getSession().invalidate();
	          //获取项目真实路径 
	          String path = req.getContextPath();
	          //拼接跳转页面路径
	          String basePath = req.getScheme() + "://"
	                  + req.getServerName() + ":" + req.getServerPort()
	                  + path + "/";
	          
	          msg = "成功退出";
	          issuc = true;
	          url = basePath;
	      } catch(Exception E) {
	         E.printStackTrace();
	         msg = "退出时发生错误";
	      } finally {
	    	  JSONObject rootJson = new JSONObject();
	    	  rootJson.put("success", true);
	    	  rootJson.put("issuc", issuc);
	    	  rootJson.put("url", url);
	    	  rootJson.put("msg", msg);
	    	  
	    	  logger.info(rootJson);
	    	  resp.getWriter().println(rootJson);
	      }
	}
	
	private JSONObject parserOperator(Operator operator) {
		
		if(operator == null) return null;
		
		JSONObject colJson = new JSONObject();
		
		colJson.put("o_id", operator.getId());
		colJson.put("name", StringTool.null2Empty(operator.getName()));
		colJson.put("password", StringTool.null2Empty(operator.getPassword()));
		colJson.put("status", StringTool.null2Empty(operator.getStatus()));
		colJson.put("email", StringTool.null2Empty(operator.getEmail()));
		colJson.put("tel", StringTool.null2Empty(operator.getTel()));
		colJson.put("intro", StringTool.null2Empty(operator.getIntro()));
		colJson.put("phone", StringTool.null2Empty(operator.getPhone()));
		colJson.put("role_ids", StringTool.null2Empty(operator.getRole_ids()));
		colJson.put("role_names", StringTool.null2Empty(operator.getRole_names()));
		colJson.put("create_time", BmUtil.formatDate(operator.getCreate_time()));
		colJson.put("modify_time", BmUtil.formatDate(operator.getModify_time()));
		
		return colJson;
	}
	
	private void updateOperator(Operator operator, HttpServletRequest req) {
		
		try {
			operator.setName(req.getParameter("name"));
			operator.setPassword(req.getParameter("password"));
			operator.setEmail(req.getParameter("email"));
			operator.setTel(req.getParameter("tel"));
			operator.setIntro(req.getParameter("intro"));
			operator.setPhone(req.getParameter("phone"));
			operator.setRole_ids(req.getParameter("role_ids"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
