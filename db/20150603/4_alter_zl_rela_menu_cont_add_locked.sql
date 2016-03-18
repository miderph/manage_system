alter table zl_rela_menu_cont add  locked number(1);
comment on column ZL_RELA_MENU_CONT.locked
  is '是否锁定 1 是 ； 0或者空 否 ';
