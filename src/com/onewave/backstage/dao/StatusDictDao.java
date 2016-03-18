package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.StatusDict;
import com.onewave.common.dao.IBaseDao;


/**
 * The site and database interaction of various interfaces
 * @author liuhaidi
 * @category Dao
 */
public interface StatusDictDao extends IBaseDao<StatusDict, String> {
	public List<StatusDict> queryStatusDict(String table_name, String field_name);
}
