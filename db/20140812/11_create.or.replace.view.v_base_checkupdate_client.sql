create or replace view v_base_checkupdate_client as
select id, rownum row_num, VERSION_NUM,SOFTWARE_INFO,PLAT,ENFORCE_FLAG,UPDATE_URL,DESCRIPTION,PUBLISH_TIME,VER_MAIN,VER_SUB,VER_DEPLOY,VER_BUILD
,usergroup_id,status,url_type,upgrade_temp_url,temp_url_expire_time,share_password
,apk_md5sum,usergroup_ids_mac,usergroup_ids_zone,usergroup_ids_model
from ZL_SOFTWARE_VERSION
where status =1;