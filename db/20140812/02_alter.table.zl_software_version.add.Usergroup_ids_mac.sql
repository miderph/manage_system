alter table zl_software_version add Usergroup_ids_mac varchar2(2048);
comment on column zl_software_version.Usergroup_ids_mac is '������ids-mac';

update zl_software_version set Usergroup_ids_mac=Usergroup_id where Usergroup_id is not null;

alter table zl_software_version add Usergroup_ids_zone varchar2(2048);
comment on column zl_software_version.Usergroup_ids_zone is '������ids-zone';

alter table zl_software_version add Usergroup_ids_model varchar2(2048);
comment on column zl_software_version.Usergroup_ids_model is '������ids-model';

