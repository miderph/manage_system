create or replace view v_base_checkupdate_client as
select id, rownum row_num, VERSION_NUM,SOFTWARE_INFO,PLAT,ENFORCE_FLAG,UPDATE_URL,DESCRIPTION,PUBLISH_TIME,VER_MAIN,VER_SUB,VER_DEPLOY,VER_BUILD
,usergroup_id,status,url_type,upgrade_temp_url,temp_url_expire_time,share_password
from ZL_SOFTWARE_VERSION
where status =1
order by plat, VER_MAIN desc,VER_SUB desc,VER_DEPLOY desc,VER_BUILD desc;
