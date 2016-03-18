update ZL_RELA_MENU_CONT set LOCKED=0 where locked is null;
alter table ZL_RELA_MENU_CONT modify LOCKED default 0 not null;
