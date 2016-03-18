package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.RecommendSync;


public interface RecommendSyncService {
	   public boolean save(RecommendSync stbModel);
	   
	   public boolean update(RecommendSync stbModel);
	   
	   public boolean delete(RecommendSync stbModel);
	   
	   public boolean delete(String id);
	   
	   public int countAll();
	   
	   public RecommendSync findById(String id);
	   
	   public List<RecommendSync> findAllByNames(String names);
	   public List<RecommendSync> findAllByCids(String ids);
	   
	   public List<RecommendSync> findAll();
	   
	   public List<RecommendSync> findAll(int firstResult, int maxResults);
	   
	   
	   public boolean syncAdd(String ids) throws  Exception;
	   public boolean syncDelete(String ids) throws  Exception;
}
