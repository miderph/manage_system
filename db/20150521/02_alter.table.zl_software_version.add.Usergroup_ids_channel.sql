alter table zl_software_version add USERGROUP_IDS_CHANNEL VARCHAR2(1024);
alter table zl_software_version add USERGROUP_IDS_MAC2 VARCHAR2(1024);
alter table zl_software_version add USERGROUP_IDS_ZONE2 VARCHAR2(1024);
alter table zl_software_version add USERGROUP_IDS_MODEL2 VARCHAR2(1024);
alter table zl_software_version add USERGROUP_IDS_CHANNEL2 VARCHAR2(1024);

comment on column zl_software_version.USERGROUP_IDS_MAC is '白名单ids-mac';
comment on column zl_software_version.USERGROUP_IDS_ZONE is '白名单ids-zone';
comment on column zl_software_version.USERGROUP_IDS_MODEL is '白名单ids-model';
comment on column zl_software_version.USERGROUP_IDS_CHANNEL is '白名单ids-channel';
comment on column zl_software_version.USERGROUP_IDS_MAC2 is '黑名单ids-mac';
comment on column zl_software_version.USERGROUP_IDS_ZONE2 is '黑名单ids-zone';
comment on column zl_software_version.USERGROUP_IDS_MODEL2 is '黑名单ids-model';
comment on column zl_software_version.USERGROUP_IDS_CHANNEL2 is '黑名单ids-channel';