alter table zl_software_version add USERGROUP_IDS_CHANNEL VARCHAR2(1024);
alter table zl_software_version add USERGROUP_IDS_MAC2 VARCHAR2(1024);
alter table zl_software_version add USERGROUP_IDS_ZONE2 VARCHAR2(1024);
alter table zl_software_version add USERGROUP_IDS_MODEL2 VARCHAR2(1024);
alter table zl_software_version add USERGROUP_IDS_CHANNEL2 VARCHAR2(1024);

comment on column zl_software_version.USERGROUP_IDS_MAC is '������ids-mac';
comment on column zl_software_version.USERGROUP_IDS_ZONE is '������ids-zone';
comment on column zl_software_version.USERGROUP_IDS_MODEL is '������ids-model';
comment on column zl_software_version.USERGROUP_IDS_CHANNEL is '������ids-channel';
comment on column zl_software_version.USERGROUP_IDS_MAC2 is '������ids-mac';
comment on column zl_software_version.USERGROUP_IDS_ZONE2 is '������ids-zone';
comment on column zl_software_version.USERGROUP_IDS_MODEL2 is '������ids-model';
comment on column zl_software_version.USERGROUP_IDS_CHANNEL2 is '������ids-channel';