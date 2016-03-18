package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.AreaDao;
import com.onewave.backstage.model.Area;
import com.onewave.backstage.service.AreaService;

@Service("areaService")
public class AreaServiceImpl implements AreaService {

	private AreaDao areaDao;

	public AreaDao getAreaDao() {
		return areaDao;
	}

	@Autowired
	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}

	public boolean save(Area area) {
		// area.setCreate_time(new Date());
		// area.setModify_time(new Date());
		return areaDao.save(area);
	}

	public boolean update(Area area) {
		// area.setModify_time(new Date());
		return areaDao.update(area);
	}

	public boolean delete(Area area) {
		return areaDao.delete(area);
	}

	public boolean delete(String id) {
		return areaDao.deleteById(id);
	}

	public int countAll() {

		return areaDao.countAll();
	}

	public Area findById(String id) {
		return areaDao.findById(id);
	}

	public List<Area> findAll() {
		return areaDao.findAll();
	}

	public List<Area> findAll(int firstResult, int maxResults) {
		return areaDao.findAll(firstResult, maxResults);
	}

	public List<Area> findAllProv() {
		return areaDao.findAll("parent_id=0 and id>0", "order by id");
	}

	public List<Area> findAllCity() {
		return areaDao.findAll("area_level=2", "order by parent_id,id");
	}
}
