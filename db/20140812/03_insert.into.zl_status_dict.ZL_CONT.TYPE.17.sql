insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_APP_DOWNLOAD_URL' table_name,'URL_TYPE' field_name,'自己服务器地址' description,'0' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_APP_DOWNLOAD_URL' table_name,'URL_TYPE' field_name,'普通地址' description,'1' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_APP_DOWNLOAD_URL' table_name,'URL_TYPE' field_name,'baidu云盘' description,'2' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_APP_DOWNLOAD_URL' table_name,'URL_TYPE' field_name,'360云盘' description,'3' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
