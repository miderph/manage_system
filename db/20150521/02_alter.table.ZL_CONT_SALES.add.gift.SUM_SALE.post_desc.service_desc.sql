alter table ZL_CONT_SALES modify URL VARCHAR2(1024);

alter table ZL_CONT_SALES add gift VARCHAR2(1024);
--SUM_STOCK �������
alter table ZL_CONT_SALES add SUM_SALE VARCHAR2(1024);
alter table ZL_CONT_SALES add post_desc VARCHAR2(1024);
alter table ZL_CONT_SALES add service_desc VARCHAR2(1024);
comment on column ZL_CONT_SALES.gift is '��Ʒ';
comment on column ZL_CONT_SALES.SUM_SALE is '����';
comment on column ZL_CONT_SALES.post_desc is '�˷�����';
comment on column ZL_CONT_SALES.service_desc is '��������';

alter table ZL_CONT_SALES add shop_id number(20);
alter table ZL_CONT_SALES add channel_id number(20);
alter table ZL_CONT_SALES add price_desc VARCHAR2(100);
comment on column ZL_CONT_SALES.shop_id is '����id';
comment on column ZL_CONT_SALES.channel_id is '����id';
comment on column ZL_CONT_SALES.price_desc is '�۸����䣨�ַ���������ʽ���̶�����Ϊ��ʱ���滻���ۼ۸��ֶΣ�������ʾ���ֶ�';

