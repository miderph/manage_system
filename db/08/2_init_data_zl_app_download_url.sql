insert  into zl_app_download_url
(c_id,c_type,version,add_time,create_time,modify_time,download_url,url_type,upgrade_temp_url,temp_url_expire_time,share_password)
select v.id,2,v.version_num,v.publish_time,v.publish_time,v.publish_time,v.update_url ,nvl(v.url_type,1),v.upgrade_temp_url,v.temp_url_expire_time,v.share_password 
from zl_software_version v where v.update_url is not null;
commit;
