package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Site;
import com.onewave.common.dao.IBaseDao;


/**
 * The site and database interaction of various interfaces
 * @author liuhaidi
 * @category Dao
 */
public interface SiteDao extends IBaseDao<Site, String> {
	public List<Site> findWithAuth(Operator operator);
}
