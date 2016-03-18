
package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.ContentVideoFileBean;

public interface ContVideoFileService
{

	public boolean save(ContentVideoFileBean contVideo);
	
	public boolean update(ContentVideoFileBean contVideo);
	
   public boolean delete(String id);
   public boolean deleteAll(String c_id);

   public int countAll(String cid);
   public List<ContentVideoFileBean> findAll(int firstResult, int maxResults, String cid);
	public List<ContentVideoFileBean> findByIds(String ids);
	
	public ContentVideoFileBean findById(String id);

}
