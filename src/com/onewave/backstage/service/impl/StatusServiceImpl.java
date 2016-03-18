package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.StatusDao;
import com.onewave.backstage.model.Status;
import com.onewave.backstage.service.StatusService;


@Service("statusService")
public class StatusServiceImpl implements StatusService {
	
	@Autowired
	@Qualifier("statusDao")
	private  StatusDao statusDao;

	public List<Status> findAll(String table_name, String field_name, String exceptValues) {
		return statusDao.findStatus(table_name, field_name, exceptValues);
	}

}
