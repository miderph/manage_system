-- Create table
create table ZL_CONT_SALES_IMPORT
(
  c_id          NUMBER(20),
  name          VARCHAR2(255),
  hot_info      VARCHAR2(2000),
  fake_price    NUMBER(20,2),
  sale_price    NUMBER(20,2),
  real_price    NUMBER(20,2),
  disaccount    NUMBER(20,2),
  key_words     VARCHAR2(255),
  cp_name       VARCHAR2(255),
  pay_type_ids  VARCHAR2(1000),
  provider_id   NUMBER(20),
  sales_no      VARCHAR2(100),
  sub_cp_name   VARCHAR2(100),
  post_price    NUMBER(20,2),
  id            NUMBER(20) not null,
  pinyin        VARCHAR2(255),
  update_status NUMBER(20),
  update_count  NUMBER(20),
  modify_time   DATE,
  create_time   DATE,
  import_status NUMBER(5),
  icon_url1     VARCHAR2(255),
  icon_url2     VARCHAR2(255),
  isp_icon_url1 VARCHAR2(255),
  isp_icon_url2 VARCHAR2(255),
  has_video   NUMBER(5)
);
-- Add comments to the columns 
comment on column ZL_CONT_SALES_IMPORT.sales_no
  is '供应商的商品标号';
comment on column ZL_CONT_SALES_IMPORT.sub_cp_name
  is '供应商名称-副标题';
comment on column ZL_CONT_SALES_IMPORT.post_price
  is '运费';
/
