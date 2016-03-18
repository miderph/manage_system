update  ZL_MODULES t  set NAME='统计报表',URL='reports',PARENT_ID=1,LEVELS=2,LEAF=1,PATH='/1/18' where id =18;
insert into ZL_MODULES (ID, NAME, URL, PARENT_ID, LEVELS, LEAF, PATH) values (23, '热词管理', 'hotWord', 1, 2, 1, '/1/23');
commit;