
package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContentVideoFileDao;
import com.onewave.backstage.model.ContentVideoFileBean;
import com.onewave.backstage.service.ContVideoFileService;

@Service("contVideoFileService")
public class ContVideoFileServiceImpl implements ContVideoFileService
{
	@Autowired
	@Qualifier("contentVideoFileDao")
   private ContentVideoFileDao ContentVideoFileDao;


	public boolean save(ContentVideoFileBean contVideo) {
		return ContentVideoFileDao.save(contVideo);
	}
	
	public boolean update(ContentVideoFileBean contVideo) {
		return ContentVideoFileDao.update(contVideo);
	}
	public boolean delete(String id) {
		return ContentVideoFileDao.deleteById(id);
	}
   public boolean deleteAll(String c_id) {
		return ContentVideoFileDao.deleteAll(c_id);
	}
	@Override
   public int countAll(String cid){
	   return ContentVideoFileDao.countAll("c_id="+cid);
	}
	@Override
	public List<ContentVideoFileBean> findAll(int firstResult, int maxResults, String cid) {
		return ContentVideoFileDao.findAll(firstResult, maxResults, "c_id="+cid);
	}
	@Override
	public List<ContentVideoFileBean> findByIds(String ids) {
		return ContentVideoFileDao.findByIds(ids);
	}

	@Override
	public ContentVideoFileBean findById(String id) {
		return ContentVideoFileDao.findById(id);
	}

}
