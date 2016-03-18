-- Add/modify columns 
alter table ZL_USER add tv_chipmodelnum VARCHAR2(200);
alter table ZL_USER add tv_deviceid VARCHAR2(200);
-- Add comments to the columns 
comment on column ZL_USER.tv_chipmodelnum
  is '机顶盒芯片型号';
comment on column ZL_USER.tv_deviceid
  is '机顶盒设备型号';
