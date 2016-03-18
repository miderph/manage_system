package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Cont;
import com.onewave.common.dao.IBaseDao;

public interface ContDao extends IBaseDao<Cont, String> {
	
	public List<Cont> findAllInIds(String ids);

	public String saveAndReturnId(Cont cont);

	public int countAll(String providerId, String type, String status, String name);

	public List<Cont> findAllByPage(int pageNum, int pageSize, String type,
			String status, String name);
	public List<Cont> findAll(int firstResult, int maxResults, String providerId, String type,
			String status, String name);

	public int countAllForApp(String providerIds,String type, String status, String name);
	public List<Cont> findAllForApp(int firstResult, int maxResults,
			String type, String status, String name,String providerId);

	public int countAllForSales(String providerIds,String type, String status, String name, String price_from, String price_to);
	public List<Cont> findAllForSales(int firstResult, int maxResults,
			String type, String status, String name,String providerId, String price_from, String price_to);

	public List<Cont> findAllForMAR(int firstResult, int maxResults,
			String type, String provider, String keyword, String status, String startTime,
			String endTime, String menuId);
	public int countAllForMAR(String type, String provider, String keyword, String status,
			String startTime, String endTime, String menuId);

	public List<Cont> findAllByRoleForMAR(int firstResult, int maxResults,
			String providers);

	public int countAllByRoleForMAR(String providers);

	public List<Cont> findAllSuperscripts();
	
	public List<Cont> findAllAppByPackageName(String packageName,String providerId);
}
