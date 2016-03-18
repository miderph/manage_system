package com.onewave.backstage.service;

import java.util.List;
import java.util.Map;

import com.onewave.backstage.model.SoftDownloadUrl;

public interface SoftDownlaodUrlService {
	public boolean save(SoftDownloadUrl downloadUrl);
	public boolean update(SoftDownloadUrl downloadUrl);
	public boolean delete(SoftDownloadUrl downloadUrl);
	public boolean delete(String id);
	public SoftDownloadUrl findById(String id);
	public List<SoftDownloadUrl>  findByCId(String c_ids,String c_type);
	public Map<String,SoftDownloadUrl> findMapByCId(String c_ids,String c_type);
}
