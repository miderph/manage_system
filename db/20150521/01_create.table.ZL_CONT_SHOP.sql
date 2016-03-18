-- Create table
create table ZL_CONT_SHOP
(
  ID          NUMBER(20) not null,
  NAME        VARCHAR2(50),
  CREDIT      VARCHAR2(50),
  HOT_INFO    VARCHAR2(255),
  ICON_URL    VARCHAR2(255),
  INTRO       VARCHAR2(255),
  CREATE_TIME DATE default SYSDATE,
  MODIFY_TIME DATE default SYSDATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table ZL_CONT_SHOP
  is 'վ���';
-- Add comments to the columns 
comment on column ZL_CONT_SHOP.ID
  is 'ID';
comment on column ZL_CONT_SHOP.NAME
  is '����';
comment on column ZL_CONT_SHOP.CREDIT
  is '���ö�';
comment on column ZL_CONT_SHOP.HOT_INFO
  is '������Ϣ';
comment on column ZL_CONT_SHOP.ICON_URL
  is 'ͼ���ַ';
comment on column ZL_CONT_SHOP.INTRO
  is '���';
comment on column ZL_CONT_SHOP.CREATE_TIME
  is '����ʱ��';
comment on column ZL_CONT_SHOP.MODIFY_TIME
  is '�޸�ʱ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ZL_CONT_SHOP
  add constraint PK_ZL_CONT_SHOP primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
