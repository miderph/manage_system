alter table ZL_ROLES add menu_IDS VARCHAR2(1000);
alter table ZL_ROLES add group_IDS VARCHAR2(1000);

comment on column ZL_ROLES.menu_IDS is '��Ŀid,�����,���ŷָ�';
comment on column ZL_ROLES.group_IDS is '������id,�����,���ŷָ�';
