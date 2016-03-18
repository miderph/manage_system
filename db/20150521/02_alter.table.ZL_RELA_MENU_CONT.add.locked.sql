alter table ZL_RELA_MENU_CONT add LOCKED        NUMBER(1);
comment on column ZL_RELA_MENU_CONT.LOCKED is '是否锁定 1 是，0或者空 否';