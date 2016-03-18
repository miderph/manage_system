package com.onewave.backstage.excel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.zhilink.tools.PinyinUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.model.ContentVideoFileBean;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.service.ContSalesService;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoFileService;
import com.onewave.backstage.service.ContVideoService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.backstage.util.BmUtil;
import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

public class ContSalesExcel {
	private static Log logger = LogFactory.getLog(ContSalesExcel.class);
	
	public static String saveFromExcel(HttpServletRequest req, String filePath,
			ContService contService, ContSalesService contSalesService,
			ContVideoService contVideoService,
			ContVideoFileService contVideoFileService, RoleService roleService,
			StatusDictService statusDictService) {

		if (req == null || contService == null || contSalesService == null
				|| contVideoService == null || contVideoFileService == null
				|| roleService == null || statusDictService == null) {
			return null;
		}

		List<String> msgList = new ArrayList<String>();

		try {
			Sheet sheet = null;
			Workbook wb = ExcelUtil.paserWorkbook(filePath);

			if (wb != null) sheet = wb.getSheetAt(0);
			
			if (sheet != null) {
				Iterator<Row> rowI = sheet.iterator();
				int cellCount = 0;
				
				boolean verification = false;
				if (rowI.hasNext()) {
					Iterator<Cell> cellI = rowI.next().iterator();
					String cellVallue = "";
					while (cellI.hasNext()) {
						cellVallue = cellI.next().getRichStringCellValue().getString();
						verification = verificationFieldForSave(cellCount, cellVallue);
						cellCount++;
						
						if(!verification) {
							break;
						}
					}
				}
				
				if(!verification) {
					return "文件验证失败";
				}

				if (cellCount > 0) {

					Map<String, String> providerMap = new HashMap<String, String>();
					List<ContProvider> contProviderList = new ArrayList<ContProvider>();
					Operator operator = (Operator) req.getSession().getAttribute("user");
					contProviderList = roleService.queryProviderListWithAuth(operator, BmUtil.isAdminOperator(req));

					if (contProviderList != null && contProviderList.size() > 0) {
						for (ContProvider cp : contProviderList) {
							providerMap.put(cp.getId(), cp.getId());//id或名称均可
							providerMap.put(cp.getName(), cp.getId());
						}
					}

					Map<String, String> statusMap = new HashMap<String, String>();
					List<StatusDict> statusDictList = statusDictService
							.queryStatusDict("ZL_CONT", "STATUS");
					if (statusDictList != null && statusDictList.size() > 0) {
						for (StatusDict sd : statusDictList) {
							statusMap.put(sd.getDescription(), sd.getStatus());
						}
					}

					boolean issuc = true;
					int index = 0;
					while (rowI.hasNext()) {
						index++;

						Row row = rowI.next();

						issuc = true;
						String result = "";
						logger.info("contsales excel update 第" + index + "条数据");
						result += "第" + index + "条数据";
						Cont cont = new Cont();
						ContVideo contVideo = new ContVideo();
						ContentSalesBean contSales = new ContentSalesBean();
						ContentVideoFileBean videoFile1300 = new ContentVideoFileBean();
						ContentVideoFileBean videoFile2200 = new ContentVideoFileBean();
						ContentVideoFileBean videoFile3800 = new ContentVideoFileBean();
						List<String> priceMsgList = new ArrayList<String>();

                        parseSalesRow(row, providerMap, statusMap, cont, contVideo, contSales, videoFile1300, videoFile2200, videoFile3800, priceMsgList);
                		result += ",名称=" + cont.getName();
                		result += ",提供商=" + cont.getProvider_id();
                		result += ",商品编号=" + contSales.getSales_no();
                		result += ",销售价=" + contSales.getSale_price();

						result += " --> ";//结果
						if ("".equals(contSales.getSales_no())) {
							result += "商品编号为空";
							msgList.add(result);
							continue;
						}

						if ("".equals(cont.getProvider_id())) {
							result += "管理系统内没有此提供商";
							msgList.add(result);
							continue;
						}
						
						try {
							List<ContentSalesBean> csList = contSalesService
									.findBySalesNo(cont.getProvider_id(), contSales.getSales_no());
							if (csList != null && csList.size() > 0) {
								result += "此商品编号的数据已存在";
								msgList.add(result);
								continue;
							}

							String c_id = contService.saveAndReturnId(cont);

							if (c_id != null && !"".equals(c_id.trim())
									&& -1 < Integer.parseInt(c_id)) {
								cont.setId(c_id);
								contVideo.setC_id(c_id);
								contSales.setC_id(c_id);
								videoFile1300.setC_id(c_id);
								videoFile2200.setC_id(c_id);
								videoFile3800.setC_id(c_id);
								issuc = contVideoService.save(contVideo);

								if (issuc) {

									issuc = contSalesService.save(contSales);

									if (issuc) {
										if (priceMsgList.size() > 0) {
											result += "基本信息添加成功（"
													+ StringUtils.join(
															priceMsgList, "；")
													+ "）";
										} else {
											result += "基本信息添加成功";
										}

										ContentVideoFileBean videoFile = null;

										if (!"".equals(videoFile1300.getPlay_url())) {
											issuc = contVideoFileService
													.save(videoFile);

											if (issuc) {
												result += "\n标清播放信息添加成功";
											} else {
												result += "\n标清播放信息添加失败";
											}
										} else {
											result += "\n标清播放地址(1300)为空，添加失败";
										}

										if (!"".equals(videoFile2200.getPlay_url())) {
											issuc = contVideoFileService
													.save(videoFile);
											if (issuc) {
												result += "\n高清播放信息添加成功";
											} else {
												result += "\n高清播放信息添加失败";
											}
										} else {
											result += "\n高清播放地址(2200)为空，添加失败";
										}

										if (!"".equals(videoFile3800.getPlay_url())) {
											issuc = contVideoFileService
													.save(videoFile);
											if (issuc) {
												result += "\n超清播放信息添加成功";
											} else {
												result += "\n超清播放信息添加失败";
											}
										} else {
											result += "\n超清播放地址(3800)为空，添加失败";
										}
									} else {
										contService.delete(cont);
										contVideoService.delete(contVideo);
										result += "基本信息添加失败";
									}
								} else {
									contService.delete(cont);
									result += "基本信息添加失败";
								}
							} else {
								result += "基本信息添加失败";
							}
						} catch (Exception e) {
							result += " "+e.getMessage();
							e.printStackTrace();
						}

						msgList.add(result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msgList.size() > 0 ? StringUtils.join(msgList, "\n") : "上传成功";
	}
	private static void parseSalesRow(Row row, Map<String, String> providerMap, Map<String, String> statusMap
			, Cont cont, ContVideo contVideo, ContentSalesBean contSales
			, ContentVideoFileBean videoFile1300, ContentVideoFileBean videoFile2200, ContentVideoFileBean videoFile3800,
			List<String> priceMsgList){
		int c_status = 11;
		String provider_id = "";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date active_time = calendar.getTime();
		calendar.add(Calendar.YEAR, 50);
		Date deactive_time = calendar.getTime();

		String c_name = ExcelUtil.parserCell(row.getCell(0));
		logger.info("contsales excel update: 名称="+ c_name);
		String providerName = ExcelUtil.parserCell(row.getCell(1));
		if (!"".equals(providerName)) {
			provider_id = providerMap.get(providerName);
			if (provider_id == null) {
				provider_id = "";
			}
		}
		logger.info("contsales excel update: 提供商="
				+ providerName + ", id=" + provider_id);

		String cs_sales_no = ExcelUtil.parserCell(row.getCell(2));
		logger.info("contsales excel update: 商品编号="
				+ cs_sales_no);

		String status = ExcelUtil.parserCell(row.getCell(3));
		String statusName = status;
		if (!"".equals(status)) {
			status = statusMap.get(status);
			if (status != null && Integer.parseInt(status) > -1) {
				c_status = Integer.parseInt(status);
			}
		}
		logger.info("contsales excel update: 状态="
				+ statusName + ", id=" + c_status);

		String cv_alias = ExcelUtil.parserCell(row.getCell(4));
		logger.info("contsales excel update: 别名="
				+ cv_alias);

		String cs_fake_price = ExcelUtil.parserCell(row
				.getCell(5));
		logger.info("contsales excel update: 市场价="
				+ cs_fake_price);

		String cs_sale_price = ExcelUtil.parserCell(row
				.getCell(6));
		logger.info("contsales excel update: 销售价="
				+ cs_sale_price);

		String cs_post_price = ExcelUtil.parserCell(row
				.getCell(7));
		logger.info("contsales excel update: 运费="
				+ cs_post_price);

		if ("".equals(cs_fake_price)) {
			priceMsgList.add("市场价格为空默认为0");
		}
		if ("".equals(cs_sale_price)) {
			priceMsgList.add("销售价格为空默认为0");
		}
		if ("".equals(cs_post_price)) {
			priceMsgList.add("运费为空默认为0");
		}

		String cs_disaccount = "10";
		if (!"".equals(cs_fake_price)
				&& !"".equals(cs_sale_price)) {
			float fp = Float.parseFloat(cs_fake_price);
			float sp = Float.parseFloat(cs_sale_price);

			float disaccount = 10;
			if (fp > 0) {
				disaccount = (sp * 10) / fp;
			}

			cs_disaccount = ""
					+ (Math.round(disaccount * 100) / 100f);
		}

		logger.info("contsales excel update: 折扣="
				+ cs_post_price);

		String cs_cp_name = ExcelUtil
				.parserCell(row.getCell(8));
		logger.info("contsales excel update: 经销商-正标题="
				+ cs_cp_name);

		String cs_sub_cp_name = ExcelUtil.parserCell(row
				.getCell(9));
		logger.info("contsales excel update: 经销商-副标题="
				+ cs_sub_cp_name);

		String locked = ExcelUtil.parserCell(row.getCell(10));

		String is_locked = "1";
		if ("1".equals(locked.trim())) {
			is_locked = "1";
		} else {
			is_locked = "0";
		}

		logger.info("contsales excel update: 基本信息锁定="
				+ locked);

		String cs_key_words = ExcelUtil.parserCell(row
				.getCell(11));
		cs_key_words = cs_key_words.replaceAll("，", ",");
		logger.info("contsales excel update: 关键词="
				+ cs_key_words);

		String cs_pay_type_ids = ExcelUtil.parserCell(row
				.getCell(12));
		cs_pay_type_ids = cs_pay_type_ids.replaceAll("，", ",");
		logger.info("contsales excel update: 支付方式="
				+ cs_pay_type_ids);

		String cv_play_url = ExcelUtil.parserCell(row
				.getCell(13));
		logger.info("contsales excel update: 视频播放地址="
				+ cv_play_url);

		String cs_hot_info = ExcelUtil.parserCell(row
				.getCell(14));
		logger.info("contsales excel update: 商品信息="
				+ cs_hot_info);

		String play_url_1300 = ExcelUtil.parserCell(row
				.getCell(15));
		logger.info("contsales excel update: 标清播放地址(1300)="
						+ play_url_1300);

		String play_url_2200 = ExcelUtil.parserCell(row
				.getCell(16));
		logger.info("contsales excel update: 高清播放地址(2200)="
						+ play_url_2200);

		String play_url_3800 = ExcelUtil.parserCell(row
				.getCell(17));
		logger.info("contsales excel update: 超清播放地址(3800)="
						+ cs_hot_info);
		
		
		cont.setName(c_name);
		cont.setPinyin(PinyinUtil
				.getHeadStringWithoutAnySymbol(c_name));
		cont.setStatus(c_status);
		cont.setDescription("");
		cont.setType(8);
		cont.setProvider_id(provider_id);
		cont.setActive_time(active_time);
		cont.setDeactive_time(deactive_time);
		cont.setLocked(is_locked);

		//contVideo.setC_id(c_id);
		contVideo.setName(c_name);
		contVideo.setAlias(cv_alias);
		contVideo.setDescription("");
		contVideo.setProvider_id(provider_id);
		contVideo.setPackage_name("");
		contVideo.setSuperscript_id("");
		contVideo.setVol_update_time(new Date());
		contVideo.setPlay_url(cv_play_url);
		
		//contSales.setC_id(c_id);
		contSales.setProvider_id(provider_id);
		contSales.setName(c_name);
		contSales.setCp_name(cs_cp_name);
		contSales.setSub_cp_name(cs_sub_cp_name);
		contSales.setDisaccount(cs_disaccount);
		contSales.setFake_price(cs_fake_price);
		contSales.setSale_price(cs_sale_price);
		contSales.setPost_price(cs_post_price);
		contSales.setHot_info(cs_hot_info);
		contSales.setKey_words(cs_key_words);
		contSales.setPay_type_ids(cs_pay_type_ids);
		contSales.setSales_no(cs_sales_no);
		
		//videoFile1300.setC_id(c_id);
		videoFile1300.setOrder_num("1");
		videoFile1300.setBit_rate("1300");
		videoFile1300.setRate_tag("标清");
		videoFile1300.setRate_tag_eng("sd");
		videoFile1300.setProvider_id(provider_id);
		videoFile1300.setPlay_url(play_url_1300);
		
		//videoFile2200.setC_id(c_id);
		videoFile2200.setOrder_num("1");
		videoFile2200.setBit_rate("2200");
		videoFile2200.setRate_tag("高清");
		videoFile2200.setRate_tag_eng("hd");
		videoFile2200.setProvider_id(provider_id);
		videoFile2200.setPlay_url(play_url_2200);
		
		//videoFile3800.setC_id(c_id);
		videoFile3800.setOrder_num("1");
		videoFile3800.setBit_rate("3800");
		videoFile3800.setRate_tag("超清");
		videoFile3800.setRate_tag_eng("gd");
		videoFile3800.setProvider_id(provider_id);
		videoFile3800.setPlay_url(play_url_3800);
	}
	public static String updateFromExcel(HttpServletRequest req, String filePath,
			ContSalesService contSalesService, String provider_id) {

		if (req == null || contSalesService == null) {
			return null;
		}

		List<String> msgList = new ArrayList<String>();

		try {
			Sheet sheet = null;
			Workbook wb = ExcelUtil.paserWorkbook(filePath);

			if (wb != null) sheet = wb.getSheetAt(0);
			
			if (sheet != null) {
				Iterator<Row> rowI = sheet.iterator();
				int cellCount = 0;
				
				boolean verification = false;
				if (rowI.hasNext()) {
					Iterator<Cell> cellI = rowI.next().iterator();
					String cellVallue = "";
					while (cellI.hasNext()) {
						cellVallue = cellI.next().getRichStringCellValue().getString();
						verification = verificationFieldForUpdate(cellCount, cellVallue);
						cellCount++;
						
						if(!verification) {
							break;
						}
					}
				}
				
				if(!verification) {
					return "文件验证失败";
				}

				if (cellCount > 0) {

					boolean issuc = true;
					int index = 0;
					while (rowI.hasNext()) {
						index++;
						Row row = rowI.next();

						issuc = true;
						String result = "";
						result += "第" + index + "条数据";
						logger.info("contsales excel update 第" + index + "条数据");
						String sales_no = ExcelUtil.parserCell(row.getCell(0));
						result += ",商品编号=" + sales_no;
						
						String price = ExcelUtil.parserCell(row.getCell(2));
						result += ",商品价格=" + price;
						
						String sum_stock = ExcelUtil.parserCell(row.getCell(3));
						result += ",库存数量=" + sum_stock;

						result += " --> ";//结果

						if(StringUtils.isBlank(sales_no)) {
							result += "商品编号为空，修改失败";
							logger.info(result);
							msgList.add(result);
							continue;
						}
						
						String msg = "修改失败";
						try {
							List<ContentSalesBean> list = contSalesService.findBySalesNo(provider_id, sales_no);
							if(list != null && list.size() > 0) {
								issuc = contSalesService.updateSumStock(provider_id, sales_no, sum_stock);
								
								if(issuc) msg = "修改成功";
							} else {
								msg = "商品不存在，修改失败";
							}
						} catch (Exception e) {
							msg += " "+e.getMessage();
							e.printStackTrace();
						}
						result += msg;

						logger.info(result);
						msgList.add(result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msgList.size() > 0 ? StringUtils.join(msgList, "\n") : "文件为空";
	}

	private static boolean verificationFieldForSave(int cellCount, String cellVallue) {
		boolean verification = true;
		switch(cellCount) {
		case 0:
			if(cellVallue == null || !"名称".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 1:
			if(cellVallue == null || !"提供商".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 2:
			if(cellVallue == null || !"商品编号".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 3:
			if(cellVallue == null || !"状态".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 4:
			if(cellVallue == null || !"别名".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 5:
			if(cellVallue == null || !"市场价".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 6:
			if(cellVallue == null || !"销售价".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 7:
			if(cellVallue == null || !"运费".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 8:
			if(cellVallue == null || !"经销商-正标题".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 9:
			if(cellVallue == null || !"经销商-副标题".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 10:
			if(cellVallue == null || !"基本信息锁定".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 11:
			if(cellVallue == null || !"关键词".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 12:
			if(cellVallue == null || !"支付方式".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 13:
			if(cellVallue == null || !"视频播放地址".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 14:
			if(cellVallue == null || !"商品信息".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 15:
			if(cellVallue == null || !"标清播放地址(1300)".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 16:
			if(cellVallue == null || !"高清播放地址(2200)".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 17:
			if(cellVallue == null || !"超清播放地址(3800)".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		}
		
		return verification;
	}
	
	private static boolean verificationFieldForUpdate(int cellCount, String cellVallue) {
		boolean verification = true;
		switch(cellCount) {
		case 0:
			if(cellVallue == null || !"sku".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 1:
			if(cellVallue == null || !"product_name".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 2:
			if(cellVallue == null || !"price".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		case 3:
			if(cellVallue == null || !"sum_stock".equals(cellVallue.trim())) {
				verification = false;
			}
			break;
		}
		
		return verification;
	}
}
