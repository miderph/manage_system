alter table zl_cont_sales add post_price NUMBER(20,2);
comment on column zl_cont_sales.post_price is 'тк╥я';

update zl_cont_sales set post_price=0 where post_price is null;
