-- Create table
create table ZL_ORDER
(
  id           NUMBER(32) not null,
  code         VARCHAR2(50),
  c_id         NUMBER(32),
  c_name       VARCHAR2(255),
  c_desc       VARCHAR2(1000),
  total_fee    NUMBER(32,2),
  out_trade_no VARCHAR2(50),
  client_ip    VARCHAR2(32),
  create_time  DATE,
  expired_time DATE,
  status       NUMBER(2),
  trade_type   VARCHAR2(20),
  attach       VARCHAR2(255),
  address_id   NUMBER(32)
);
-- Add comments to the columns 
comment on column ZL_ORDER.id
  is '����id';
comment on column ZL_ORDER.code
  is '������ţ��32λ��';
comment on column ZL_ORDER.c_id
  is '��Ʒ���';
comment on column ZL_ORDER.c_name
  is '��Ʒ����';
comment on column ZL_ORDER.c_desc
  is '��Ʒ����';
comment on column ZL_ORDER.total_fee
  is '���,Ԫ';
comment on column ZL_ORDER.out_trade_no
  is '�̻�������-΢��֧��ʹ��';
comment on column ZL_ORDER.client_ip
  is '�ͻ���ip';
comment on column ZL_ORDER.create_time
  is '��������ʱ��';
comment on column ZL_ORDER.expired_time
  is '����ʧЧʱ��';
comment on column ZL_ORDER.status
  is '״̬ 0��ʼ  1���µ�  9 �Ѹ���   10 ֧���ص��ɹ�  11 ֧��ʧ��   12 ��ȡ��  13 �ѹر� ';
comment on column ZL_ORDER.trade_type
  is '΢��֧���ã� JSAPI ��';
comment on column ZL_ORDER.attach
  is '����������Ϣ';
comment on column ZL_ORDER.address_id
  is '�ջ���ַid';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ZL_ORDER
  add constraint PK_ZL_ORDER_ID primary key (ID);
