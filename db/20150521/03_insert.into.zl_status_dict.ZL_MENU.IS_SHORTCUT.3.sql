insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_MENU' table_name,'IS_SHORTCUT' field_name,'分渠道活动广告栏目' description,'3' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);

