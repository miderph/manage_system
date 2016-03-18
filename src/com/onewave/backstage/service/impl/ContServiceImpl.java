package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onewave.backstage.dao.ContDao;
import com.onewave.backstage.dao.ContentSalesDao;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContentSalesBean;
import com.onewave.backstage.service.ContChecksService;
import com.onewave.backstage.service.ContService;

@Service("contService")
public class ContServiceImpl implements ContService {
	
	@Autowired
	@Qualifier("contDao")
	private ContDao contDao;
	
	@Autowired
	@Qualifier("contentSalesDao")
	private ContentSalesDao  contentSalesDao;
	
	@Autowired
	@Qualifier("contChecksService")
	private ContChecksService contChecksService;
	
	public List<Cont> findAllInIds(String ids) {
		return contDao.findAllInIds(ids);
	}
	
	public boolean save(Cont cont) {
		return contDao.save(cont);
	}
	
	public String saveAndReturnId(Cont cont) {
		return contDao.saveAndReturnId(cont);
	}
	
	@Transactional
	public boolean update(Cont cont) {
		cont.setModify_time(new Date());
		boolean flag = contDao.update(cont);
		if(InitManager.PROVIDER_TMALL.equals(cont.getProvider_id()) && cont.getStatus()==-1){//禁用的
			ContentSalesBean  salesBean = contentSalesDao.findById(cont.getId());
			if(salesBean != null){
				flag = contChecksService.underShelf(salesBean.getSales_no());
			}
		}
		return flag;
	}
	
	public boolean delete(Cont cont) {
		return contDao.delete(cont);
	}
	
	public boolean delete(String id) {
		return contDao.deleteById(id);
	}
	
	public int countAll() {
		
		return contDao.countAll();
	}
	
	public int countAll(String providerIds, String type, String status, String name) {
		return contDao.countAll(providerIds, type, status, name);
	}
	
	public Cont findById(String id) {
		return contDao.findById(id);
	}
	
	public List<Cont> findAll() {
		return contDao.findAll();
	}
	
	public List<Cont> findAll(int firstResult, int maxResults) {
		return contDao.findAll(firstResult, maxResults);
	}

	public List<Cont> findAllByPage(int pageNum, int pageSize, String type,
			String status, String name) {
		return contDao.findAllByPage(pageNum, pageSize, type, status, name);
	}

	public List<Cont> findAll(int firstResult, int maxResults, String providerIds, String type,
			String status, String name) {
		return contDao.findAll(firstResult, maxResults, providerIds, type, status, name);
	}

	public int countAllForApp(String providerIds,String type, String status, String name) {
		return contDao.countAllForApp(providerIds,type, status, name);
	}

	public List<Cont> findAllForApp(int firstResult, int maxResults,
			String type, String status, String name,String providerId) {
		return contDao.findAllForApp(firstResult, maxResults, type, status, name,providerId);
	}
	public int countAllForSales(String providerIds,String type, String status, String name, String price_from, String price_to) {
		return contDao.countAllForSales(providerIds,type, status, name, price_from, price_to);
	}

	public List<Cont> findAllForSales(int firstResult, int maxResults,
			String type, String status, String name,String providerId, String price_from, String price_to) {
		return contDao.findAllForSales(firstResult, maxResults, type, status, name,providerId, price_from, price_to);
	}
	public List<Cont> findAllForMAR(int firstResult, int maxResults,
			String type, String provider, String keyword, String status, String startTime,
			String endTime, String menuId) {
		return contDao.findAllForMAR(firstResult, maxResults, type, provider, keyword, status, startTime, endTime, menuId);
	}

	public int countAllForMAR(String type, String provider, String keyword, String status,
			String startTime, String endTime, String menuId) {
		return contDao.countAllForMAR(type, provider, keyword, status, startTime, endTime, menuId);
	}

	@Override
	public int countAllByRoleForMAR(String provider) {
		// TODO Auto-generated method stub
		return  contDao.countAllByRoleForMAR(provider);
	}

	@Override
	public List<Cont> findAllByRoleForMAR(int firstResult, int maxResults,
			String providers) {
		// TODO Auto-generated method stub
		return  contDao.findAllByRoleForMAR(firstResult, maxResults, providers);
	}

	@Override
	public List<Cont> findAllSuperscripts() {
		return  contDao.findAllSuperscripts();
	}

	@Override
	public Cont findAppByPackageName(String packageName,
			String providerId) {
		List<Cont> conts = contDao.findAllAppByPackageName(packageName, providerId);
		if( conts !=null && conts.size()>0){
			return conts.get(0);
		}else{
			return null;
		}
	}
}
