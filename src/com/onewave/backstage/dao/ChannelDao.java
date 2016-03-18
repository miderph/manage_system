package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Channel;
import com.onewave.backstage.model.Operator;
import com.onewave.common.dao.IBaseDao;


/**
 * The site and database interaction of various interfaces
 * @author liuhaidi
 * @category Dao
 */
public interface ChannelDao extends IBaseDao<Channel, String> {
	public List<Channel> findAll(String type);

}
