-- Create table
create table zl_order_address
(
  id         number(32),
  username   varchar2(128),
  telnumber  varchar2(64),
  postalcode varchar2(32),
  province   varchar2(128),
  city       varchar2(128),
  county     varchar2(128),
  detailinfo varchar2(1024)
)
;
-- Add comments to the columns 
comment on column zl_order_address.username
  is '联系人';
comment on column zl_order_address.telnumber
  is '联系电话';
comment on column zl_order_address.postalcode
  is '邮编';
comment on column zl_order_address.province
  is '省';
comment on column zl_order_address.city
  is '市';
comment on column zl_order_address.county
  is '区';
comment on column zl_order_address.detailinfo
  is '详细地址';
