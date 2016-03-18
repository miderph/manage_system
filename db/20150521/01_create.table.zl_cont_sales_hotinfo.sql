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
comment on column ZL_CONT_SALES_HOTINFO.C_ID  is '�ʲ�id';
comment on column ZL_CONT_SALES_HOTINFO.ICON_URL  is 'ͼ��';
comment on column ZL_CONT_SALES_HOTINFO.HOT_INFO  is '������Ϣ';
comment on column ZL_CONT_SALES_HOTINFO.TYPE  is 'provider ��Ӧ��,shop ����,cont �ʲ�';
comment on column ZL_CONT_SALES_HOTINFO.DESCRIPTION  is '����';
comment on column ZL_CONT_SALES_HOTINFO.CHANNEL  is 'apk����������';
comment on column ZL_CONT_SALES_HOTINFO.ID  is 'id';

alter table ZL_CONT_SALES_HOTINFO add constraint PK_ZL_CONT_SALES_HOTINFO primary key (ID);
create unique index UNI_ZL_CONT_SALES_HOTINFO on ZL_CONT_SALES_HOTINFO (C_ID, CHANNEL);
