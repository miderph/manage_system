package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ColumnDao;
import com.onewave.backstage.dao.ContRuleDao;
import com.onewave.backstage.model.Column;
import com.onewave.backstage.model.ContRule;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contRuleDao")
public class ContRuleDaoImpl extends BaseDaoImpl<ContRule, String> implements ContRuleDao {
 
	@Autowired
	@Qualifier("columnDao")
	private ColumnDao columnDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContRule> findAllByMenuId(String menuId) {
			
		if(StringUtils.isEmpty(menuId)) return new ArrayList<ContRule>();
		
		Column column = columnDao.findById(menuId);
		String sql = "select cr.*, ( select wm_concat(p.name) from zl_cont_provider p where ','|| cr.provider_ids||',' like '%,'||p.id||',%') provider_names, " +
				"(select wm_concat(s.name ) from zl_cont_shop s where ','||cr.shop_ids||',' like '%,'||s.id||',%') shop_names from zl_cont_rule cr where cr.id in ("+column.getImport_rule_ids()+")";
		logger.info("find sql: " + sql);
		List<ContRule> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(ContRule.class));
		if(list == null) {
			list = new ArrayList<ContRule>();
		}
		return list;
	}
	
}
