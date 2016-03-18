-- Create table
create table ZL_STB_MODEL
(
  ID          NUMBER not null,
  MODEL       VARCHAR2(200) not null,
  NAME        VARCHAR2(200),
  PROVIDER_ID NUMBER(20),
  CREATE_TIME DATE,
  UPDATE_TIME DATE,
  DESCRIPTION VARCHAR2(200)
);
-- Add comments to the table 
comment on table ZL_STB_MODEL
  is '盒子型号';
-- Add comments to the columns 
comment on column ZL_STB_MODEL.MODEL
  is '型号';
comment on column ZL_STB_MODEL.NAME
  is '名称';
comment on column ZL_STB_MODEL.PROVIDER_ID
  is '内容提供商id';
comment on column ZL_STB_MODEL.DESCRIPTION
  is '描述';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ZL_STB_MODEL
  add constraint PK_ZL_STB_MODEL primary key (ID);
