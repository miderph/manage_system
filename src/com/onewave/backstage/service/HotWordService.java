package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.HotWord;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface HotWordService {
	
	public boolean save(HotWord hotWord);
	
   public boolean update(HotWord hotWord);
   public boolean updateAllRela(HotWord hotWord);
   public boolean addRelaHotword(String site_id, String ids);
   public boolean deleteRelaHotword(String ids);
	
	public boolean delete(String id);
	
	public int countAll();
	
	public HotWord findById(String id);
	
	public List<HotWord> findAll();
	
	public List<HotWord> findAll(int firstResult, int maxResults);
	
	public List<HotWord> findAll(String site_id, String hotword, int firstResult, int maxResult);
	
   public int countAll(String site_id, String hotword);
   public int countAllById(String id);
	
	public boolean isExist(HotWord hotWord);
}
