package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContentSalesBean.PayType;
import com.onewave.common.dao.IBaseDao;


/**
 * 
 * @author liuhaidi
 *
 */
public interface ContentSalesPayTypeDao extends IBaseDao<PayType, String>{
	
   public boolean isUsing(String id);
	List<PayType> findByIds(String ids);
}
