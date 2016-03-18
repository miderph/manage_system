create or replace view v_base_checkupdate_client as
select id, rownum row_num, VERSION_NUM,SOFTWARE_INFO,PLAT,ENFORCE_FLAG,UPDATE_URL,DESCRIPTION,PUBLISH_TIME,VER_MAIN,VER_SUB,VER_DEPLOY,VER_BUILD
,usergroup_id,status,url_type,upgrade_temp_url,temp_url_expire_time,share_password
,apk_md5sum
   ,cc.USERGROUP_IDS_MAC,cc.USERGROUP_IDS_ZONE,cc.USERGROUP_IDS_MODEL,cc.USERGROUP_IDS_CHANNEL
   ,cc.USERGROUP_IDS_MAC2,cc.USERGROUP_IDS_ZONE2,cc.USERGROUP_IDS_MODEL2,cc.USERGROUP_IDS_CHANNEL2
from ZL_SOFTWARE_VERSION cc
where status =1
;