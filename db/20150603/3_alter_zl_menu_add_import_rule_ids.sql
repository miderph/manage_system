-- Add/modify columns 
alter table ZL_MENU add import_rule_ids varchar2(255);
-- Add comments to the columns 
comment on column ZL_MENU.import_rule_ids
  is '自动编排规则ids,';
