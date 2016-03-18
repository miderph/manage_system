package com.onewave.backstage.dao.impl;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.AreaDao;
import com.onewave.backstage.model.Area;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("areaDao")
public class AreaDaoImpl extends BaseDaoImpl<Area, String> implements AreaDao {

}
