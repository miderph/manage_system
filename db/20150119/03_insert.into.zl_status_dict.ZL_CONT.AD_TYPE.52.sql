insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT' table_name,'AD_TYPE' field_name,'�˳�Ӧ�ù��ͼ' description,'50' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
update zl_status_dict set description='��������ͣ���ͼ' where table_name='ZL_CONT' and field_name='AD_TYPE' and status=51;
insert into zl_status_dict(table_name,field_name,description,status)
   select * from (select 'ZL_CONT' table_name,'AD_TYPE' field_name,'������ͼ' description,'8' status from dual) import
     where not exists(select id from zl_status_dict dict where dict.table_name=import.table_name and dict.field_name=import.field_name and dict.status=import.status);
