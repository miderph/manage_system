alter table zl_cont_sales add sales_no varchar2(100);
comment on column zl_cont_sales.sales_no is '供应商的商品标号';
alter table zl_cont_sales add sub_cp_name varchar2(100);
comment on column zl_cont_sales.sub_cp_name is '供应商名称-副标题';

update zl_cont_sales set sales_no=substr(hot_info,6,7) where sales_no is null;
--update zl_cont_sales set hot_info=substr(hot_info,14);
--update zl_cont_sales set sub_cp_name=cp_name;