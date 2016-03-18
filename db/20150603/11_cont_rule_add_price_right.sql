-- Add/modify columns 
alter table ZL_CONT_RULE modify price VARCHAR2(255);
alter table ZL_CONT_RULE add price_right VARCHAR2(255);
-- Add comments to the columns 
comment on column ZL_CONT_RULE.price_rela
  is '取值=、>、>=、<、<=、in、between';
comment on column ZL_CONT_RULE.price_right
  is 'price_rela 等于 between时有值,只能取一个值';
