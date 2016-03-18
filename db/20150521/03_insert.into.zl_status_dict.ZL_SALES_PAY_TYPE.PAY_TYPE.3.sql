insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_SALES_PAY_TYPE' table_name,'PAY_TYPE' field_name,'ÌÔ±¦' description,'3' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_SALES_PAY_TYPE' table_name,'PAY_TYPE' field_name,'ÆäËû¶þÎ¬Âë' description,'4' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
