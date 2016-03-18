insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_USER_GROUP' table_name,'TYPE' field_name,'盒子型号集合' description,'model' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
