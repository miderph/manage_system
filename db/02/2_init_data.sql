prompt PL/SQL Developer import file
prompt Created on 2014��1��24�� by miderph.yan
set feedback off
set define off
prompt Disabling triggers for ZL_MODULES...
alter table ZL_MODULES disable all triggers;
prompt Disabling triggers for ZL_OPERATORS...
alter table ZL_OPERATORS disable all triggers;
prompt Disabling triggers for ZL_ROLES...
alter table ZL_ROLES disable all triggers;
prompt Disabling triggers for ZL_STB_PREFIXES...
alter table ZL_STB_PREFIXES disable all triggers;
prompt Loading ZL_MODULES...
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (11, '�û�����', 'operators', 1, 2, 1, '/1/11');
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (12, '��ɫ����', 'roles', 1, 2, 1, '/1/12');
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (13, 'վ�����', 'siteAdmin', 1, 2, 1, '/1/13');
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (14, '��Ŀ����', 'menuStructure', 1, 2, 1, '/1/14');
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (15, '��Ƶ�ʲ�', 'contVideo', 1, 2, 1, '/1/15');
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (16, '����ʲ�', 'contAppstore', 1, 2, 1, '/1/16');
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (17, '��Ŀ�ʲ�����', 'menuAssetsRelaPanel', 1, 2, 1, '/1/17');
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (18, 'Ӧ�����ر���', 'appdownloadreport', 1, 2, 1, '/1/18');
insert into ZL_MODULES (id, name, url, parent_id, levels, leaf, path)
values (19, 'ǰ׺����', 'stbPrefix', 1, 2, 1, '/1/19');
commit;
prompt 9 records loaded
prompt Loading ZL_ROLES...
insert into ZL_ROLES (id, name, site_ids, provider_ids, create_time, update_time, module_ids)
values (1, '��������Ա', null, null, to_date('21-01-2014 16:02:21', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-01-2014 20:53:37', 'dd-mm-yyyy hh24:mi:ss'), '1,11,12,13,19');
commit;
prompt Enabling triggers for ZL_MODULES...
alter table ZL_MODULES enable all triggers;
prompt Enabling triggers for ZL_OPERATORS...
alter table ZL_OPERATORS enable all triggers;
prompt Enabling triggers for ZL_ROLES...
alter table ZL_ROLES enable all triggers;
prompt Enabling triggers for ZL_STB_PREFIXES...
alter table ZL_STB_PREFIXES enable all triggers;
set feedback on
set define on
prompt Done.
