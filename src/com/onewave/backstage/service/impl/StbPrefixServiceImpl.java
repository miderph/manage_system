package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContProviderDao;
import com.onewave.backstage.dao.StbPrefixDao;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.StbPrefix;
import com.onewave.backstage.service.StbPrefixService;

@Service("stbPrefixService")
public class StbPrefixServiceImpl implements StbPrefixService {

	@Autowired
	@Qualifier("stbPrefixDao")
	private StbPrefixDao stbPrefixDao;
	@Autowired
	@Qualifier("contProviderDao")
	private ContProviderDao contProviderDao;

	@Override
	public boolean save(StbPrefix stbPrefix) {
		stbPrefix.setCreate_time(new Date());
		stbPrefix.setUpdate_time(new Date());

		return stbPrefixDao.save(stbPrefix);
	}

	@Override
	public boolean update(StbPrefix stbPrefix) {
		stbPrefix.setUpdate_time(new Date());
		
		return stbPrefixDao.update(stbPrefix);
	}

	/*@Override
	public boolean update(StbPrefix stbPrefix, String old_stbPrefix,
			String old_provider_id) {
		boolean flag = stbPrefixDao.update(stbPrefix);
		return flag;
	}*/

	public boolean deleteAndRemove(StbPrefix stbPrefix) {
		boolean flag = removeProviderStbPrefix(stbPrefix);
		flag = stbPrefixDao.delete(stbPrefix);
		return flag;
	}

	public boolean delete(String id) {
		StbPrefix stbPrefix = findById(id);
		return deleteAndRemove(stbPrefix);
	}

	@Override
	public int countAll() {
		return stbPrefixDao.countAll();
	}

	@Override
	public List<StbPrefix> findAll() {
		return stbPrefixDao.findAll();
	}

	@Override
	public List<StbPrefix> findAll(int firstResult, int maxResults) {
		return stbPrefixDao.findAll(firstResult, maxResults);
	}
	
	@Override
	// 获取改内容提供商已绑定的，或者没有被使用过的前缀数量
	public int countAllUnbound(String providerId) {
		String where = " provider_id is null ";
		if(StringUtils.isEmpty(providerId)) {
			where += " or  provider_id= " + providerId + " ";
		}
		
		return stbPrefixDao.countAll(where);
	}

	@Override
	// 获取改内容提供商已绑定的，或者没有被使用过的前缀
	public List<StbPrefix> findAllUnbound(String providerId) {
		String where = " provider_id is null ";
		if(StringUtils.isNotBlank(providerId)) {
			where += " or  provider_id= " + providerId + " ";
		}
		
		return stbPrefixDao.findAll(where, null);
	}

	@Override
	public StbPrefix findById(String id) {
		return stbPrefixDao.findById(id);
	}

	@Override
	public boolean isExistPrefix(StbPrefix stbPrefix) {
		return stbPrefixDao.isExistPrefix(stbPrefix);
	}

	private boolean removeProviderStbPrefix(StbPrefix stbPrefix) {
		boolean flag = false;
		ContProvider pv = contProviderDao.findById(stbPrefix.getProvider_id());
		if (pv != null) {
			pv.removeStbPrefix(stbPrefix.getCode());
			flag = contProviderDao.update(pv);
		}
		return flag;
	}
}
