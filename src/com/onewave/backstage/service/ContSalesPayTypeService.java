
package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.model.ContentSalesBean.PayType;

public interface ContSalesPayTypeService
{

	public boolean save(PayType contVideo);
	
	public boolean update(PayType contVideo);
	
	public boolean delete(String id);
   public boolean isUsing(String id);

	public List<PayType> findAll();
	public List<PayType> findByIds(String ids);
	
	public PayType findById(String id);

}
