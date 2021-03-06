alter table ZL_MENU modify USERGROUP_IDS_MAC VARCHAR2(1024);
alter table ZL_MENU modify USERGROUP_IDS_ZONE VARCHAR2(1024);
alter table ZL_MENU modify USERGROUP_IDS_MODEL VARCHAR2(1024);

alter table ZL_MENU add USERGROUP_IDS_CHANNEL VARCHAR2(1024);
alter table ZL_MENU add USERGROUP_IDS_MAC2 VARCHAR2(1024);
alter table ZL_MENU add USERGROUP_IDS_ZONE2 VARCHAR2(1024);
alter table ZL_MENU add USERGROUP_IDS_MODEL2 VARCHAR2(1024);
alter table ZL_MENU add USERGROUP_IDS_CHANNEL2 VARCHAR2(1024);

comment on column ZL_MENU.USERGROUP_IDS_MAC is '白名单ids-mac';
comment on column ZL_MENU.USERGROUP_IDS_ZONE is '白名单ids-zone';
comment on column ZL_MENU.USERGROUP_IDS_MODEL is '白名单ids-model';
comment on column ZL_MENU.USERGROUP_IDS_CHANNEL is '白名单ids-channel';
comment on column ZL_MENU.USERGROUP_IDS_MAC2 is '黑名单ids-mac';
comment on column ZL_MENU.USERGROUP_IDS_ZONE2 is '黑名单ids-zone';
comment on column ZL_MENU.USERGROUP_IDS_MODEL2 is '黑名单ids-model';
comment on column ZL_MENU.USERGROUP_IDS_CHANNEL2 is '黑名单ids-channel';

comment on column ZL_IMG.USE_TYPE is '用途类型: 0 栏目, 1 内容, 2应用截图/商品缩略图';