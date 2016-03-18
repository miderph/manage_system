package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ColumnDao;
import com.onewave.backstage.model.Column;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("columnDao")
public class ColumnDaoImpl extends BaseDaoImpl<Column, String> implements ColumnDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Column> findAll(long pid, long status, String site_id) {
		
		String where = " site_id='" + site_id + "' ";
		
		if(status != InitManager.Defaut_Unselected_ID) {
			where += " and status='" + status + "' ";
		}
		
		if(pid != -1) {
			where += " and parent_id='" + pid + "' ";
		}
		
		//select t.*,(select count(1) from zl_menu where parent_id=t.id) tcount from zl_menu t where parent_id=0
		String HQL_FIND_ALL = "select mt.*,(select count(1) from zl_menu where parent_id=mt.id) sub_count from zl_menu mt";
		
		String sql = "";
		if("".equals(where)) {
			sql = HQL_FIND_ALL + " order by order_num asc";
		} else {
			sql = HQL_FIND_ALL + " where " + where + " order by order_num asc";
		}
		
		logger.info("find sql: " + sql);
		
		List<Column> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Column.class));
		
		if(list == null) {
			list = new ArrayList<Column>();
		}
		
		return list;
	}

	@Override
	public int countAll(long pid, long status, String site_id) {
		String where = " site_id='" + site_id + "' ";
		
		if(status != InitManager.Defaut_Unselected_ID) {
			where += " and status='" + status + "' ";
		}
		
		if(pid != -1) {
			where += " and parent_id='" + pid + "' ";
		}
		
		return countAll(where);
	}

	@Override
	public List<Column> findAll(long pid, String site_ids, String title,
			String ignore_id, String menuIds) {
		
		String where = "";
		
		if(!"-1".equals(site_ids)) {
			where += " and site_id in (" + site_ids + ") ";
		}
		if(!StringUtils.isBlank(menuIds)) {
		   where += " and id in (" + menuIds + ") ";
		}
		
		if(pid != -1) {
		   where += " and parent_id='" + pid + "' ";
		}
		
		if(title!=null && !"".equals(title)) {
		   where += " and instr(title,'" + title + "')>0 ";
		}
		
		if(!StringUtils.isBlank(ignore_id)){
			String ingnoreSql ="id  not in ( select  m.id  from zl_menu m start with m.id ="+ignore_id+" connect by prior m.id = m.parent_id ) ";
			where += " and " + ingnoreSql;
		}
		
		//select t.*,(select count(1) from zl_menu where parent_id=t.id) tcount from zl_menu t where parent_id=0
		String HQL_FIND_ALL = "select mt.*,(select count(1) from zl_menu where parent_id=mt.id) sub_count from zl_menu mt";
		
		String sql = "";
		if(!"".equals(where)) {
		   where = " where " + where.substring("and ".length());
		}
		
		sql = HQL_FIND_ALL + where + " order by order_num asc";
		
		logger.info("find sql: " + sql);
		
		List<Column> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Column.class));
		
		if(list == null) {
			list = new ArrayList<Column>();
		}
		
		return list;
	}
	
}
