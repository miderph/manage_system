package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Area;


/**
 * The area and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface AreaService {
   public boolean save(Area area);
   
   public boolean update(Area area);
   
   public boolean delete(Area area);
   
   public boolean delete(String id);
   
   public int countAll();
   
   public Area findById(String id);
   
   public List<Area> findAll();
   
   public List<Area> findAll(int firstResult, int maxResults);
   
   public List<Area> findAllCity();
   public List<Area> findAllProv();
}
