-- Create table
create table ZL_CONT_CHANNEL
(
  ID          NUMBER(20) not null,
  NAME        VARCHAR2(50),
  INTRO       VARCHAR2(255),
  CREATE_TIME DATE default SYSDATE,
  MODIFY_TIME DATE default SYSDATE,
  TYPE        VARCHAR2(10),
  CHANNEL     VARCHAR2(50)
);
-- Add comments to the table 
comment on table ZL_CONT_CHANNEL
  is '站点表';
-- Add comments to the columns 
comment on column ZL_CONT_CHANNEL.ID
  is 'ID';
comment on column ZL_CONT_CHANNEL.NAME
  is '名称';
comment on column ZL_CONT_CHANNEL.INTRO
  is '简介';
comment on column ZL_CONT_CHANNEL.CREATE_TIME
  is '创建时间';
comment on column ZL_CONT_CHANNEL.MODIFY_TIME
  is '修改时间';
comment on column ZL_CONT_CHANNEL.TYPE
  is '类型：apk 应用推广渠道,cont 内容接入渠道';
comment on column ZL_CONT_CHANNEL.CHANNEL
  is 'apk合作渠道号';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ZL_CONT_CHANNEL
  add constraint PK_ZL_CONT_CHANNEL primary key (ID);
