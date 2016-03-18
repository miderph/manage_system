insert into ZL_MODULES (ID, NAME, URL, PARENT_ID, LEVELS, LEAF, PATH)
   select * from (select 26 ID, 'Ö§¸¶·½Ê½' NAME, 'contSalesPayType' URL, 1 PARENT_ID, 2 LEVELS, 1 LEAF, '/1/26' PATH from dual) import
     where not exists(select id from ZL_MODULES mod where mod.URL=import.URL or mod.id=import.id);