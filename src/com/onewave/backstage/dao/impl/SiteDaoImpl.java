package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.SiteDao;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Site;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("siteDao")
public class SiteDaoImpl extends BaseDaoImpl<Site, String> implements SiteDao {

	@Override
	public List<Site> findWithAuth(Operator operator) {

		List<Site> siteList = new ArrayList<Site>();

		if (operator == null)
			return siteList;

		if ("admin".equals(operator.getName())) {
			siteList = findAll(null, " order by id asc");
		} else {
			String sql = "select distinct s.* from zl_operators u,zl_roles r,zl_site s where u.id='" + operator.getId()
					+ "' and ','||u.role_ids||',' like '%,'||r.id||',%' and ','||r.site_ids||',' like '%,'||s.id||',%' order by s.id asc";
			siteList = findAllBySql(sql);
		}

		return siteList;
	}

}
