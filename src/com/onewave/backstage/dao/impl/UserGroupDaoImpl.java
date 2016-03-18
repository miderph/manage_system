package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.UserGroupDao;
import com.onewave.backstage.model.UserGroup;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("userGroupDao")
public class UserGroupDaoImpl extends BaseDaoImpl<UserGroup, String> implements UserGroupDao {

   public List<UserGroup> findAll(String ug_type){
      if(StringUtils.isBlank(ug_type) || (""+InitManager.Defaut_Unselected_ID).equals(ug_type)) {
         return findAll();
      } else {
         return findAll(" type='"+ug_type+"' ", null);
      }
   }
	public List<UserGroup> findAll(int start, int end, String name, String type, String value){
		
		String where = "";
		if (StringUtils.isNotBlank(name) )
			where += "and name like '%"+name+"%'";
		if (StringUtils.isNotBlank(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim()))
			where += "and type ='"+type+"'";
		if (StringUtils.isNotBlank(value))
			where += "and raw_value like '%"+value+"%'";
		String order = null;
		if (StringUtils.isNotBlank(where))
			where = where.substring("and ".length());
		
		return findAll(where, order);
	}

	@Override
	public List<UserGroup> findAll(String ug_type, String name,
			int firstResult, int maxResult) {

		String where = "";
		if (StringUtils.isNotBlank(ug_type) && !(""+InitManager.Defaut_Unselected_ID).equals(ug_type.trim()))
			where += "and type ='"+ug_type+"'";

		if(StringUtils.isNotBlank(name)) {
			where += " and ug.name like '%"+name+"%' ";
		}
		if (StringUtils.isNotBlank(where))
			where = " where " + where.substring("and ".length());

		String sql = "select * from zl_user_group ug "+ where;
		List<UserGroup> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(UserGroup.class));

		if(list == null) {
			list = new ArrayList<UserGroup>();
		}

		return list;
	}

	@Override
	public int countAll(String ug_type, String name) {
		String where = "";
		if (StringUtils.isNotBlank(ug_type) && !(""+InitManager.Defaut_Unselected_ID).equals(ug_type.trim()))
			where += "and type ='"+ug_type+"'";

		if(StringUtils.isNotBlank(name)) {
			where += " and ug.name like '%"+name+"%' ";
		}
		if (StringUtils.isNotBlank(where))
			where = " where " + where.substring("and ".length());
		
		String sql = "select COUNT(*) from zl_user_group ug "+ where;
		
		return countBySql(sql);
	}

}
