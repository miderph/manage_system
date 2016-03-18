package com.onewave.backstage.excel;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class ExcelUtil {

	public static String parserCell(Cell cell) {
		String str = "";

		if (cell == null) {
			return str;
		}

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			str = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				if (date != null) {
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

		if (str != null) {
			str = str.trim();
		} else {
			str = "";
		}

		return str;
	}

	public static boolean needAuth(HttpServletRequest req) {
		String auth = req.getParameter("auth");
		boolean flag = !StringUtils.isEmpty(auth) && "1".equals(auth);
		return flag;
	}
	
	public static MultipartFile getMultipartFile(HttpServletRequest req){
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				req.getSession().getServletContext());
		// 判断req是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(req)) {
			// 转换成多部分req
			MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) req;
			MultipartFile multiFile = multiReq.getFile("excel_file");
			return multiFile;
		}
		return null;
	}
	
	public static String getOriginalFileName(HttpServletRequest req){
		MultipartFile multiFile = getMultipartFile(req);
		if(null != multiFile){
			String fileName = multiFile.getOriginalFilename();
			return fileName;
		}
		return null;
	}
	
	public static File paserFile(HttpServletRequest req, String tag) {
		File file = null;
		String newFileName = "excel_file";

		if (StringUtils.isNotBlank(tag)) {
			newFileName = tag + "_" + newFileName;
		}
		try {
			MultipartFile multiFile = getMultipartFile(req);
			if (null != multiFile) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				// 取得当前上传文件的文件名称
				String fileName = multiFile.getOriginalFilename();
				if (null != fileName && !"".equals(fileName.trim())) {
					String suffix = fileName.substring(fileName.lastIndexOf("."));
					newFileName += format.format(java.util.Calendar.getInstance().getTime()) + suffix;
					String filePath = InitManager.getRootLocalPath()+ newFileName;
					file = new File(filePath);
					multiFile.transferTo(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return file;
	}

	public static Workbook paserWorkbook(String excelPath) {
		Workbook wb = null;
		try {
			if (excelPath != null && excelPath.contains(".xls")) {
				if (excelPath.endsWith(".xls")) {
					wb = new HSSFWorkbook(new FileInputStream(excelPath));
				} else if (excelPath.endsWith(".xlsx")) {
					wb = new XSSFWorkbook(new FileInputStream(excelPath));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return wb;
	}

}
