
package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ContentSalesPayTypeDao;
import com.onewave.backstage.model.ContentSalesBean.PayType;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contentSalesPayTypeDao")
public class ContentSalesPayTypeDaoImpl extends BaseDaoImpl<PayType, String> implements ContentSalesPayTypeDao
{

	@Override
   public boolean isUsing(String id) {

	   String sql = "select count(*) tcount from dual where exists(select c_id from zl_cont_sales where '"+id+"' in pay_type_ids)";
	   logger.info("------------sql:" + sql);
	   int iRows =countBySql(sql);

		return iRows>0;
	}
	@Override
	public List<PayType> findByIds(String ids) {
		List<PayType> contsalesList = new ArrayList<PayType>();
		if(ids!=null && ids.trim().length()>0) {
			String sql = "select * from  zl_sales_pay_type c where c.ID in (" + ids +")";
			logger.info("------------sql:" + sql);
			contsalesList = findAllBySql(sql);
		}
		return contsalesList;
		
	}

}
