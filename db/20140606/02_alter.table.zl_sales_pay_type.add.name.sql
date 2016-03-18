alter table zl_sales_pay_type add name varchar2(100);
comment on column zl_sales_pay_type.name is 'Ãû³Æ';
update zl_sales_pay_type set name=description;