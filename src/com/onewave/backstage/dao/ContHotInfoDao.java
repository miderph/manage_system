package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContHotInfo;
import com.onewave.common.dao.IBaseDao;

/**
 * The area and database interaction of various interfaces
 * @author liuhaidi
 * @category Dao
 */
public interface ContHotInfoDao extends IBaseDao<ContHotInfo, String> {
	public int countHotInfos(String c_id, String channel);
	public List<ContHotInfo> findHotInfos(String c_id, String channel);
}
