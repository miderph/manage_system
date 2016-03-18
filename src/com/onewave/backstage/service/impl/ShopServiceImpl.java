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

import com.onewave.backstage.dao.ShopDao;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Shop;
import com.onewave.backstage.service.ShopService;

@Service("shopService")
public class ShopServiceImpl implements ShopService {
	
	@Autowired
	@Qualifier("shopDao")
	private ShopDao shopDao;
	
	@Override
	public boolean save(Shop shop) {
		shop.setCreate_time(new Date());
		shop.setModify_time(new Date());
		return shopDao.save(shop);
	}

   public String saveAndReturnId(Shop shop) {
      shop.setCreate_time(new Date());
      shop.setModify_time(new Date());
      return shopDao.saveAndReturnId(shop);
   }

	@Override
	public boolean update(Shop shop) {
		shop.setModify_time(new Date());
		return shopDao.update(shop);
	}
	
	@Override
	public boolean delete(String id) {
		return shopDao.deleteById(id);
	}
	
	@Override
	public int countAll() {
		
		return shopDao.countAll();
	}
	
	public Shop findById(String id) {
		return shopDao.findById(id);
	}
	
	public List<Shop> findAll() {
		return shopDao.findAll();
	}
	
	@Override
	public List<Shop> findAll(int firstResult, int maxResults) {
		return shopDao.findAll(firstResult, maxResults);
	}

	public String findNames(String ids) {
		String names ="";
		if(!StringUtils.isEmpty(ids)){
			List<Shop> shops = shopDao.findAll(" id in ("+ids+")", " order by name asc");
			if(shops!=null && shops.size() > 0){
				for(Shop shop : shops){
					names += shop.getName()+",";
				}
				names = names.substring(0,names.length()-1);
			}
		}
		return names;
	}

}
