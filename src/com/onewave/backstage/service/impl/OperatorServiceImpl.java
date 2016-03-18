package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.OperatorDao;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.service.OperatorService;

@Service("operatorService")
public class OperatorServiceImpl implements OperatorService {
	
	@Autowired
	@Qualifier("operatorDao")
	private OperatorDao operatorDao;
	
	@Override
	public boolean save(Operator operator) {
		operator.setCreate_time(new Date());
		operator.setModify_time(new Date());
		return operatorDao.save(operator);
	}
	
	@Override
	public boolean update(Operator operator) {
		operator.setModify_time(new Date());
		return operatorDao.update(operator);
	}
	
	@Override
	public boolean delete(String id) {
		return operatorDao.deleteById(id);
	}
	
	@Override
	public Operator findById(String id) {
		return operatorDao.findById(id);
	}
	
	@Override
	public Operator findByName(String name) {
		return operatorDao.findByName(name);
	}
	
	@Override
	public int countAll() {
		return operatorDao.countAll();
	}
	
	@Override
	public List<Operator> findAll(int firstResult, int maxResults) {
		return operatorDao.findAll(firstResult, maxResults);
	}

	@Override
	public boolean isExist(String name) {
		String where= " name='"+ name + "' ";
		
		int count = operatorDao.countAll(where);
		
		return count >= 1 ? true :false;
	}
	
	@Override
	public boolean roleIsUsed(String id) {
		String sql= "select * from zl_operators t where t.role_ids like '%"+id+"%'";
		List<Operator>  operators = operatorDao.findAllBySql(sql);
		if(operators!=null && operators.size() >0){
			return true;
		}
		return false;
	}
}
