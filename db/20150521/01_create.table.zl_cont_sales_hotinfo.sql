-- Create table
create table ZL_CONT_SALES_HOTINFO
(
  ID          NUMBER(20),
  C_ID        NUMBER(20),
  TYPE        VARCHAR2(20),
  CHANNEL     VARCHAR2(100),
  HOT_INFO    VARCHAR2(1024),
  ICON_URL    VARCHAR2(1024),
  DESCRIPTION VARCHAR2(1024),
  CREATE_TIME DATE default sysdate,
  MODIFY_TIME DATE default sysdate
);
-- Add comments to the columns 
comment on column ZL_CONT_SALES_HOTINFO.C_ID  is '资产id';
comment on column ZL_CONT_SALES_HOTINFO.ICON_URL  is '图标';
comment on column ZL_CONT_SALES_HOTINFO.HOT_INFO  is '促销信息';
comment on column ZL_CONT_SALES_HOTINFO.TYPE  is 'provider 供应商,shop 商铺,cont 资产';
comment on column ZL_CONT_SALES_HOTINFO.DESCRIPTION  is '描述';
comment on column ZL_CONT_SALES_HOTINFO.CHANNEL  is 'apk合作渠道号';
comment on column ZL_CONT_SALES_HOTINFO.ID  is 'id';

alter table ZL_CONT_SALES_HOTINFO add constraint PK_ZL_CONT_SALES_HOTINFO primary key (ID);
create unique index UNI_ZL_CONT_SALES_HOTINFO on ZL_CONT_SALES_HOTINFO (C_ID, CHANNEL);
