package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ChannelDao;
import com.onewave.backstage.model.Channel;
import com.onewave.backstage.model.Operator;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("channelDao")
public class ChannelDaoImpl extends BaseDaoImpl<Channel, String> implements ChannelDao {

	public List<Channel> findAll(String type){
		String where = "";
		if (StringUtils.isNotBlank(type)){
			where = " type='"+type+"'";
		}
		return this.findAll(where, null);
	}

}
