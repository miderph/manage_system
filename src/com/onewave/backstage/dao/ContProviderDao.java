package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Operator;
import com.onewave.common.dao.IBaseDao;

public interface ContProviderDao extends IBaseDao<ContProvider, String> {
	
	public List<ContProvider> findAll(int firstResult, int maxResult);
	
	public ContProvider findByName(String name);

	public ContProvider findContProviderByName(String name);

	public List<ContProvider> findContProviderListByName(String name);

	public ContProvider findContProviderByProviderId(String provider_id);

}
