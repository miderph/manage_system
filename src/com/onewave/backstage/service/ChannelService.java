package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Channel;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Operator;

/**
 * The channel and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author zyf
 * @category Service
 * 
 */
public interface ChannelService {
	
	public boolean save(Channel channel);
   public String saveAndReturnId(Channel channel);

	public boolean update(Channel channel);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public Channel findById(String id);
	
	public List<Channel> findAll();
	public List<Channel> findAll(String type);
	
	public List<Channel> findAll(int firstResult, int maxResults);

	public String findNames(String ids);
}
