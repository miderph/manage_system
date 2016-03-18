-- Add/modify columns 
alter table ZL_CONT_APPSTORE add tags varchar2(200);
-- Add comments to the columns 
comment on column ZL_CONT_APPSTORE.tags
  is '标签属性，多个英文逗号分隔,';
