package com.onewave.backstage.dao.impl;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.PlatGroupDao;
import com.onewave.backstage.model.PlatGroup;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("platGroupDao")
public class PlatGroupDaoImpl extends BaseDaoImpl<PlatGroup, String> implements PlatGroupDao {
	
}
