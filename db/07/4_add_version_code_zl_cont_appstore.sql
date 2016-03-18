-- Add/modify columns 
alter table ZL_CONT_APPSTORE add version_code number(10);
-- Add comments to the columns 
comment on column ZL_CONT_APPSTORE.version_code
  is '°æ±¾ºÅcode£¬Êý×Ö';
