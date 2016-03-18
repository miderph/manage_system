package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContProviderDao;
import com.onewave.backstage.dao.RoleDao;
import com.onewave.backstage.dao.StbPrefixDao;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Role;
import com.onewave.backstage.service.ContProviderService;

@Service("contProviderService")
public class ContProviderServiceImpl implements ContProviderService {
	
	@Autowired
	@Qualifier("roleDao")
	private RoleDao roleDao;
	
	@Autowired
	@Qualifier("stbPrefixDao")
	private StbPrefixDao stbPrefixDao;
	
	@Autowired
	@Qualifier("contProviderDao")
	private ContProviderDao contProviderDao;
	
	@Override
	public boolean save(ContProvider contProvider, String prefixIds) {
		contProvider.setCreate_time(new Date());
		contProvider.setModify_time(new Date());
		
		boolean result = contProviderDao.save(contProvider);
		
		if(result) {
			stbPrefixDao.updateContProviderField(prefixIds, contProvider.getId());
		}
		
		return result;
	}

   public String saveAndReturnId(ContProvider contProvider) {
      contProvider.setCreate_time(new Date());
      contProvider.setModify_time(new Date());
      return contProviderDao.saveAndReturnId(contProvider);
   }

	@Override
	public boolean update(ContProvider contProvider, String prefixIds) {
		contProvider.setModify_time(new Date());
		
		boolean result = contProviderDao.update(contProvider);
		
		if(result) {
			stbPrefixDao.cleanContProviderField(prefixIds, contProvider.getId());
			stbPrefixDao.updateContProviderField(prefixIds, contProvider.getId());
		}
		
		return result;
	}
	
	@Override
	public boolean delete(String id) {
		boolean result = contProviderDao.deleteById(id);
		
		if(result) {
			stbPrefixDao.cleanContProviderField(null, id);
		}
		
		return result;
	}
	
	@Override
	public ContProvider findById(String id) {
		return contProviderDao.findById(id);
	}

	@Override
	public String findNames(String ids) {
		String names = "";
		if (!StringUtils.isEmpty(ids)) {
			List<ContProvider> contProviders = contProviderDao.findAll(
					" id in (" + ids + ")", " order by name asc");
			if (contProviders != null && contProviders.size() > 0) {
				for (ContProvider provider : contProviders) {
					names += provider.getName() + ",";
				}
				names = names.substring(0, names.length() - 1);
			}
		}
		return names;
	}
	
	@Override
	public List<ContProvider> findAll() {
		return contProviderDao.findAll();
	}

	@Override
	public List<ContProvider> findAll(int firstResult, int maxResult) {
		return contProviderDao.findAll(firstResult, maxResult);
	}

	@Override
	public long countAll() {
		return contProviderDao.countAll();
	}

	@Override
	public ContProvider findContProviderByName(String name) {
		return contProviderDao.findContProviderByName(name);
	}
}
