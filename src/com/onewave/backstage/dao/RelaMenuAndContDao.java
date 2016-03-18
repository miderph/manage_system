package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.RelaMenuAndCont;
import com.onewave.common.dao.IBaseDao;

public interface RelaMenuAndContDao extends IBaseDao<RelaMenuAndCont, String> {

	public List<RelaMenuAndCont> findAllForMAR(String menuId);

	public int countAllForMAR(String menuId, int status, int status_type);
	public List<RelaMenuAndCont> findAllForMAR(int firstResult, int maxResults, String menuId, int status, int status_type);
	
	public void deleteDuplicateOrderNum(String menuId);
	public int getMinOrderNum(String menuId, int status, int status_type);
	public int getMaxOrderNum(String menuId, int status, int status_type);
	public int getPrevOrderNum(String menuId, String orderNum, int status, int status_type);
	public int getNextOrderNum(String menuId, String orderNum, int status, int status_type);

	public RelaMenuAndCont findById(String id, String menuId);

	public boolean deleteRelaMenuAndCont(String menuId, String ids);
	public boolean lockRelaMenuAndCont(String menuId, String ids, String locked);
//	public boolean deleteInvalidRelaMenuAndCont(String menuId);

	public RelaMenuAndCont find(String menuId, String orderNum);

	public List<RelaMenuAndCont> findby_id(String id);

}
