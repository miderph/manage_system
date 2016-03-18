package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Operator;

public interface ContProviderService {
	
	public boolean save(ContProvider contProvider, String prefixIds);
   public String saveAndReturnId(ContProvider contProvider);

	public boolean update(ContProvider contProvider, String prefixIds);
	
	public boolean delete(String id);
	
	public ContProvider findById(String id);

	public String findNames(String ids);

	public List<ContProvider> findAll();
	
	public List<ContProvider> findAll(int firstResult, int maxResult);

	public long countAll();

	public ContProvider findContProviderByName(String name);
}
