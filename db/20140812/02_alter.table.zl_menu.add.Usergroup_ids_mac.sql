alter table zl_menu add Usergroup_ids_mac varchar2(2048);
comment on column zl_menu.Usergroup_ids_mac is '������ids-mac';

update zl_menu set Usergroup_ids_mac=Usergroup_id where Usergroup_id is not null;

alter table zl_menu add Usergroup_ids_zone varchar2(2048);
comment on column zl_menu.Usergroup_ids_zone is '������ids-zone';

alter table zl_menu add Usergroup_ids_model varchar2(2048);
comment on column zl_menu.Usergroup_ids_model is '������ids-model';

