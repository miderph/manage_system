-- Add/modify columns 
alter table ZL_MENU add version number(20);
-- Add comments to the columns 
comment on column ZL_MENU.version
  is 'ʱ����汾��';
