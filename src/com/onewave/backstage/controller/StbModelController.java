package com.onewave.backstage.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onewave.backstage.model.StbModel;
import com.onewave.backstage.service.StbModelService;

@Controller("stbModelController")
@RequestMapping("/stbmodel/*")
public class StbModelController extends MultiActionController {
	
	@Autowired
	@Qualifier("stbModelService")
	private StbModelService stbModelService;

	@RequestMapping("query.do")
	public void queryAllStbModel(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		try {
			
			List<StbModel> list = stbModelService.findAll();
			
			if(list != null && list.size() > 0) {
				JSONObject colJson = null;
				for(StbModel stbModel: list) {
					if(stbModel == null) continue;
					
					colJson = new JSONObject();
					
					colJson.put("sm_id", stbModel.getId());
					colJson.put("sm_model", stbModel.getModel());
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

}
