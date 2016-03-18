package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.RelaMenuAndContDao;
import com.onewave.backstage.model.RelaMenuAndCont;
import com.onewave.backstage.service.RelaMenuAndContService;

@Service("relaMenuAndContService")
public class RelaMenuAndContServiceImpl implements RelaMenuAndContService {
	
	@Autowired
	@Qualifier("relaMenuAndContDao")
	private RelaMenuAndContDao relaMenuAndContDao;

	public boolean save(RelaMenuAndCont relaMenuAndCont) {
		relaMenuAndCont.setDate_time(new Date());
		return relaMenuAndContDao.save(relaMenuAndCont);
	}

	public boolean update(RelaMenuAndCont relaMenuAndCont) {
		return relaMenuAndContDao.update(relaMenuAndCont);
	}

	public boolean delete(RelaMenuAndCont relaMenuAndCont) {
		return relaMenuAndContDao.delete(relaMenuAndCont);
	}

	public boolean deleteById(String id) {
		return relaMenuAndContDao.deleteById(id);
	}

	public RelaMenuAndCont findById(String id) {
		return relaMenuAndContDao.findById(id);
	}

	public RelaMenuAndCont findById(String id, String menuId) {
		return relaMenuAndContDao.findById(id, menuId);
	}

	public int countAll() {
		return relaMenuAndContDao.countAll();
	}

	public int countAll(String where) {
		return relaMenuAndContDao.countAll(where);
	}

	public int countBySql(String sql) {
		return relaMenuAndContDao.countBySql(sql);
	}

	public List<RelaMenuAndCont> findAll() {
		return relaMenuAndContDao.findAll();
	}

	public List<RelaMenuAndCont> findAll(String where) {
		return relaMenuAndContDao.findAll(where, null);
	}

	public List<RelaMenuAndCont> findAllForMAR(String menuId) {
		return relaMenuAndContDao.findAllForMAR(menuId);
	}

	public List<RelaMenuAndCont> findAllForMAR(int firstResult, int maxResults, String menuId, int status, int status_type) {
		return relaMenuAndContDao.findAllForMAR(firstResult, maxResults, menuId, status, status_type);
	}

	public int countAllForMAR(String menuId, int status, int status_type) {
		return relaMenuAndContDao.countAllForMAR(menuId, status, status_type);
	}
	public void deleteDuplicateOrderNum(String menuId) {
		relaMenuAndContDao.deleteDuplicateOrderNum(menuId);
	}	
	public int getMinOrderNum(String menuId, int status, int status_type) {
		return relaMenuAndContDao.getMinOrderNum(menuId, status, status_type);
	}	
	public int getMaxOrderNum(String menuId, int status, int status_type) {
		return relaMenuAndContDao.getMaxOrderNum(menuId, status, status_type);
	}
	public int getPrevOrderNum(String menuId, String orderNum, int status, int status_type){
		return relaMenuAndContDao.getPrevOrderNum(menuId,orderNum, status, status_type);
	}
	public int getNextOrderNum(String menuId, String orderNum, int status, int status_type){
		return relaMenuAndContDao.getNextOrderNum(menuId,orderNum, status, status_type);
	}
	public boolean deleteRelaMenuAndCont(String menuId, String ids) {
		return relaMenuAndContDao.deleteRelaMenuAndCont(menuId, ids);
	}
	public boolean lockRelaMenuAndCont(String menuId, String ids, String locked) {
		return relaMenuAndContDao.lockRelaMenuAndCont(menuId, ids, locked);
	}
//	public boolean deleteInvalidRelaMenuAndCont(String menuId){
//		return relaMenuAndContDao.deleteInvalidRelaMenuAndCont(menuId);
//	}

	public RelaMenuAndCont find(String menuId, String orderNum) {
		return relaMenuAndContDao.find(menuId, orderNum);
	}

	@Override
	public List<RelaMenuAndCont> findby_id(String id) {
		return relaMenuAndContDao.findby_id(id);
	}
}
