alter table ZL_HOTWORDS add hid NUMBER(20);
comment on column ZL_HOTWORDS.hid is '对应热词的id';

create table zl_hotwords_20150430 as
select * from zl_hotwords t;

truncate table zl_hotwords;

insert into zl_hotwords(id,hid,hotword,site_id)
select min(id) id, min(id) hid, hotword, 0 site_id from zl_hotwords_20150430 t group by hotword;

insert into zl_hotwords(id,hid,hotword,site_id)
select ZLSQ_DATA.NEXTVAL id, t2.id hid,t1.hotword,t1.site_id from zl_hotwords_20150430 t1, zl_hotwords t2
where t1.hotword=t2.hotword and t2.site_id=0;