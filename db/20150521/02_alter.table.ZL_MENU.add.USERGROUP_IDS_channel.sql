alter table ZL_MENU modify USERGROUP_IDS_MAC VARCHAR2(1024);
alter table ZL_MENU modify USERGROUP_IDS_ZONE VARCHAR2(1024);
alter table ZL_MENU modify USERGROUP_IDS_MODEL VARCHAR2(1024);

alter table ZL_MENU add USERGROUP_IDS_CHANNEL VARCHAR2(1024);
alter table ZL_MENU add USERGROUP_IDS_MAC2 VARCHAR2(1024);
alter table ZL_MENU add USERGROUP_IDS_ZONE2 VARCHAR2(1024);
alter table ZL_MENU add USERGROUP_IDS_MODEL2 VARCHAR2(1024);
alter table ZL_MENU add USERGROUP_IDS_CHANNEL2 VARCHAR2(1024);

comment on column ZL_MENU.USERGROUP_IDS_MAC is '������ids-mac';
comment on column ZL_MENU.USERGROUP_IDS_ZONE is '������ids-zone';
comment on column ZL_MENU.USERGROUP_IDS_MODEL is '������ids-model';
comment on column ZL_MENU.USERGROUP_IDS_CHANNEL is '������ids-channel';
comment on column ZL_MENU.USERGROUP_IDS_MAC2 is '������ids-mac';
comment on column ZL_MENU.USERGROUP_IDS_ZONE2 is '������ids-zone';
comment on column ZL_MENU.USERGROUP_IDS_MODEL2 is '������ids-model';
comment on column ZL_MENU.USERGROUP_IDS_CHANNEL2 is '������ids-channel';

comment on column ZL_IMG.USE_TYPE is '��;����: 0 ��Ŀ, 1 ����, 2Ӧ�ý�ͼ/��Ʒ����ͼ';