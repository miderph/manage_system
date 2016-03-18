package com.onewave.backstage.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ContHotInfoDao;
import com.onewave.backstage.model.ContHotInfo;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contHotInfoDao")
public class ContHotInfoDaoImpl extends BaseDaoImpl<ContHotInfo, String> implements ContHotInfoDao {
	@Override
	public int countHotInfos(String c_id, String channel) {
		String where = "c_id="+c_id;
		if (StringUtils.isNotBlank(channel)){
			where += " and (id like '%"+channel+"%' or channel like '%"+channel+"%' or hot_info like '%"+channel+"%' or description like '%"+channel+"%')";
		}
		return countAll(where);
	}
	@Override
	public List<ContHotInfo> findHotInfos(String c_id, String channel) {
		String sql = "select * from zl_cont_sales_hotinfo where c_id="+c_id;
		if (StringUtils.isNotBlank(channel)){
			sql += " and (id like '%"+channel+"%' or channel like '%"+channel+"%' or hot_info like '%"+channel+"%' or description like '%"+channel+"%')";
		}
		List<ContHotInfo> list = findAllBySql(sql);

		return list;
	}
}
