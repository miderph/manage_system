-- Add/modify columns 
alter table ZL_CONT_SALES add HAS_VIDEO number(1);
alter table ZL_CONT_SALES add url varchar2(4000);
alter table ZL_CONT_SALES add manage_user VARCHAR2(255);
alter table ZL_CONT_SALES add audit_review VARCHAR2(500);
alter table ZL_CONT_SALES add shop_type VARCHAR2(2);
-- Add comments to the columns 
comment on column ZL_CONT_SALES.url
  is '�������ת��ַ';
comment on column ZL_CONT_SALES.icon_url3
  is '�Ƽ�ͼ��ͼ';
comment on column ZL_CONT_SALES.isp_icon_url3
  is 'ISPԭʼ�Ƽ�ͼ��ͼ';
comment on column ZL_CONT_SALES.manage_user
  is '������';
comment on column ZL_CONT_SALES.audit_review
  is 'С������';
comment on column ZL_CONT_SALES.shop_type
  is '��������';
-- Add comments to the columns
alter table ZL_CONT_SALES add start_time DATE;
alter table ZL_CONT_SALES add end_time DATE;
comment on column ZL_CONT_SALES.start_time
  is '��ʼʱ��';
comment on column ZL_CONT_SALES.end_time
  is '����ʱ��';