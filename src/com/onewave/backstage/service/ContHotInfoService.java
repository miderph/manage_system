package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.ContHotInfo;


/**
 * The area and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface ContHotInfoService {
   public boolean save(ContHotInfo area);
   
   public boolean update(ContHotInfo area);
   
   public boolean delete(ContHotInfo area);
   
   public boolean delete(String id);
   
   public int countAll();
   
   public ContHotInfo findById(String id);
   
   public List<ContHotInfo> findAll();
   
   public List<ContHotInfo> findAll(int firstResult, int maxResults);

   public int countHotInfos(String c_id, String channel);
   public List<ContHotInfo> findHotInfos(String c_id, String channel);
}
