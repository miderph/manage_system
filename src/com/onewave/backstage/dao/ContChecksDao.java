package com.onewave.backstage.dao;

import com.onewave.backstage.model.ContCheck;
import com.onewave.common.dao.IBaseDao;
import com.onewave.common.dao.Pagination;

public interface ContChecksDao extends IBaseDao<ContCheck, String> {
	public ContCheck findByItemUrl(String linkUrl);
	public Pagination<ContCheck> findPagination(int pageNum, int pageSize,String c_status,
			String c_name, String price_from, String price_to,
			String start_time, String end_time);
}
