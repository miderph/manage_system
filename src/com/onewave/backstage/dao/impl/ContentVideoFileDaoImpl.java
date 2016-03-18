
package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ContentVideoFileDao;
import com.onewave.backstage.model.ContentVideoFileBean;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contentVideoFileDao")
public class ContentVideoFileDaoImpl extends BaseDaoImpl<ContentVideoFileBean, String> implements ContentVideoFileDao
{
	@Override
   public boolean deleteAll(String c_id){
      String sql = "delete from  zl_cont_video_file c where c.c_id='" + c_id +"'";
      logger.info("------------sql:" + sql);
      getJdbcTemplate().update(sql);
      return true;
   }
	@Override
	public List<ContentVideoFileBean> findByIds(String ids) {
		List<ContentVideoFileBean> contsalesList = new ArrayList<ContentVideoFileBean>();
		if(ids!=null && ids.trim().length()>0) {
			String sql = "select * from  zl_cont_video_file c where c.ID in (" + ids +")";
			logger.info("------------sql:" + sql);
			contsalesList = findAllBySql(sql);
		}
		return contsalesList;
		
	}

}
