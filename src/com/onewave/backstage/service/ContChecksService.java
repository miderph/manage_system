package com.onewave.backstage.service;

import com.onewave.backstage.model.ContCheck;
import com.onewave.common.dao.Pagination;
import com.onewave.common.service.IBaseService;

public interface ContChecksService extends IBaseService<ContCheck, String> {
	public ContCheck findByItemUrl(String itemUrl);

	public boolean underShelf(String saleNo);

	public Pagination<ContCheck> findPagination(int pageNum, int pageSize,
			String c_status, String c_name, String price_from, String price_to,
			String start_time, String end_time);

}
