package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.Img;
import com.onewave.common.dao.IBaseDao;

public interface ImgDao extends IBaseDao<Img, String> {
	
	public String saveAndReturnId(final Img img);
	
	public List<Img> findAll(String targetId, String useType);
	
	public Img findByTargetId(String targetId);
	
	public List<Img> findByIds(String ids);
	
	public boolean deleteAll(String targetId,String useType);
	
	public boolean updatelocked(String targetId,String useType,String locked);
}
