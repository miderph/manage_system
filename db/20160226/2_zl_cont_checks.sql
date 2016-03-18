-- Create table
create table ZL_CONT_CHECKS
(
  id                 NUMBER(32),
  code               VARCHAR2(64),
  name               VARCHAR2(255),
  icon               VARCHAR2(1000),
  item_url           VARCHAR2(1000),
  shop               VARCHAR2(255),
  price              VARCHAR2(32),
  sales_num          VARCHAR2(32),
  bate               VARCHAR2(16),
  wangwang           VARCHAR2(64),
  taobaoke_url_short VARCHAR2(255),
  taobaoke_url       VARCHAR2(2000),
  classify           VARCHAR2(255),
  status             NUMBER(8)
);
-- Add comments to the columns 
comment on column ZL_CONT_CHECKS.code
  is '商品id';
comment on column ZL_CONT_CHECKS.name
  is '商品名称';
comment on column ZL_CONT_CHECKS.icon
  is '商品主图';
comment on column ZL_CONT_CHECKS.item_url
  is '商品详情页链接地址';
comment on column ZL_CONT_CHECKS.shop
  is '店铺名称';
comment on column ZL_CONT_CHECKS.price
  is '商品价格(单位：元)';
comment on column ZL_CONT_CHECKS.sales_num
  is '商品月销量';
comment on column ZL_CONT_CHECKS.bate
  is '收入比率';
comment on column ZL_CONT_CHECKS.wangwang
  is '阿里旺旺';
comment on column ZL_CONT_CHECKS.taobaoke_url_short
  is '淘宝客短链接(300天内有效)';
comment on column ZL_CONT_CHECKS.taobaoke_url
  is '淘宝客链接';
comment on column ZL_CONT_CHECKS.classify
  is '一级分类';
comment on column ZL_CONT_CHECKS.status
  is '-1 禁用 1 正常  ';
