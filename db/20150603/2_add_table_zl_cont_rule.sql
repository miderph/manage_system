-- Create table
create table ZL_CONT_RULE
(
  id                NUMBER(20) not null,
  name              VARCHAR2(255),
  price             VARCHAR2(1024),
  price_rela        VARCHAR2(20),
  provider_ids      VARCHAR2(1024),
  provider_rela     VARCHAR2(20),
  shop_ids          VARCHAR2(1024),
  shop_rela         VARCHAR2(20),
  category          VARCHAR2(1024),
  category_rela     VARCHAR2(20),
  category_new_menu NUMBER(1),
  create_time			 	Date default sysdate,
  modify_time				Date default sysdate
);
-- Add comments to the columns 
comment on column ZL_CONT_RULE.price
  is '�ɶ��';
comment on column ZL_CONT_RULE.price_rela
  is 'ȡֵ=��>��>=��<��<=��in';
comment on column ZL_CONT_RULE.provider_ids
  is '�ɶ��';
comment on column ZL_CONT_RULE.provider_rela
  is 'ȡֵ=��in';
comment on column ZL_CONT_RULE.shop_ids
  is '�ɶ��';
comment on column ZL_CONT_RULE.shop_rela
  is 'ȡֵ=��in';
comment on column ZL_CONT_RULE.category
  is '�ɶ��';
comment on column ZL_CONT_RULE.category_rela
  is 'ȡֵ=��in';
comment on column ZL_CONT_RULE.category_new_menu
  is '�Ƿ��Զ���������Ŀ  1 �� �� 0���߿� ��';
