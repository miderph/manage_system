-- Add/modify columns 
alter table zl_MENU add is_shortcut number(1) default 0;
alter table zl_MENU add shortcut_contid number(20);
-- Add comments to the columns 
comment on column zl_MENU.is_shortcut
  is '栏目快捷方式 1是 0 否';
comment on column zl_MENU.shortcut_contid
  is '栏目快捷方式关联内容id';
