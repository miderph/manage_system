package com.onewave.backstage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zhilink.tools.InitManager;
import net.zhilink.tools.XMLSender;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ChannelDao;
import com.onewave.backstage.model.Channel;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.service.ChannelService;

@Service("channelService")
public class ChannelServiceImpl implements ChannelService {
	
	@Autowired
	@Qualifier("channelDao")
	private ChannelDao channelDao;
	
	@Override
	public boolean save(Channel channel) {
		channel.setCreate_time(new Date());
		channel.setModify_time(new Date());
		return channelDao.save(channel);
	}

   public String saveAndReturnId(Channel channel) {
      channel.setCreate_time(new Date());
      channel.setModify_time(new Date());
      return channelDao.saveAndReturnId(channel);
   }

	@Override
	public boolean update(Channel channel) {
		channel.setModify_time(new Date());
		return channelDao.update(channel);
	}
	
	@Override
	public boolean delete(String id) {
		return channelDao.deleteById(id);
	}
	
	@Override
	public int countAll() {
		
		return channelDao.countAll();
	}
	
	public Channel findById(String id) {
		return channelDao.findById(id);
	}
	
	@Override
	public List<Channel> findAll() {
		return channelDao.findAll();
	}
	@Override
	public List<Channel> findAll(String type) {
		return channelDao.findAll(type);
	}
	@Override
	public List<Channel> findAll(int firstResult, int maxResults) {
		return channelDao.findAll(firstResult, maxResults);
	}

	public String findNames(String ids) {
		String names ="";
		if(!StringUtils.isEmpty(ids)){
			List<Channel> channels = channelDao.findAll(" id in ("+ids+")", " order by name asc");
			if(channels!=null && channels.size() > 0){
				for(Channel channel : channels){
					names += channel.getName()+",";
				}
				names = names.substring(0,names.length()-1);
			}
		}
		return names;
	}

}
