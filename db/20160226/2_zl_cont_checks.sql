-- Create table
create table ZL_CONT_CHECKS
(
  id                 NUMBER(32),
  code               VARCHAR2(64),
  name               VARCHAR2(255),
  icon               VARCHAR2(1000),
  item_url           VARCHAR2(1000),
  shop               VARCHAR2(255),
  price              VARCHAR2(32),
  sales_num          VARCHAR2(32),
  bate               VARCHAR2(16),
  wangwang           VARCHAR2(64),
  taobaoke_url_short VARCHAR2(255),
  taobaoke_url       VARCHAR2(2000),
  classify           VARCHAR2(255),
  status             NUMBER(8)
);
-- Add comments to the columns 
comment on column ZL_CONT_CHECKS.code
  is '��Ʒid';
comment on column ZL_CONT_CHECKS.name
  is '��Ʒ����';
comment on column ZL_CONT_CHECKS.icon
  is '��Ʒ��ͼ';
comment on column ZL_CONT_CHECKS.item_url
  is '��Ʒ����ҳ���ӵ�ַ';
comment on column ZL_CONT_CHECKS.shop
  is '��������';
comment on column ZL_CONT_CHECKS.price
  is '��Ʒ�۸�(��λ��Ԫ)';
comment on column ZL_CONT_CHECKS.sales_num
  is '��Ʒ������';
comment on column ZL_CONT_CHECKS.bate
  is '�������';
comment on column ZL_CONT_CHECKS.wangwang
  is '��������';
comment on column ZL_CONT_CHECKS.taobaoke_url_short
  is '�Ա��Ͷ�����(300������Ч)';
comment on column ZL_CONT_CHECKS.taobaoke_url
  is '�Ա�������';
comment on column ZL_CONT_CHECKS.classify
  is 'һ������';
comment on column ZL_CONT_CHECKS.status
  is '-1 ���� 1 ����  ';
