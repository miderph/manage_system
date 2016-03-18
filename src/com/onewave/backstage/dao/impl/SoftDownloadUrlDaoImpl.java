package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.SoftDownloadUrlDao;
import com.onewave.backstage.model.SoftDownloadUrl;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("softDownloadUrlDao")
public class SoftDownloadUrlDaoImpl extends BaseDaoImpl<SoftDownloadUrl, String> implements
		SoftDownloadUrlDao {

	@Override
	public List<SoftDownloadUrl> findByCId(String cId, String cType) {
		List<SoftDownloadUrl> list = new ArrayList<SoftDownloadUrl>();
		if(! StringUtils.isBlank(cId) && !StringUtils.isBlank(cType)){
			String sql = "select * from zl_app_download_url t where t.c_type ='"+cType+"' and t.c_id in ("+cId+")";
			list = findAllBySql(sql);
		}
		return list;
	}
}
