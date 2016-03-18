
package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Result;
import com.onewave.backstage.model.SoftwareVersion;

public interface SoftwareService
{

    public List<SoftwareVersion> getAllData(int firstResult, int maxResults, String version_number, String software_info, String plat, String enforce_flag, String usergroup_id, String file_type, String update_url, String description, String url_type, String status);

    public long getLength(String version_number, String software_info, String plat, String enforce_flag, String usergroup_id, String file_type, String update_url, String description, String url_type, String status);

    public void update(SoftwareVersion test);

    public void deleteData(SoftwareVersion test);
    
    public String saveAndReturnId(SoftwareVersion soft);
    
    public boolean isExistRecord(SoftwareVersion soft);
    
    public long countByVersionNum(String version_num, String plat);
}
