prompt Importing table ZL_MODULES...
set feedback off
set define off
insert into ZL_MODULES (ID, NAME, URL, PARENT_ID, LEVELS, LEAF, PATH)
values (31, 'Ã‘±¶øÕ…Ã∆∑', 'contCheck', 1, 2, 1, '/1/31');
update zl_roles set module_ids=module_ids||',31' where id=215448704;
commit;
prompt Done.
