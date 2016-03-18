package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.RelaMenuAndCont;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface RelaMenuAndContService {
	
	public boolean save(RelaMenuAndCont relaMenuAndCont);
	
	public boolean update(RelaMenuAndCont relaMenuAndCont);
	
	public boolean delete(RelaMenuAndCont relaMenuAndCont);
	
	public boolean deleteById(String id);
	
	public RelaMenuAndCont findById(String id);
	
	public RelaMenuAndCont findById(String id, String menuId);
	
	public RelaMenuAndCont find(String menuId, String orderNum);
	
	public int countAll();
	
	public int countAll(String where);

	public int countBySql(String sql);
	
	public List<RelaMenuAndCont> findAll();
	
	public List<RelaMenuAndCont> findAll(String where);
	
	public List<RelaMenuAndCont> findAllForMAR(String menuId);
	
	public List<RelaMenuAndCont> findAllForMAR(int firstResult, int maxResults, String menuId, int status, int status_type);

	public int countAllForMAR(String menuId, int status, int status_type);
	
	public void deleteDuplicateOrderNum(String menuId);
	public int getMinOrderNum(String menuId, int status, int status_type);
	public int getMaxOrderNum(String menuId, int status, int status_type);
	public int getPrevOrderNum(String menuId, String orderNum, int status, int status_type);
	public int getNextOrderNum(String menuId, String orderNum, int status, int status_type);

	public boolean deleteRelaMenuAndCont(String menuId, String ids);
	public boolean lockRelaMenuAndCont(String menuId, String ids, String locked);
//	public boolean deleteInvalidRelaMenuAndCont(String menuId);

	public List<RelaMenuAndCont> findby_id(String id);
	
}
