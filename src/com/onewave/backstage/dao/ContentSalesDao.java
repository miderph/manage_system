package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.model.ContentSalesBean.PayType;
import com.onewave.common.dao.IBaseDao;


/**
 * 
 * @author liuhaidi
 *
 */
public interface ContentSalesDao extends IBaseDao<ContentSalesBean, String>{
	
	List<ContentSalesBean> findByIds(String ids);
	public List<ContentSalesBean> findBySalesNo(String provider_id, String salesNo);
	public boolean updateSumStock(String provider_id, String sales_no, String sum_stock);

	//List<ContentSalesBean> findbyid(String id);
}
