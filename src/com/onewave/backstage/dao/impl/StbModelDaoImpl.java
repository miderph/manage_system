package com.onewave.backstage.dao.impl;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.StbModelDao;
import com.onewave.backstage.model.StbModel;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("stbModelDao")
public class StbModelDaoImpl extends BaseDaoImpl<StbModel, String> implements StbModelDao {

	
}
