package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Img;

/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface ImgService {
	
	public boolean save(Img img);
	
	public boolean update(Img img);
	
	public boolean delete(Img img);
	
	public boolean deleteById(String id);
	
	public boolean deleteAll(String targetId,String useType);
	
	public Img findById(String id);
	
	public List<Img>  findByIds(String ids);
	
	public Img findByTargetId(String id);
	
	public int countAll();
	
	public int countAll(String where);
	
	public int countBySql(String sql);
	
	public List<Img> findAll();
	
	public List<Img> findAll(String where);
	
	public List<Img> findAllBySql(String sql);
	
	public List<Img> findAllByPage(int pageNum, int pageSize);
	
	public List<Img> findAllByPage(int pageNum, int pageSize, String where);
	
	public List<Img> findAll(int firstResult, int maxResults);
	
	public List<Img> findAll(int firstResult, int maxResults, String where);
	
	public List<Img> findAll(String targetId, String useType);
	
	public String saveAndReturnId(final Img img);
	
	public boolean updatelocked(String targetId,String useType,String locked);
	
}
