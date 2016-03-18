package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Cont;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface ContService {

	public boolean save(Cont cont);
	public String saveAndReturnId(Cont cont);
	public boolean update(Cont cont);
	public boolean delete(Cont cont);
	public boolean delete(String id);
	
	public int countAll();
	public List<Cont> findAll();
	
	public int countAll(String providerIds, String type, String status, String name);
	public List<Cont> findAll(int firstResult, int maxResults, String providerIds, String type, String status, String name);
	public List<Cont> findAll(int firstResult, int maxResults);
	public List<Cont> findAllByPage(int pageNum, int pageSize, String type, String status, String name);
	
	public Cont findById(String id);
	public List<Cont> findAllInIds(String ids);
	
	public int countAllForApp(String providerIds,String type, String status, String name);
	public List<Cont> findAllForApp(int firstResult, int maxResults, String type, String status, String name,String providerIds);

	public int countAllForSales(String providerIds,String type, String status, String name, String price_from, String price_to);
	public List<Cont> findAllForSales(int firstResult, int maxResults, String type, String status, String name,String providerIds, String price_from, String price_to);

	public List<Cont> findAllForMAR(int firstResult, int maxResults, String type, String provider, String keyword, String status, String startTime,String endTime, String menuId);
	public int countAllForMAR(String type, String provider, String keyword, String status,String startTime, String endTime, String menuId);
	
	public List<Cont> findAllByRoleForMAR(int firstResult, int maxResults,String providers);
	public int countAllByRoleForMAR(String providers);

	public List<Cont> findAllSuperscripts();
	public Cont findAppByPackageName(String packageName,String providerId);
	
}
