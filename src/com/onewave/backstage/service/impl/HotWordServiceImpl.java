package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.HotWordDao;
import com.onewave.backstage.model.HotWord;
import com.onewave.backstage.service.HotWordService;

/**
 * 热词管理
 * 
 * @author liuhaidi
 * 
 */

@Service("hotWordService")
public class HotWordServiceImpl implements HotWordService {

	@Autowired
	@Qualifier("hotWordDao")
	private HotWordDao hotWordDao;

	@Override
	public boolean delete(String id) {
		return hotWordDao.deleteById(id);
	}

	@Override
	public boolean isExist(HotWord hotWord) {
		String where = " hotword ='" + hotWord.getHotword()
				+ "'and site_id = ' " + hotWord.getSite_id() + "'";
		if (!StringUtils.isEmpty(hotWord.getId())) {
			where += " and id <> '" + hotWord.getId() + "'";
		}
		int i = hotWordDao.countAll(where);
		return i >= 1 ? true : false;
	}

	@Override
	public boolean save(HotWord hotWord) {
		hotWord.setCreate_time(new Date());
		hotWord.setModify_time(new Date());
		return hotWordDao.save(hotWord);
	}

	@Override
	public boolean update(HotWord hotWord) {
		hotWord.setModify_time(new Date());
		return hotWordDao.update(hotWord);
	}
	@Override
   public boolean updateAllRela(HotWord hotWord) {
		return hotWordDao.updateAllRela(hotWord);
	}
	@Override
   public boolean addRelaHotword(String site_id, String ids) {
		return hotWordDao.addRelaHotword(site_id, ids);
	}
	@Override
   public boolean deleteRelaHotword(String ids) {
		return hotWordDao.deleteRelaHotword(ids);
	}
	@Override
	public int countAll() {
		return hotWordDao.countAll();
	}

	@Override
	public HotWord findById(String id) {
		return hotWordDao.findById(id);
	}

	@Override
	public List<HotWord> findAll() {
		return hotWordDao.findAll();
	}

	@Override
	public List<HotWord> findAll(int firstResult, int maxResults) {
		return hotWordDao.findAll(firstResult, maxResults);
	}

	@Override
	public List<HotWord> findAll(String site_id, String hotword,
			int firstResult, int maxResult) {
		return hotWordDao.findAll(site_id, hotword, firstResult, maxResult);
	}

	@Override
	public int countAll(String site_id, String hotword) {
		return hotWordDao.countAll(site_id, hotword);
	}
	@Override
   public int countAllById(String id) {
		return hotWordDao.countAllById(id);
	}
}
