-- Create table
create table zl_ACCESS_LOG_DICTS
(
  key   VARCHAR2(200),
  value VARCHAR2(200),
  type  NUMBER(2)
);
-- Add comments to the columns 
comment on column zl_ACCESS_LOG_DICTS.key
  is '�ؼ���';
comment on column zl_ACCESS_LOG_DICTS.value
  is 'ȡֵ';
comment on column zl_ACCESS_LOG_DICTS.type
  is '���� 0�� prefixǰ׺ö��  1��access_typeö��';
