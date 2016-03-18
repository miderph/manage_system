package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.HotWord;
import com.onewave.common.dao.IBaseDao;

/**
 * 热词管理
 * 
 * @author liuhaidi
 * 
 */
public interface HotWordDao extends IBaseDao<HotWord, String> {

	public List<HotWord> findAll(String site_id, String hotword, int firstResult, int maxResult);
	
	public int countAll(String site_id, String hotword);
   public int countAllById(String id);

   public boolean updateAllRela(HotWord hotWord);
   public boolean addRelaHotword(String site_id, String ids);
   public boolean deleteRelaHotword(String ids);

}
