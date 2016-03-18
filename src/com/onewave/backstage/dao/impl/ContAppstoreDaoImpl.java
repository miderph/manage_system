package com.onewave.backstage.dao.impl;

import java.util.List;

import net.zhilink.tools.InitManager;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ContAppstoreDao;
import com.onewave.backstage.model.ContAppstore;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contAppstoreDao")
public class ContAppstoreDaoImpl extends BaseDaoImpl<ContAppstore, String> implements ContAppstoreDao {

	public int countAll(String type, String status, String name) {
		String where = " r.type=" + type + " ";
		
		if(status!=null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and r.status=" + status + " ";
		}
		
		if(name!=null && !"".equals(name.trim())) {
			where += "and r.name like '%" + name + "%' ";
		}
		
		String sql = "select COUNT(*) from zl_cont r, zl_cont_video cv, zl_cont_appstore ca where " + where + " and r.id=cv.c_id and r.id=ca.c_id";
		
		return countBySql(sql);
	}
	
	public List<ContAppstore> findAll(int firstResult, int maxResults, String type,
			String status, String name) {
		
		String where = " r.type='" + type + "' ";
		
		if(status!=null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and r.status=" + status + " ";
		}
		
		if(name!=null && !"".equals(name.trim())) {
			where += "and r.name like '%" + name + "%' ";
		}
		
		String sql = "";
		
		if(firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
			sql = "select * from zl_cont r, zl_cont_video cv, zl_cont_appstore ca where " + where + " and r.id=cv.c_id and r.id=ca.c_id order by id desc";
    	} else {
    		sql = "select t.* from (select r.*, rownum linenum from zl_cont r, zl_cont_video cv, zl_cont_appstore ca where " + where + " and r.id=cv.c_id and r.id=ca.c_id and rownum <= " + maxResults + ") t WHERE linenum > " + firstResult;
    	}
		
		return findAllBySql(sql);
	}

}
