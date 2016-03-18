
package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.ContAppstore;
import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.model.ContentSalesBean.PayType;

public interface ContSalesService
{

	public boolean save(ContentSalesBean contVideo);
	
	public boolean update(ContentSalesBean contVideo);
	
	public boolean delete(String id);
	
	public List<ContentSalesBean> findByIds(String ids);
	
	public ContentSalesBean findById(String id);
	
	public List<ContentSalesBean> findBySalesNo(String provider_id, String salesNo);
	
	public boolean updateSumStock(String provider_id, String sales_no, String sum_stock);

}
