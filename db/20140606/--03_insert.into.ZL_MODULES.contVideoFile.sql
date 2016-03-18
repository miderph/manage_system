insert into ZL_MODULES (ID, NAME, URL, PARENT_ID, LEVELS, LEAF, PATH)
   select * from (select 27 ID, ' ”∆µ≤•∑≈µÿ÷∑' NAME, 'contVideoFile' URL, 1 PARENT_ID, 2 LEVELS, 1 LEAF, '/1/27' PATH from dual) import
     where not exists(select id from ZL_MODULES mod where mod.URL=import.URL or mod.id=import.id);