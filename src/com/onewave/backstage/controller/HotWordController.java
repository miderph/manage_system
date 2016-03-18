package com.onewave.backstage.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
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
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.HotWord;
import com.onewave.backstage.service.HotWordService;
import com.onewave.backstage.util.BmUtil;

@Controller("hotWordController")
@RequestMapping("/hotword/*")
public class HotWordController extends MultiActionController {
	
	@Autowired
	@Qualifier("hotWordService")
	private HotWordService hotWordService;
	

   private HashMap<String,Integer> checkPage(HttpServletRequest req){
      HashMap<String,Integer> result = new HashMap<String,Integer>();
      int first = 1;
      int last = 100;
      int recordCountPerPage = 100;
      int pageNo = 1;
      try {
         first = Integer.parseInt(req.getParameter("start"));
      } catch (Exception e) {
         e.printStackTrace();
      }
      try {
         recordCountPerPage = first+Integer.parseInt(req.getParameter("limit"));
         last = first+recordCountPerPage-1;
      } catch (Exception e) {
         e.printStackTrace();
      }
      logger.info("first="+first+",last="+last);
      result.put("first", first);
      result.put("last", last);
      result.put("pagesize", recordCountPerPage);
      result.put("pageNo", (first/recordCountPerPage));
      return result;
   }
	@RequestMapping("query_rela.do")
   public void queryRelaHotword(HttpServletRequest req, HttpServletResponse resp)
         throws IOException {
      resp.setContentType("application/json; charset=UTF-8");
      if (!BmUtil.isAdminOperator(req)) {
         resp.getWriter().print("{total:-1,data:[],error:'need to be logined!'}");
         return;
      }
      HashMap<String,Integer> pageInfo = checkPage(req);
      String site_id = req.getParameter("site_id");logger.info("site_id="+site_id);
      String hotword = req.getParameter("hotword");logger.info("hotword="+hotword);
      if (StringUtils.isBlank(site_id)){
         site_id = ""+InitManager.Defaut_Unselected_ID;
      }

      if (Integer.parseInt(site_id) <= 0){
         resp.getWriter().print("{total:-1,data:[],error:'please select a site first!'}");
         return ;
      }
      JSONArray rootArr = new JSONArray();
      long total = 0;
      try {
         List<HotWord> list_site0 = hotWordService.findAll("0", hotword, pageInfo.get("first"), pageInfo.get("last"));
         List<HotWord> list = hotWordService.findAll(site_id, hotword, pageInfo.get("first"), pageInfo.get("last"));
         if(list != null && list.size() > 0
          &&list_site0 != null && list_site0.size() > 0) {
            for (int ii = list_site0.size() - 1; ii >= 0; ii--){
               HotWord hotWord = list_site0.get(ii);
               for (int jj = list.size() - 1; jj >= 0; jj--){
                  String hid = list.get(jj).getHid(); 
                  if (hotWord.getId().equals(hid)){
                     list.remove(jj);
                     list_site0.remove(ii);
                     break;
                  }
               }
            }
         }
         
         if(list_site0 != null && list_site0.size() > 0) {
            for(HotWord hotWord: list_site0) {
               if(hotWord == null) continue;
               
               rootArr.add(parserHotWord(hotWord));
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
	@RequestMapping("add_rela_hotword.do")
	public void addRelaHotword(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
      resp.setContentType("application/json; charset=UTF-8");

      String site_id = req.getParameter("site_id");logger.info("site_id="+site_id);
      String ids = req.getParameter("ids");logger.info("ids="+ids);
      boolean issuc = false;
      String msg = "保存失败";
      try {
         if (!BmUtil.isAdminOperator(req)) {
            msg = "非Admin用户不能修改";
            return;
         }

         hotWordService.addRelaHotword(site_id,ids);
         issuc = true;
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (issuc) {
            msg = "保存成功";
         }
         
         JSONObject rootJson = new JSONObject();
         rootJson.put("success", issuc);
         rootJson.put("msg", msg);
         
         logger.info(rootJson);
         resp.getWriter().print(rootJson);
      }
	}
	@RequestMapping("del_rela_hotword.do")
	public void deleteRelaHotword(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
      resp.setContentType("application/json; charset=UTF-8");

      String ids = req.getParameter("ids");logger.info("ids="+ids);
      boolean issuc = false;
      String msg = "解绑失败";
      try {
         if (!BmUtil.isAdminOperator(req)) {
            msg = "非Admin用户不能修改";
            return;
         }

         hotWordService.deleteRelaHotword(ids);
         issuc = true;
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (issuc) {
            msg = "解绑成功";
         }
         
         JSONObject rootJson = new JSONObject();
         rootJson.put("success", issuc);
         rootJson.put("msg", msg);
         
         logger.info(rootJson);
         resp.getWriter().print(rootJson);
      }
	}
	
	@RequestMapping("query.do")
	public void queryHandler(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
      if (!BmUtil.isAdminOperator(req)) {
         resp.getWriter().print("{total:-1,data:[],error:'need to be logined!'}");
         return;
      }

      HashMap<String,Integer> pageInfo = checkPage(req);
		
		String site_id = req.getParameter("site_id");logger.info("site_id="+site_id);
		String hotword = req.getParameter("hotword");logger.info("hotword="+hotword);
      if (StringUtils.isEmpty(site_id)){
         site_id = ""+InitManager.Defaut_Unselected_ID;
      }

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {
			List<HotWord> list = hotWordService.findAll(site_id, hotword, pageInfo.get("first"), pageInfo.get("last"));
			if(list != null && list.size() > 0) {
				for(HotWord hotWord: list) {
					if(hotWord == null) continue;
					
					rootArr.add(parserHotWord(hotWord));
				}
			}
			
			total = hotWordService.countAll(site_id, hotword);
			
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
			
			if (BmUtil.isAdminOperator(req)) {
				HotWord hotWord = new HotWord();
				updateHotWord(hotWord, req);
	         hotWord.setSite_id("0");
				if (hotWordService.isExist(hotWord)) {
					msg = "热词【" + hotWord.getHotword() +"】已存在";
				} else {
					issuc = hotWordService.save(hotWord);
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

		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");
		
		boolean issuc = false;
		String msg = "修改失败";
		try {
			
			if (BmUtil.isAdminOperator(req)) {
				HotWord hotWord = new HotWord();
				hotWord.setId(req.getParameter("hw_id"));
				updateHotWord(hotWord, req);
				if (hotWordService.isExist(hotWord)) {
					msg = "热词【" + hotWord.getHotword() +"】已存在";
				} else {
					issuc = hotWordService.update(hotWord);
					hotWordService.updateAllRela(hotWord);//更新所有站点已关联的热词
				}
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
		String retMsg = "删除失败";
		try {
         String id = req.getParameter("hw_id");logger.info("==================id_del=" + id);
         String hotword_del = req.getParameter("hotword_del");logger.info("==================hotword_del=" + hotword_del);
         String site_id_del = req.getParameter("site_id_del");logger.info("==================site_id_del=" + site_id_del);

			if (BmUtil.isAdminOperator(req)) {
	         HotWord hotWord = hotWordService.findById(id);

	         if (hotWord != null){
	            boolean canDelete = true;
	            if ("0".equals(hotWord.getSite_id())){
	               int icount = hotWordService.countAllById(id);//是否已关联给其他站点
	               if (icount > 0){
	                  canDelete = false;
	                  retMsg = "删除失败！热词[" + hotword_del +",id="+ id + "]已经绑定给其他站点！";
	               }
	               else{
	                  canDelete = true;
	               }
	            }
	            if (canDelete){
	               issuc = hotWordService.delete(id);
	               if (!issuc) {
	                  retMsg = "热词[" + hotword_del +",id="+ id + "]删除失败";
	               }
	            }
	         }
	         else{
	            retMsg = "热词[" + hotword_del +",id="+ id + "]存在";
	         }
			} else {
			   retMsg = "非Admin用户不能删除";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (issuc) {
			   retMsg = "删除成功";
			}
			
			JSONObject rootJson = new JSONObject();
			rootJson.put("success", true);
			rootJson.put("issuc", issuc);
			rootJson.put("msg", retMsg);

			logger.info(rootJson);
			resp.getWriter().print(rootJson);
		}
	}
	
	private JSONObject parserHotWord(HotWord hotWord) {
		
		if(hotWord == null) return null;
		
		JSONObject colJson = new JSONObject();
		
		colJson.put("hw_id", hotWord.getId());
		colJson.put("hotword", StringTool.null2Empty(hotWord.getHotword()));
		colJson.put("site_id", StringTool.null2Empty(hotWord.getSite_id()));
		colJson.put("create_time", BmUtil.formatDate(hotWord.getCreate_time()));
		colJson.put("modify_time", BmUtil.formatDate(hotWord.getModify_time()));
		
		return colJson;
	}
	
	private void updateHotWord(HotWord hotWord, HttpServletRequest req) {
		
		try {
			hotWord.setHotword(req.getParameter("hotword"));
			hotWord.setSite_id(req.getParameter("site_id"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
