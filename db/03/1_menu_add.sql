-- Add/modify columns 
alter table zl_MENU add is_shortcut number(1) default 0;
alter table zl_MENU add shortcut_contid number(20);
-- Add comments to the columns 
comment on column zl_MENU.is_shortcut
  is '��Ŀ��ݷ�ʽ 1�� 0 ��';
comment on column zl_MENU.shortcut_contid
  is '��Ŀ��ݷ�ʽ��������id';
