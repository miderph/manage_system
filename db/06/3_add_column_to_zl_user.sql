-- Add/modify columns 
alter table ZL_USER add tv_chipmodelnum VARCHAR2(200);
alter table ZL_USER add tv_deviceid VARCHAR2(200);
-- Add comments to the columns 
comment on column ZL_USER.tv_chipmodelnum
  is '������оƬ�ͺ�';
comment on column ZL_USER.tv_deviceid
  is '�������豸�ͺ�';
