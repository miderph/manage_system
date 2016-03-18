package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.ContentVideoFileBean;
import com.onewave.common.dao.IBaseDao;


/**
 * 
 * @author liuhaidi
 *
 */
public interface ContentVideoFileDao extends IBaseDao<ContentVideoFileBean, String>{
   public boolean deleteAll(String c_id);
	List<ContentVideoFileBean> findByIds(String ids);
}
