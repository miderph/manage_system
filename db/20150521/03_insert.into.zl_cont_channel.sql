insert into zl_cont_channel(name,intro,CHANNEL,type)
select description name,description intro,substr(plat,instr(plat,'_',15)+1) channel, 'apk' type from ZL_SOFTWARE_VERSION t where version_num='2.19.1';

update zl_cont_channel set name=replace(name,'2.19.1','');
update zl_cont_channel set intro=name;
update zl_cont_channel set name=replace(name,'应用导航-','');
update zl_cont_channel set name=replace(name,'购好东西-','');

delete from zl_cont_channel t1 where intro like '购好东西%' and exists(select 1 from zl_cont_channel t2 where t2.channel=t1.channel and t2.rowid>t1.rowid);
delete from zl_cont_channel t1 where exists(select 1 from zl_cont_channel t2 where t2.channel=t1.channel and t2.rowid<t1.rowid);
commit;
