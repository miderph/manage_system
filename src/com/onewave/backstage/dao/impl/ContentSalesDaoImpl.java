package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ContentSalesDao;
import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.model.ContentSalesBean.PayType;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contentSalesDao")
public class ContentSalesDaoImpl extends BaseDaoImpl<ContentSalesBean, String>
		implements ContentSalesDao {
	@Override
	public List<ContentSalesBean> findByIds(String ids) {
		List<ContentSalesBean> contsalesList = new ArrayList<ContentSalesBean>();
		if (ids != null && ids.trim().length() > 0) {
			String sql = "select * from  zl_cont_sales c where c.C_ID in ("
					+ ids + ")";
			logger.info("------------sql:" + sql);
			contsalesList = findAllBySql(sql);
		}
		return contsalesList;

	}

	@Override
	public List<ContentSalesBean> findBySalesNo(String provider_id,
			String salesNo) {
		List<ContentSalesBean> contsalesList = new ArrayList<ContentSalesBean>();
		String sql = "select * from zl_cont cont,zl_cont_sales c where cont.provider_id='"
				+ provider_id + "' and cont.id=c.c_id and c.sales_No = '" + salesNo + "'";
		logger.info("------------sql:" + sql);
		contsalesList = findAllBySql(sql);

		return contsalesList;

	}

	// @Override
	// public List<ContentSalesBean> findbyid(String id) {
	// List<ContentSalesBean> contsalesList = new ArrayList<ContentSalesBean>();
	// if(id!=null && id.trim().length()>0) {
	// String sql = "select * from zl_cont_sales c where c.c_id =(" + id +")";
	// logger.info("------------sql:" + sql);
	// contsalesList = findAllBySql(sql);
	// }
	// return contsalesList;
	// }

	@Override
	public boolean updateSumStock(String provider_id, String sales_no, String sum_stock) {

		int sum = 0;

		if (StringUtils.isNotBlank(sum_stock) && StringUtils.isNumeric(sum_stock)) {
			sum = Integer.parseInt(sum_stock);
		}

		String sql = "update zl_cont_sales set sum_stock=" + sum
				+ " where provider_id='" + provider_id + "' and sales_no = '"
				+ sales_no + "'";

		return updateBySql(sql);
	}

}
