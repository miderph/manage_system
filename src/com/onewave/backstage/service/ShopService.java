package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Shop;

/**
 * The shop and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author zyf
 * @category Service
 * 
 */
public interface ShopService {
	
	public boolean save(Shop shop);
   public String saveAndReturnId(Shop shop);

	public boolean update(Shop shop);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public Shop findById(String id);
	
	public List<Shop> findAll();
	
	public List<Shop> findAll(int firstResult, int maxResults);

	public String findNames(String ids);
}
