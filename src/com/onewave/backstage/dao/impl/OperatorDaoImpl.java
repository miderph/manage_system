package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.OperatorDao;
import com.onewave.backstage.model.Operator;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("operatorDao")
public class OperatorDaoImpl extends BaseDaoImpl<Operator, String> implements OperatorDao {

	public Operator findByName(String name) {
		
		Operator operators = null;
		List<Operator> operatorsList = new ArrayList<Operator>();
		if(name!=null && name.trim().length()>0) {
			String sql = "select * from zl_operators t where t.name='" + name + "'";
			operatorsList = findAllBySql(sql);
		}
		
		if(operatorsList!=null && operatorsList.size()>0) {
			operators = operatorsList.get(0);
		}
		
		return operators;
	}
	
	public List<Operator> findAll(int firstResult, int maxResults) {
		
		String sql = "";
		
		String HQL_FIND_ALL = "select o.*, (select wm_concat(r.name) from zl_roles r where ','||o.role_ids||',' like '%,'||r.id||',%') role_names from zl_operators o";
		
		if(firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
			sql = HQL_FIND_ALL + " order by id desc";
    	} else {
    		sql = "select t.* from (select r.*, rownum linenum from ( " + HQL_FIND_ALL+" order by id desc ) r where rownum <= " + maxResults + ") t WHERE linenum > " + firstResult;
    	}
		
		@SuppressWarnings("unchecked")
		List<Operator> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Operator.class));
		
		if(list == null) {
			list = new ArrayList<Operator>();
		}
		
		return list;
		
	}

}
