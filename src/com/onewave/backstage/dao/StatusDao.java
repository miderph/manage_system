package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Status;
import com.onewave.common.dao.IBaseDao;


/**
 * The site and database interaction of various interfaces
 * @author liuhaidi
 * @category Dao
 */
public interface StatusDao extends IBaseDao<Status, String> {
	public List<Status> findStatus(String table_name, String field_name, String exceptValues);
}
