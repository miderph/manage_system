package com.onewave.backstage.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.onewave.backstage.controller.SoftwareController;
import com.onewave.backstage.model.SoftwareVersion;
import com.onewave.backstage.service.SoftwareService;
import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

public class SoftwareExcel {
	private static Log logger = LogFactory.getLog(ContSalesExcel.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static String saveFromExcel(HttpServletRequest req, String filePath,
			SoftwareService softwareService, SoftwareController sc) {

		if (req == null || softwareService == null) {
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
						logger.info("software excel update 第" + index + "条数据");
						String version_number = ExcelUtil.parserCell(row.getCell(0));
						logger.info("software excel update: version_number=" + version_number);
						String plat = ExcelUtil.parserCell(row.getCell(1));
						result += ",平台信息=" + plat + ",版本号=" + plat;
						logger.info("software excel update: plat=" + plat);
						
						String md5 = ExcelUtil.parserCell(row.getCell(2)); 
						logger.info("software excel update: md5=" + md5);
						String status = ExcelUtil.parserCell(row.getCell(3));
						logger.info("software excel update: status=" + status);
						String fileType = ExcelUtil.parserCell(row.getCell(4));
						logger.info("software excel update: fileType=" + fileType);
						Date publish_time = null;
						try {
							publish_time = sdf.parse(ExcelUtil.parserCell(row.getCell(5)));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						logger.info("software excel update: publish_time=" + publish_time);
						String enforce_flag = ExcelUtil.parserCell(row.getCell(6));
						logger.info("software excel update: enforce_flag=" + enforce_flag);
						String software_info = ExcelUtil.parserCell(row.getCell(7));
						logger.info("software excel update: software_info=" + software_info);
						String description = ExcelUtil.parserCell(row.getCell(8));
						logger.info("software excel update: description=" + description);
						String userGroupIdsMac = ExcelUtil.parserCell(row.getCell(9));
						logger.info("software excel update: userGroupIdsMac=" + userGroupIdsMac);
						String userGroupIdsZone = ExcelUtil.parserCell(row.getCell(10));
						logger.info("software excel update: userGroupIdsZone=" + userGroupIdsZone);
						String userGroupIdsModel = ExcelUtil.parserCell(row.getCell(11));
						logger.info("software excel update: userGroupIdsModel=" + userGroupIdsModel);
						// 普通地址
						String url_general_id = "-1";
						String update_url_general = ExcelUtil.parserCell(row.getCell(12));
						logger.info("software excel update: update_url_general=" + update_url_general);
						
						// 360云盘
						String url_360_id = "-1";
						String update_url_360 = ExcelUtil.parserCell(row.getCell(13));
						logger.info("software excel update: update_url_360=" + update_url_360);
						String share_password_360 = ExcelUtil.parserCell(row.getCell(14));
						logger.info("software excel update: share_password_360=" + share_password_360);
						
						result += " --> ";
						if(StringUtils.isEmpty(version_number) || StringUtils.isEmpty(plat)) {
							result += "平台信息或版本号为空";
							msgList.add(result);
							continue;
						}

						SoftwareVersion soft = new SoftwareVersion(null, version_number,
								software_info, plat, enforce_flag, update_url_360, description,
								publish_time
								, userGroupIdsMac, userGroupIdsZone,userGroupIdsModel,null,null,null,null,null
								, fileType, "-1", status, share_password_360,
								md5);
						
						try {
							if (softwareService.isExistRecord(soft)) {
								result += "已存在此平台信息和版本号";
								msgList.add(result);
								continue;
							} else {
								softId = softwareService.saveAndReturnId(soft);
							}
							
							if(!StringUtils.isEmpty(softId) && sc != null) {
								sc.saveOrUpdateUrl(url_general_id, update_url_general,
										url_360_id, update_url_360, share_password_360, softId);
							}
							result += "添加成功";
						} catch (Exception e) {
							result += "添加失败 " + e.getMessage();
							e.printStackTrace();
						}
						
						if(StringUtils.isBlank(update_url_general) || StringUtils.isBlank(update_url_360)) {
							result += "下载地址为空";
						}
						if(StringUtils.isNotBlank(update_url_360) && StringUtils.isBlank(share_password_360)) {
							result += "分享密码为空";
						}
						msgList.add(result);
						softId = null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return msgList.size() > 0 ? StringUtils.join(msgList, "\n") : "批量导入失败";
	}

//	public String saveFromExcel(String filePath) {
//		
//		List<String> msgList = new ArrayList<String>();
//		
//		try {
//			Sheet sheet = null;
//			Workbook wb = null;
//			if(filePath != null && filePath.contains(".xls")) {
//				if(filePath.endsWith(".xls")) {
//					wb = new HSSFWorkbook(new FileInputStream(filePath));
//				} else if(filePath.endsWith(".xlsx")) {
//					wb = new XSSFWorkbook(new FileInputStream(filePath));
//				}
//			}
//			
//			if(wb != null) sheet = wb.getSheetAt(0);
//			Map<Integer, String> fieldMap = new HashMap<Integer, String>();
//			if(sheet != null) {
//				Iterator<Row> rowI = sheet.iterator();
//				int cellCount = 0;
//				
//				boolean verification = true;
//				if(rowI.hasNext()) {
//					Iterator<Cell> cellI = rowI.next().iterator();
//					String cellVallue = "";
//					while(cellI.hasNext()) {
//						cellVallue = cellI.next().getRichStringCellValue().getString();
//						verification = verificationField(cellCount, cellVallue);
//						fieldMap.put(cellCount, cellVallue);
//						cellCount++;
//						
//						if(!verification) {
//							break;
//						}
//					}
//				}
//				
//				if(!verification) {
//					return "文件验证失败";
//				}
//				
//				if(cellCount > 0) {
//					String softId = null;
//					int index = 1;
//					while(rowI.hasNext()) {
//						Row row = rowI.next();
//						logger.info("software excel update 第" + index + "条数据");
//						String version_number = parserCell(row.getCell(0));
//						logger.info("software excel update: version_number=" + version_number);
//						String plat = parserCell(row.getCell(1));
//						logger.info("software excel update: plat=" + plat);
//						if(StringUtils.isEmpty(version_number) || StringUtils.isEmpty(plat)) {
//							msgList.add("第" + index + "条软件版本或软件平台为空");
//							index++;
//							continue;
//						}
//						
//						String md5 = parserCell(row.getCell(2)); 
//						logger.info("software excel update: md5=" + md5);
//						String status = parserCell(row.getCell(3));
//						logger.info("software excel update: status=" + status);
//						String fileType = parserCell(row.getCell(4));
//						logger.info("software excel update: fileType=" + fileType);
//						String publish_time = parserCell(row.getCell(5));
//						logger.info("software excel update: publish_time=" + publish_time);
//						String enforce_flag = parserCell(row.getCell(6));
//						logger.info("software excel update: enforce_flag=" + enforce_flag);
//						String software_info = parserCell(row.getCell(7));
//						logger.info("software excel update: software_info=" + software_info);
//						String description = parserCell(row.getCell(8));
//						logger.info("software excel update: description=" + description);
//						String userGroupIdsMac = parserCell(row.getCell(9));
//						logger.info("software excel update: userGroupIdsMac=" + userGroupIdsMac);
//						String userGroupIdsZone = parserCell(row.getCell(10));
//						logger.info("software excel update: userGroupIdsZone=" + userGroupIdsZone);
//						String userGroupIdsModel = parserCell(row.getCell(11));
//						logger.info("software excel update: userGroupIdsModel=" + userGroupIdsModel);
//						// 普通地址
//						String url_general_id = "-1";
//						String update_url_general = parserCell(row.getCell(12));
//						logger.info("software excel update: update_url_general=" + update_url_general);
//						
//						// 360云盘
//						String url_360_id = "-1";
//						String update_url_360 = parserCell(row.getCell(13));
//						logger.info("software excel update: update_url_360=" + update_url_360);
//						String share_password_360 = parserCell(row.getCell(14));
//						logger.info("software excel update: share_password_360=" + share_password_360);
//						
//						SoftwareVersion soft = new SoftwareVersion("-1", version_number,
//								software_info, plat, enforce_flag, update_url_360, description,
//								publish_time, userGroupIdsMac, userGroupIdsZone,userGroupIdsModel,null,null,null,null,null
//								, fileType, "-1", status, share_password_360,
//								md5);
//						
//						if (this.softwareService.isExistRecord(soft)) {
//							msgList.add("第" + index + "条记录已存在");
//							index++;
//							continue;
//						} else {
//							softId = this.softwareService.saveAndReturnId(soft);
//						}
//						
//						if(!StringUtils.isEmpty(softId)) {
//							saveOrUpdateUrl(url_general_id, update_url_general,
//									url_360_id, update_url_360, share_password_360, softId);
//						}
//						
//						index++;
//						softId = null;
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return msgList.size() > 0 ? StringUtils.join(msgList, "\n") : "批量导入成功";
//	}
	
	public static boolean verificationFieldForSave(int cellCount, String cellVallue) {
		boolean verification = true;
		switch(cellCount) {
		case 0:
			if(cellVallue == null || !"版本号".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 1:
			if(cellVallue == null || !"平台信息".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 2:
			if(cellVallue == null || !"MD5值".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 3:
			if(cellVallue == null || !"状态".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 4:
			if(cellVallue == null || !"文件类型".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 5:
			if(cellVallue == null || !"发布时间".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 6:
			if(cellVallue == null || !"是否强制升级".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 7:
			if(cellVallue == null || !"软件信息".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 8:
			if(cellVallue == null || !"描述".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 9:
			if(cellVallue == null || !"网卡地址测试组".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 10:
			if(cellVallue == null || !"地区测试组".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 11:
			if(cellVallue == null || !"型号测试组".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 12:
			if(cellVallue == null || !"普通升级地址".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 13:
			if(cellVallue == null || !"360云盘升级地址".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 14:
			if(cellVallue == null || !"360分享密码".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		}
		
		return verification;
	}
	
	public String parserCell(Cell cell) {
		String str = "";
		
		if(cell == null) {
			return str;
		}
		
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			str = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				if(date != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					str = sdf.format(date);
				}
            } else {
            	str = "" + (int) cell.getNumericCellValue();
            }
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			str = "" + cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			str = "" + cell.getCellFormula();
			break;
		default:
			str = "";
		}
		
		return str;
	}
}
