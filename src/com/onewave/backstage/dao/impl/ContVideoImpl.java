package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ContVideoDao;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContVideo;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contVideoDao")
public class ContVideoImpl extends BaseDaoImpl<ContVideo, String> implements ContVideoDao {

	@Override
	public boolean saveSuperscript(String ids, String contId) {
		String sql= "";
		if("".equals(contId)){
			contId=null;
		}
		sql+= "update    ZL_CONT_VIDEO  set  SUPERSCRIPT_ID  = "+contId+"  where c_id in("+ids+")";
		logger.info("sql:"+sql);
		return updateBySql(sql);
	}

	@Override
	public List<ContVideo> findByIds(String ids) {
		List<ContVideo> contVideoList = new ArrayList<ContVideo>();
		if(ids!=null && ids.trim().length()>0) {
			String sql = "select * from  ZL_CONT_VIDEO c where c.C_ID in (" + ids +")";
			logger.info("------------sql:" + sql);
			contVideoList = findAllBySql(sql);
		}
		return contVideoList;
		
	}

	@Override
	public List<ContVideo> findbyid(String id) {
		List<ContVideo> contVideoList = new ArrayList<ContVideo>();
		if(id!=null && id.trim().length()>0) {
			String sql = "select * from  ZL_CONT_VIDEO c where c.superscript_id =(" + id +")";
			logger.info("------------sql:" + sql);
			contVideoList = findAllBySql(sql);
		}
		return contVideoList;
	}

}
