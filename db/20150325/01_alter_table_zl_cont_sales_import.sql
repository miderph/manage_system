-- Add/modify columns 
alter table ZL_CONT_SALES_IMPORT add HAS_VIDEO number(1);
alter table ZL_CONT_SALES_IMPORT add url varchar2(4000);
alter table ZL_CONT_SALES_IMPORT add icon_url3 VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add isp_icon_url3 VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add manage_user VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add audit_review VARCHAR2(500);
alter table ZL_CONT_SALES_IMPORT add shop_type VARCHAR2(2);
-- Add comments to the columns 
comment on column ZL_CONT_SALES_IMPORT.icon_url1
  is '推荐图方图';
comment on column ZL_CONT_SALES_IMPORT.icon_url2
  is '推荐图竖图';
comment on column ZL_CONT_SALES_IMPORT.isp_icon_url1
  is 'ISP原始推荐图方图';
comment on column ZL_CONT_SALES_IMPORT.isp_icon_url2
  is 'ISP原始推荐图方图';
comment on column ZL_CONT_SALES_IMPORT.url
  is '点击后跳转地址';
comment on column ZL_CONT_SALES_IMPORT.icon_url3
  is '推荐图横图';
comment on column ZL_CONT_SALES_IMPORT.isp_icon_url3
  is 'ISP原始推荐图横图';
comment on column ZL_CONT_SALES_IMPORT.manage_user
  is '发布者';
comment on column ZL_CONT_SALES_IMPORT.audit_review
  is '小编评论';
comment on column ZL_CONT_SALES_IMPORT.shop_type
  is '店铺类型';
-- Add comments to the columns
alter table ZL_CONT_SALES_IMPORT add start_time DATE;
alter table ZL_CONT_SALES_IMPORT add end_time DATE;
comment on column ZL_CONT_SALES_IMPORT.start_time
  is '开始时间';
comment on column ZL_CONT_SALES_IMPORT.end_time
  is '结束时间';