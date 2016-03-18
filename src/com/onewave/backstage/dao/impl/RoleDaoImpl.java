package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.RoleDao;
import com.onewave.backstage.model.Role;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role, String> implements RoleDao {
	
	public List<Role> findAll(int firstResult, int maxResults) {
		String sql = "";
		
		String HQL_FIND_ALL = "select r.*, (select wm_concat(s.name) from zl_site s where ','||r.site_ids||',' like '%,'||s.id||',%') site_names "
				+ ", (select wm_concat(cp.name) from zl_cont_provider cp where ','||r.provider_ids||',' like '%,'||cp.id||',%') provider_names "
            + ", (select wm_concat(m.name) from zl_modules m where ','||r.module_ids||',' like '%,'||m.id||',%') module_names "
            + ", (select wm_concat(m.title) from zl_menu m where site_id=0 and parent_id=0 and ','||r.menu_ids||',' like '%,'||m.id||',%') menu_names "
            + ", (select wm_concat(m.name) from zl_user_group m where ','||r.group_ids||',' like '%,'||m.id||',%') group_names "
				+ " from zl_roles r";
		
		if(firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
			sql = HQL_FIND_ALL + " order by id desc";
    	} else {
    		sql = "select t.* from (select r.*, rownum linenum from ( " + HQL_FIND_ALL+" order by id desc ) r where rownum <= " + maxResults + ") t WHERE linenum > " + firstResult;
    	}
		
		@SuppressWarnings("unchecked")
		List<Role> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Role.class));
		
		if(list == null) {
			list = new ArrayList<Role>();
		}
		
		return list;
	}

	@Override
	public Role findByName(String name) {
		Role role = null;
		List<Role> roleList = new ArrayList<Role>();
		if(name!=null && name.trim().length()>0) {
			String sql = "select * from zl_roles t where t.name='" + name + "'";
			logger.info("------------sql:" + sql);
			roleList = findAllBySql(sql);
		}
		
		if(roleList!=null && roleList.size()>0) {
			role = roleList.get(0);
		}
		
		return role;
	}
	
}
