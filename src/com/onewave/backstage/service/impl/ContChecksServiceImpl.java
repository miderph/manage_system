package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContChecksDao;
import com.onewave.backstage.model.ContCheck;
import com.onewave.backstage.service.ContChecksService;
import com.onewave.common.dao.Pagination;

@Service("contChecksService")
public class ContChecksServiceImpl implements ContChecksService {
	
	@Autowired
	@Qualifier("contChecksDao")
	ContChecksDao dao;
	
	@Override
	public boolean save(ContCheck entity) {
		entity.setCreate_time(new Date());
		entity.setUpdate_time(new Date());
		return dao.save(entity);
	}

	@Override
	public String saveAndReturnId(ContCheck entity) {
		entity.setCreate_time(new Date());
		entity.setUpdate_time(new Date());
		return dao.saveAndReturnId(entity);
	}

	@Override
	public boolean update(ContCheck entity) {
		entity.setUpdate_time(new Date());
		return dao.update(entity);
	}

	@Override
	public boolean delete(ContCheck entity) {
		return dao.delete(entity);
	}

	@Override
	public boolean deleteById(String id) {
		return dao.deleteById(id);
	}

	@Override
	public ContCheck findById(String id) {
		return dao.findById(id);
	}

	@Override
	public int countAll() {
		return dao.countAll();
	}

	@Override
	public int countAll(String where) {
		return dao.countAll(where);
	}

	@Override
	public int countBySql(String sql) {
		return dao.countBySql(sql);
	}

	@Override
	public List<ContCheck> findAll() {
		return dao.findAll();
	}

	@Override
	public List<ContCheck> findAll(String where, String order) {
		return dao.findAll(where, order);
	}

	@Override
	public List<ContCheck> findAllBySql(String sql) {
		return dao.findAllBySql(sql);
	}

	@Override
	public List<ContCheck> findAllByPage(int pageNum, int pageSize) {
		return dao.findAllByPage(pageNum, pageSize);
	}

	@Override
	public List<ContCheck> findAllByPage(int pageNum, int pageSize, String where) {
		return dao.findAllByPage(pageNum, pageSize, where);
	}

	@Override
	public String getSplitFieldSql(String orgi_set_sql) {
		return dao.getSplitFieldSql(orgi_set_sql);
	}

	@Override
	public ContCheck findByItemUrl(String itemUrl) {
		return dao.findByItemUrl(itemUrl);
	}

	@Override
	public Pagination<ContCheck> findByPage(int pagenum) {
		
		return dao.findPagination(pagenum,25,""," update_time desc");
	}
	
	
	@Override
	public boolean underShelf(String salesNo) {
		boolean flag = false;
		List<ContCheck> list =dao.findAll("code = '"+salesNo+"'");
		if(list !=null && list.size() > 0){
			ContCheck check = list.get(0);
			check.setStatus(ContCheck.EnumStatus.UnderShelf.getValue());
			flag = dao.update(check);
		}
		return flag;
	}

	public Pagination<ContCheck> findPagination(int pageNum, int pageSize,String c_status,
			String c_name, String price_from, String price_to,
			String start_time, String end_time) {
		return dao.findPagination(pageNum, pageSize, c_status, c_name, price_from, price_to, start_time, end_time);
	}

	
	
}
