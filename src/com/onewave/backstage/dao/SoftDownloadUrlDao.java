package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.SoftDownloadUrl;
import com.onewave.common.dao.IBaseDao;

public interface SoftDownloadUrlDao extends IBaseDao<SoftDownloadUrl, String> {
	public List<SoftDownloadUrl> findByCId(String c_id,String c_type);
}
