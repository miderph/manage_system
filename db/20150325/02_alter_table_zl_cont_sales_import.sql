-- Add/modify columns 
alter table ZL_CONT_SALES add HAS_VIDEO number(1);
alter table ZL_CONT_SALES add url varchar2(4000);
alter table ZL_CONT_SALES add manage_user VARCHAR2(255);
alter table ZL_CONT_SALES add audit_review VARCHAR2(500);
alter table ZL_CONT_SALES add shop_type VARCHAR2(2);
-- Add comments to the columns 
comment on column ZL_CONT_SALES.url
  is '点击后跳转地址';
comment on column ZL_CONT_SALES.icon_url3
  is '推荐图横图';
comment on column ZL_CONT_SALES.isp_icon_url3
  is 'ISP原始推荐图横图';
comment on column ZL_CONT_SALES.manage_user
  is '发布者';
comment on column ZL_CONT_SALES.audit_review
  is '小编评论';
comment on column ZL_CONT_SALES.shop_type
  is '店铺类型';
-- Add comments to the columns
alter table ZL_CONT_SALES add start_time DATE;
alter table ZL_CONT_SALES add end_time DATE;
comment on column ZL_CONT_SALES.start_time
  is '开始时间';
comment on column ZL_CONT_SALES.end_time
  is '结束时间';