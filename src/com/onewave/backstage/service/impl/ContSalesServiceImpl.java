package com.onewave.backstage.service.impl;

import java.util.List;

import net.zhilink.tools.InitManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onewave.backstage.dao.ContChecksDao;
import com.onewave.backstage.dao.ContentSalesDao;
import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.service.ContChecksService;
import com.onewave.backstage.service.ContSalesService;

@Service("contSalesService")
public class ContSalesServiceImpl implements ContSalesService {
	@Autowired
	@Qualifier("contentSalesDao")
	private ContentSalesDao contentSalesDao;

	@Autowired
	@Qualifier("contChecksDao")
	private ContChecksDao contChecksDao;
	
	@Autowired
	@Qualifier("contChecksService")
	private ContChecksService contChecksService;
	
	public ContentSalesDao getContentSalesDao() {
		return contentSalesDao;
	}

	public void setContentSalesDao(ContentSalesDao contentSalesDao) {
		this.contentSalesDao = contentSalesDao;
	}

	public boolean save(ContentSalesBean contVideo) {
		return contentSalesDao.save(contVideo);
	}

	public boolean update(ContentSalesBean contVideo) {
		boolean flag = contentSalesDao.update(contVideo);
		return flag;
	}

	@Transactional
	public boolean delete(String id) {
		ContentSalesBean salesBean = contentSalesDao.findById(id);
		boolean flag = contentSalesDao.deleteById(id);
		if (flag) {
			// 天猫商品删除时，联动检查是淘宝客商品，联动禁用
			if (salesBean!=null && InitManager.PROVIDER_TMALL.equals(salesBean.getProvider_id())) {
				flag = contChecksService.underShelf(salesBean.getSales_no());
			}
		}
		return flag;
	}

	@Override
	public List<ContentSalesBean> findByIds(String ids) {
		return contentSalesDao.findByIds(ids);
	}

	@Override
	public ContentSalesBean findById(String id) {
		return contentSalesDao.findById(id);
	}

	@Override
	public List<ContentSalesBean> findBySalesNo(String provider_id,
			String salesNo) {
		return contentSalesDao.findBySalesNo(provider_id, salesNo);
	}

	@Override
	public boolean updateSumStock(String provider_id, String sales_no,
			String sum_stock) {
		return contentSalesDao.updateSumStock(provider_id, sales_no, sum_stock);
	}
}
