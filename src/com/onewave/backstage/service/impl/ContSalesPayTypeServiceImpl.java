
package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContentSalesPayTypeDao;
import com.onewave.backstage.model.ContentSalesBean.PayType;
import com.onewave.backstage.service.ContSalesPayTypeService;

@Service("contSalesPayTypeService")
public class ContSalesPayTypeServiceImpl implements ContSalesPayTypeService
{
	@Autowired
	@Qualifier("contentSalesPayTypeDao")
   private ContentSalesPayTypeDao contentSalesPayTypeDao;


   public ContentSalesPayTypeDao getContentSalesPayTypeDao() {
	   return contentSalesPayTypeDao;
   }

   public void setContentSalesPayTypeDao(ContentSalesPayTypeDao contentSalesPayTypeDao) {
	   this.contentSalesPayTypeDao = contentSalesPayTypeDao;
   }

	public boolean save(PayType contVideo) {
		return contentSalesPayTypeDao.save(contVideo);
	}
	
	public boolean update(PayType contVideo) {
		return contentSalesPayTypeDao.update(contVideo);
	}
	public boolean delete(String id) {
		return contentSalesPayTypeDao.deleteById(id);
	}
   public boolean isUsing(String id) {
		return contentSalesPayTypeDao.isUsing(id);
	}
	@Override
	public List<PayType> findAll() {
		return contentSalesPayTypeDao.findAll();
	}
	@Override
	public List<PayType> findByIds(String ids) {
		return contentSalesPayTypeDao.findByIds(ids);
	}

	@Override
	public PayType findById(String id) {
		return contentSalesPayTypeDao.findById(id);
	}

}
