-- Add/modify columns 
alter table ZL_CONT_SALES_IMPORT add category VARCHAR2(255);
-- Add comments to the columns 
comment on column ZL_CONT_SALES_IMPORT.category
  is '分类名称';
alter table ZL_CONT_SALES_IMPORT add classify VARCHAR2(255);
-- Add comments to the columns 
comment on column ZL_CONT_SALES_IMPORT.classify
  is '类别名称';
