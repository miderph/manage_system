package com.onewave.backstage.dao;

import java.util.List;

import com.onewave.backstage.model.SoftwareVersion;
import com.onewave.common.dao.IBaseDao;

/**
 * 
 * @author liuhaidi
 * 
 */
public interface SoftwareDao extends IBaseDao<SoftwareVersion, String> {

	public List<SoftwareVersion> query(int firstResult, int maxResults, String version_number,
			String software_info, String plat, String enforce_flag,
			String usergroup_id, String file_type, String update_url,
			String description, String url_type, String status);

	public long getLength(String version_number, String software_info,
			String plat, String enforce_flag, String usergroup_id,
			String file_type, String update_url, String description,
			String url_type, String status);

	public boolean isExistRecord(SoftwareVersion test);

	public long countByVersionNum(String version_num, String plat);
}
