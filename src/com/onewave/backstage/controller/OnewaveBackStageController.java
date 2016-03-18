package com.onewave.backstage.controller;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("onewaveBackStageController")
public class OnewaveBackStageController extends MultiActionController {
	
	@RequestMapping("index.do")
	public ModelAndView indexHandler(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		//return new ModelAndView("/jsp/login/login");
		return new ModelAndView("onewave/onewave");
	}
	
	@RequestMapping("theme_combo.do")
	public ModelAndView skinThemeHandler(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		
		JSONArray jsonArray = new JSONArray();
		JSONObject groupJson=new JSONObject();
		
		String[][] skinDate= {
				{"默认", "css/ext-all.css"},
				{"淡紫", "Lavender/css/ext-all.css"},
				{"粉红", "Pink/css/ext-all.css"},
				{"砖红", "BrickRed/css/ext-all.css"},
				{"淡黄", "LightYellow/css/ext-all.css"},
				{"黄绿", "Olivine/css/ext-all.css"},
				{"淡绿", "LightGreen/css/ext-all.css"},
				{"浅灰", "LightGray/css/ext-all.css"},
		};
		
		for(int i=0; i<skinDate.length; i++) {
			groupJson.put("theme_name", skinDate[i][0]);
			groupJson.put("theme_value", skinDate[i][1]);
			jsonArray.add(groupJson);
		}
		
		JSONObject json = new JSONObject();
		
		json.put("theme_combo_data", jsonArray);
		logger.info(json);
		pw.print(json);
		
		return null;
	}

}
