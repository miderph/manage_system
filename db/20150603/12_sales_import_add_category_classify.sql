-- Add/modify columns 
alter table ZL_CONT_SALES_IMPORT add category varchar2(255);
alter table ZL_CONT_SALES_IMPORT add classify varchar2(255);
-- Add comments to the columns 
comment on column ZL_CONT_SALES_IMPORT.category
  is '��������';
comment on column ZL_CONT_SALES_IMPORT.classify
  is '�������';
