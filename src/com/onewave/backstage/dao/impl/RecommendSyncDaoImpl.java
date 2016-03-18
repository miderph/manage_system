package com.onewave.backstage.dao.impl;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.RecommendSyncDao;
import com.onewave.backstage.model.RecommendSync;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("recommendSyncDao")
public class RecommendSyncDaoImpl extends BaseDaoImpl<RecommendSync, String> implements
		RecommendSyncDao {
}
