package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ContProviderDao;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Module;
import com.onewave.backstage.model.Operator;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contProviderDao")
public class ContProviderDaoImpl extends BaseDaoImpl<ContProvider, String> implements ContProviderDao {
	
	@Override
	public List<ContProvider> findAll(int firstResult, int maxResult) {
		String sql = "";
		
		String HQL_FIND_ALL = "select cp.*, (select wm_concat(sp.id) from zl_stb_prefixes sp where ','||cp.stb_prefix||',' like '%,'||sp.code||',%') stb_prefix_ids "
				+ ", (select wm_concat(sp.code) from zl_stb_prefixes sp where ','||cp.stb_prefix||',' like '%,'||sp.code||',%') stb_prefix_names "
				+ " from zl_cont_provider cp";
		
		if(firstResult <= InitManager.Defaut_Unselected_ID || maxResult <= InitManager.Defaut_Unselected_ID || maxResult < firstResult) {
			sql = HQL_FIND_ALL + " order by id";
    	} else {
    		sql = "select t.* from (select r.*, rownum linenum from ( " + HQL_FIND_ALL+" order by id) r where rownum <= " + maxResult + ") t WHERE linenum > " + firstResult;
    	}
		
		@SuppressWarnings("unchecked")
		List<ContProvider> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(ContProvider.class));
		
		if(list == null) {
			list = new ArrayList<ContProvider>();
		}
		
		return list;
	}

	@Override
	public ContProvider findByName(String name) {

		ContProvider provider = null;
		List<ContProvider> providers = new ArrayList<ContProvider>();
		if (name != null && name.trim().length() > 0) {
			String sql = "select * from zl_roles t where t.name='" + name + "'";
			logger.info("------------sql:" + sql);
			providers = findAllBySql(sql);
		}

		if (providers != null && providers.size() > 0) {
			provider = providers.get(0);
		}

		return provider;
	}

	@Override
	public ContProvider findContProviderByName(String name) {

		ContProvider provider = null;
		List<ContProvider> providers = new ArrayList<ContProvider>();
		if (name != null && name.trim().length() > 0) {
			String sql = "select * from zl_cont_provider t where t.name='"
					+ name + "'";
			logger.info("------------sql:" + sql);
			providers = findAllBySql(sql);
		}

		if (providers != null && providers.size() > 0) {
			provider = providers.get(0);
		}

		return provider;
	}

	@Override
	public List<ContProvider> findContProviderListByName(String name) {

		List<ContProvider> providers = new ArrayList<ContProvider>();
		if (name != null && name.trim().length() > 0) {
			String sql = "select * from zl_cont_provider t where t.name='"
					+ name + "'";
			logger.info("------------sql:" + sql);
			providers = findAllBySql(sql);
		}

		return providers;
	}

	@Override
	public ContProvider findContProviderByProviderId(String provider_id) {

		ContProvider provider = null;
		List<ContProvider> providers = new ArrayList<ContProvider>();
		if (provider_id != null && provider_id.trim().length() > 0) {
			String sql = "select * from zl_cont_provider t where t.id="
					+ provider_id;
			logger.info("------------sql:" + sql);
			providers = findAllBySql(sql);
		}

		if (providers != null && providers.size() > 0) {
			provider = providers.get(0);
		}
		return provider;
	}

}
