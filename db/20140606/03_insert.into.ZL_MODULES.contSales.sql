insert into ZL_MODULES (ID, NAME, URL, PARENT_ID, LEVELS, LEAF, PATH)
   select * from (select 24 ID, '¹ºÎï×Ê²ú' NAME, 'contSales' URL, 1 PARENT_ID, 2 LEVELS, 1 LEAF, '/1/24' PATH from dual) import
     where not exists(select id from ZL_MODULES mod where mod.URL=import.URL or mod.id=import.id);