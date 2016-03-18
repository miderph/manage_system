insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_CHANNEL' table_name,'TYPE' field_name,'apk·¢²¼ÇþµÀ' description,'APK' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
