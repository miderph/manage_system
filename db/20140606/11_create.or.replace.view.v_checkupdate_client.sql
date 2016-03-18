create or replace view v_checkupdate_client as
select cc.id version_id,rownum row_num, cc.VERSION_NUM,cc.SOFTWARE_INFO,cc.PLAT,cc.ENFORCE_FLAG,cc.DESCRIPTION,cc.PUBLISH_TIME,cc.usergroup_id,cc.status,url.id url_id,url.version,url.version_code,url.download_url,url.url_type,url.upgrade_temp_url,url.temp_url_expire_time,url.share_password
,apk_md5sum
from v_base_checkupdate_client  cc left join zl_app_download_url url
on cc.id=url.c_id and url.c_type=2
order by cc.VER_MAIN desc,cc.VER_SUB desc,cc.VER_DEPLOY desc,cc.VER_BUILD desc, url.url_type desc;