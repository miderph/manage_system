insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_APP_DOWNLOAD_URL' table_name,'URL_TYPE' field_name,'�Լ���������ַ' description,'0' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_APP_DOWNLOAD_URL' table_name,'URL_TYPE' field_name,'��ͨ��ַ' description,'1' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_APP_DOWNLOAD_URL' table_name,'URL_TYPE' field_name,'baidu����' description,'2' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_APP_DOWNLOAD_URL' table_name,'URL_TYPE' field_name,'360����' description,'3' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
