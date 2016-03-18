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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.model.Area;
import com.onewave.backstage.service.AreaService;

@Controller("areaController")
@RequestMapping("/area/*")
public class AreaController extends MultiActionController {

	@Autowired
	@Qualifier("areaService")
	private AreaService areaService;

	@RequestMapping("query_prov.do")
	public void queryAreaProvHandler(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {

			List<Area> list = areaService.findAllProv();
			if(list != null && list.size() > 0) {
				for(Area area: list) {
					if(area == null) continue;
					
					rootArr.add(parserArea(area));
					total++;
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

	@RequestMapping("query_city.do")
	public void queryAreaCityHandler(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		logger.info("--:");
		resp.setContentType("application/json; charset=UTF-8");

		JSONArray rootArr = new JSONArray();
		long total = 0;
		try {

			List<Area> list = areaService.findAllCity();
			if(list != null && list.size() > 0) {
				for(Area area: list) {
					if(area == null) continue;
					
					if("北京上海天津重庆".indexOf(area.getName())>=0) {
						continue;//不返回4个直辖市
					}
					
					rootArr.add(parserArea(area));
					total++;
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

	private JSONObject parserArea(Area area) {

		if (area == null)
			return null;

		JSONObject colJson = new JSONObject();

		colJson.put("id", area.getId());

		String pname = area.getPname();
		if (pname == null && "0".equals(area.getParent_id())){
		   pname = area.getName();
		}
		if ((pname != null && pname.length() >= 3) && (pname.startsWith("黑龙") || pname.startsWith("内蒙"))) {
			colJson.put("pname", pname.substring(0, 3));
		} else if (pname != null && pname.length() >= 2) {
			colJson.put("pname", pname.substring(0, 2));
		} else {
			colJson.put("pname", "" + pname);
		}

		colJson.put("name", area.getName());
		colJson.put("description", area.getDescription());

		return colJson;
	}

}
