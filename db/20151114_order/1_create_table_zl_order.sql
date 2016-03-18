-- Create table
create table ZL_ORDER
(
  id           NUMBER(32) not null,
  code         VARCHAR2(50),
  c_id         NUMBER(32),
  c_name       VARCHAR2(255),
  c_desc       VARCHAR2(1000),
  total_fee    NUMBER(32,2),
  out_trade_no VARCHAR2(50),
  client_ip    VARCHAR2(32),
  create_time  DATE,
  expired_time DATE,
  status       NUMBER(2),
  trade_type   VARCHAR2(20),
  attach       VARCHAR2(255),
  address_id   NUMBER(32)
);
-- Add comments to the columns 
comment on column ZL_ORDER.id
  is '订单id';
comment on column ZL_ORDER.code
  is '订单编号，最长32位。';
comment on column ZL_ORDER.c_id
  is '商品编号';
comment on column ZL_ORDER.c_name
  is '商品名称';
comment on column ZL_ORDER.c_desc
  is '商品描述';
comment on column ZL_ORDER.total_fee
  is '金额,元';
comment on column ZL_ORDER.out_trade_no
  is '商户订单号-微信支付使用';
comment on column ZL_ORDER.client_ip
  is '客户端ip';
comment on column ZL_ORDER.create_time
  is '订单创建时间';
comment on column ZL_ORDER.expired_time
  is '订单失效时间';
comment on column ZL_ORDER.status
  is '状态 0初始  1已下单  9 已付款   10 支付回调成功  11 支付失败   12 已取消  13 已关闭 ';
comment on column ZL_ORDER.trade_type
  is '微信支付用： JSAPI ，';
comment on column ZL_ORDER.attach
  is '订单附加信息';
comment on column ZL_ORDER.address_id
  is '收货地址id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ZL_ORDER
  add constraint PK_ZL_ORDER_ID primary key (ID);
