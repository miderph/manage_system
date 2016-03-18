package com.onewave.backstage.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.onewave.backstage.model.ContCheck;
import com.onewave.backstage.service.ContChecksService;
import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

public class ContChecksExcel {
	private static Log logger = LogFactory.getLog(ContSalesExcel.class);
	public static String saveFromExcel(HttpServletRequest req, String filePath,ContChecksService contChecksService,String classify) {

		if (req == null || contChecksService == null) {
			return null;
		}

		List<String> msgList = new ArrayList<String>();

		try {
			Sheet sheet = null;
			Workbook wb = ExcelUtil.paserWorkbook(filePath);

			if(wb != null) sheet = wb.getSheetAt(0);
			Map<Integer, String> fieldMap = new HashMap<Integer, String>();
			if(sheet != null) {
				Iterator<Row> rowI = sheet.iterator();
				int cellCount = 0;
				
				boolean verification = false;
				if(rowI.hasNext()) {
					Iterator<Cell> cellI = rowI.next().iterator();
					String cellVallue = "";
					while(cellI.hasNext()) {
						cellVallue = cellI.next().getRichStringCellValue().getString();
						verification = verificationFieldForSave(cellCount, cellVallue);
						fieldMap.put(cellCount, cellVallue);
						cellCount++;
						
						if(!verification) {
							break;
						}
					}
				}
				
				if(!verification) {
					return "文件验证失败";
				}
				
				if(cellCount > 0) {
					String softId = null;
					int index = 0;
					while(rowI.hasNext()) {
						index++;
						Row row = rowI.next();
						String result = "";
						result += "第" + index + "条数据";
						ContCheck contCheck = parseBeanFromRow(row, index);
						contCheck.setClassify(classify);
						if(StringUtils.isBlank(contCheck.getItemUrl())) {
							result += "商品详情页链接地址为空";
							msgList.add(result);
							continue;
						}
						try {
							ContCheck oldContCheck = contChecksService.findByItemUrl(contCheck.getItemUrl());
							
							if(null !=oldContCheck){
								result += contCheck.getItemUrl()+" 记录已存在，";
								oldContCheck.setBate(contCheck.getBate());
								oldContCheck.setClassify(contCheck.getClassify());
								oldContCheck.setCode(contCheck.getCode());
								oldContCheck.setIcon(contCheck.getIcon());
								oldContCheck.setName(contCheck.getName());
								oldContCheck.setPrice(contCheck.getPrice());
								oldContCheck.setSales_num(contCheck.getSales_num());
								oldContCheck.setShop(contCheck.getShop());
								oldContCheck.setTaobaoke_url(contCheck.getTaobaoke_url());
								oldContCheck.setTaobaoke_url_short(contCheck.getTaobaoke_url_short());
								oldContCheck.setWangwang(contCheck.getWangwang());
								if(contChecksService.update(oldContCheck)){
									result += "更新成功";
								}else{
									result +="更新失败";
								}
							} else {
								contCheck.setStatus(ContCheck.EnumStatus.Normal.getValue());
								softId = contChecksService.saveAndReturnId(contCheck);
								if(!StringUtils.isEmpty(softId)){
									result += contCheck.getItemUrl() + " 添加成功";
								}
							}
							
						} catch (Exception e) {
							result += "添加失败 " + e.getMessage();
							e.printStackTrace();
							logger.error("添加失败", e);
						}
						msgList.add(result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return msgList.size() > 0 ? StringUtils.join(msgList, "\n") : "批量导入失败";
	}
	
	private static ContCheck parseBeanFromRow(Row row,long rowIndex){
		ContCheck check = new ContCheck();
		logger.info("contcheck excelupdate 第" + rowIndex + "条数据");
		check.setCode(ExcelUtil.parserCell(row.getCell(0)));
		check.setName(ExcelUtil.parserCell(row.getCell(1)));
		check.setIcon(ExcelUtil.parserCell(row.getCell(2)));
		check.setItemUrl(ExcelUtil.parserCell(row.getCell(3)));
		check.setShop(ExcelUtil.parserCell(row.getCell(4)));
		check.setPrice(ExcelUtil.parserCell(row.getCell(5)));
		check.setSales_num(ExcelUtil.parserCell(row.getCell(6)));
		check.setBate(ExcelUtil.parserCell(row.getCell(7)));
		check.setWangwang(ExcelUtil.parserCell(row.getCell(8)));
		check.setTaobaoke_url_short(ExcelUtil.parserCell(row.getCell(9)));
		check.setTaobaoke_url(ExcelUtil.parserCell(row.getCell(10)));
		return check;
	}
	
	
	private static boolean verificationFieldForSave(int cellCount, String cellVallue) {
		boolean verification = true;
		switch(cellCount) {
		case 0:
			if(cellVallue == null || !"商品id".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 1:
			if(cellVallue == null || !"商品名称".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 3: //D
			if(cellVallue == null || !"商品详情页链接地址".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 5://F
			if(cellVallue == null || !"商品价格(单位：元)".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		}
		return verification;
	}
	

}
