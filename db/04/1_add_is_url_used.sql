-- Add/modify columns 
alter table ZL_RELA_MENU_CONT add is_url_used NUMBER(5);
-- Add comments to the columns 
comment on column ZL_RELA_MENU_CONT.is_url_used
  is '0使用url_little,竖图 1 使用url，2 使用url_icon，3 使用 URL_4_SQUARES，4使用 URL_6_SQUARES';
