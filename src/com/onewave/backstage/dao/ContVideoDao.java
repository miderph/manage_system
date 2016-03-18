package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContVideo;
import com.onewave.common.dao.IBaseDao;

public interface ContVideoDao extends IBaseDao<ContVideo, String> {

	boolean saveSuperscript(String ids, String contId);

	List<ContVideo> findByIds(String ids);
	
	List<ContVideo> findbyid(String id);

}
