alter table zl_cont_sales_import add  bitmask_price varchar2(2) ; 
comment on column zl_cont_sales_import.bitmask_price is '拍下改价标识';

alter table zl_cont_sales add  bitmask_price varchar2(2) ; 
comment on column zl_cont_sales.bitmask_price is '拍下改价标识';