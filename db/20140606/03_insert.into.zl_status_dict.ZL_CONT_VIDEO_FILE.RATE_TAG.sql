insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_VIDEO_FILE' table_name,'RATE_TAG' field_name,'����' description,'gd' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_VIDEO_FILE' table_name,'RATE_TAG' field_name,'����' description,'hd' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_VIDEO_FILE' table_name,'RATE_TAG' field_name,'����' description,'cd' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT_VIDEO_FILE' table_name,'RATE_TAG' field_name,'����' description,'sd' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
