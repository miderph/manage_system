alter table ZL_AREA add area_level NUMBER(5);
comment on column ZL_AREA.area_level is '¼¶±ð';

update ZL_AREA set area_level=1 where parent_id=0;
update ZL_AREA set area_level=2 where parent_id in (select id from ZL_AREA where parent_id=0);
update ZL_AREA set area_level=3 where parent_id in (select id from ZL_AREA where parent_id in (select id from ZL_AREA where parent_id=0));
