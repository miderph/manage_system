package com.onewave.backstage.dao;

import com.onewave.backstage.model.StbPrefix;
import com.onewave.common.dao.IBaseDao;

public interface StbPrefixDao extends IBaseDao<StbPrefix, String> {
	public boolean isExistPrefix(StbPrefix stbPrefix);
	
	public void updateContProviderField(String spIds, String provider_id);
	
	public void cleanContProviderField(String spIds, String provider_id);
}
