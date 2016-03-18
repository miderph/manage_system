alter table ZL_ROLES add menu_IDS VARCHAR2(1000);
alter table ZL_ROLES add group_IDS VARCHAR2(1000);

comment on column ZL_ROLES.menu_IDS is '栏目id,多个用,逗号分隔';
comment on column ZL_ROLES.group_IDS is '测试组id,多个用,逗号分隔';
