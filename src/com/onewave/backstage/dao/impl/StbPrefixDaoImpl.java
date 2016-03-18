package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.StbPrefixDao;
import com.onewave.backstage.model.StbPrefix;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("stbPrefixDao")
public class StbPrefixDaoImpl extends BaseDaoImpl<StbPrefix, String> implements
		StbPrefixDao {

	@Override
	public boolean isExistPrefix(StbPrefix stbPrefix) {
		/*
		 * select count(*) from zl_stb_prefixes where code='1' and site_id='31'
		 * and provider_id='28'
		 */
		String sql = "select count(*) from zl_stb_prefixes where code='" + stbPrefix.getCode() + "' ";
		
		if(StringUtils.isNotBlank(stbPrefix.getId())) {
			sql += " and id<>'" + stbPrefix.getId() + "'";
		}
		
		int i = this.getJdbcTemplate().queryForInt(sql);
		if (i >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<StbPrefix> findAll(int firstResult, int maxResults) {

		String sql = "";

		String HQL_FIND_ALL = "select sp.*, cp.name provider_name, cp.site_id, s.name site_name from zl_stb_prefixes sp left join zl_cont_provider cp on sp.provider_id=cp.id left join zl_site s on cp.site_id=s.id";

		if (firstResult <= InitManager.Defaut_Unselected_ID
				|| maxResults <= InitManager.Defaut_Unselected_ID
				|| maxResults < firstResult) {
			sql = HQL_FIND_ALL + " order by sp.code desc";
		} else {
			sql = "select t.* from (select r.*, rownum linenum from ( "
					+ HQL_FIND_ALL
					+ " order by sp.code desc ) r where rownum <= "
					+ maxResults + ") t WHERE linenum > " + firstResult;
		}

		@SuppressWarnings("unchecked")
		List<StbPrefix> list = getJdbcTemplate().query(sql,
				new BeanPropertyRowMapper(StbPrefix.class));

		if (list == null) {
			list = new ArrayList<StbPrefix>();
		}

		return list;

	}

	@Override
	public void updateContProviderField(String spIds, String provider_id) {
		if(StringUtils.isBlank(spIds)) {
			spIds = "-10";
		}
		System.out.println("------spIds: " + spIds);
		System.out.println("------provider_id: " + provider_id);
		if(provider_id != null && spIds != null && StringUtils.isNotEmpty(spIds)) {
			String sql = "update zl_stb_prefixes set provider_id='"+provider_id+"' where id in("+spIds+") and (provider_id is null or provider_id <> '"+provider_id+"')";
			System.out.println("------sql: " + sql);
			getJdbcTemplate().update(sql);
		}
		
	}

	@Override
	public void cleanContProviderField(String spIds, String provider_id) {
		if(StringUtils.isBlank(spIds)) {
			spIds = "-10";
		}
		if(provider_id != null) {
			String sql = "update zl_stb_prefixes set provider_id='' where provider_id='"+provider_id+"' and id not in("+spIds+")";
			System.out.println("------sql: " + sql);
			getJdbcTemplate().update(sql);
		}
	}
}
