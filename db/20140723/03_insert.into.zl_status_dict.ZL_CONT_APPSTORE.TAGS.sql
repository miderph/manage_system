insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_APPSTORE' table_name,'TAGS' field_name,'Êó±ê' description,'Êó±ê' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_APPSTORE' table_name,'TAGS' field_name,'ÊÖ±ú' description,'ÊÖ±ú' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_APPSTORE' table_name,'TAGS' field_name,'¿ÕÊó' description,'¿ÕÊó' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_APPSTORE' table_name,'TAGS' field_name,'Ìå¸ÐÒ£¿ØÆ÷' description,'Ìå¸Ð' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_APPSTORE' table_name,'TAGS' field_name,'¾Å¼üÒ£¿ØÆ÷' description,'¾Å¼ü' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
