-- Add/modify columns 
alter table ZL_MENU add version number(20);
-- Add comments to the columns 
comment on column ZL_MENU.version
  is 'Ê±¼ä´Á°æ±¾ºÅ';
