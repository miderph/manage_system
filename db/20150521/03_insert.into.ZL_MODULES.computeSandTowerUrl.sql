insert into ZL_MODULES (ID, NAME, URL, PARENT_ID, LEVELS, LEAF, PATH)
   select * from (select 27 ID, 'É³ËþµØÖ·' NAME, 'computeSandTowerUrl' URL, 1 PARENT_ID, 2 LEVELS, 1 LEAF, '/1/27' PATH from dual) import
     where not exists(select id from ZL_MODULES mod where mod.URL=import.URL or mod.id=import.id);
update zl_roles set module_ids=module_ids||',27' where id=1 and module_ids not like '%,27%';
