package com.onewave.backstage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.SoftwareDao;
import com.onewave.backstage.model.Result;
import com.onewave.backstage.model.SoftwareVersion;
import com.onewave.backstage.service.SoftwareService;

@Service("softwareService")
public class SoftwareServiceImpl implements SoftwareService {

	@Autowired
	@Qualifier("softwareDao")
	private SoftwareDao softwareDao;

	public SoftwareDao getSoftwareDao() {
		return softwareDao;
	}

	public void setSoftwareDao(SoftwareDao softwareDao) {
		this.softwareDao = softwareDao;
	}

	public List<SoftwareVersion> getAllData(int firstResult, int maxResults, String version_number,
			String software_info, String plat, String enforce_flag,
			String usergroup_id, String file_type, String update_url,
			String description, String url_type, String status) {
		return this.getSoftwareDao().query(firstResult, maxResults, version_number,
				software_info, plat, enforce_flag, usergroup_id, file_type,
				update_url, description, url_type, status);
	}

	public long getLength(String version_number, String software_info,
			String plat, String enforce_flag, String usergroup_id,
			String file_type, String update_url, String description,
			String url_type, String status) {
		return this.getSoftwareDao().getLength(version_number, software_info,
				plat, enforce_flag, usergroup_id, file_type, update_url,
				description, url_type, status);
	}

	public void update(SoftwareVersion soft) {

		this.getSoftwareDao().update(soft);

	}

	public void deleteData(SoftwareVersion test) {
		this.getSoftwareDao().delete(test);

	}

	@Override
	public String saveAndReturnId(SoftwareVersion soft) {
		return this.getSoftwareDao().saveAndReturnId(soft);
	}

	@Override
	public boolean isExistRecord(SoftwareVersion soft) {
		return this.softwareDao.isExistRecord(soft);
	}

	@Override
	public long countByVersionNum(String version_num, String plat) {
		return softwareDao.countByVersionNum(version_num, plat);
	}

}
