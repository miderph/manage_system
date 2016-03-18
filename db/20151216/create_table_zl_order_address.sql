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
  is '��ϵ��';
comment on column zl_order_address.telnumber
  is '��ϵ�绰';
comment on column zl_order_address.postalcode
  is '�ʱ�';
comment on column zl_order_address.province
  is 'ʡ';
comment on column zl_order_address.city
  is '��';
comment on column zl_order_address.county
  is '��';
comment on column zl_order_address.detailinfo
  is '��ϸ��ַ';
