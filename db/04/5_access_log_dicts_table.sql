-- Create table
create table zl_ACCESS_LOG_DICTS
(
  key   VARCHAR2(200),
  value VARCHAR2(200),
  type  NUMBER(2)
);
-- Add comments to the columns 
comment on column zl_ACCESS_LOG_DICTS.key
  is '关键词';
comment on column zl_ACCESS_LOG_DICTS.value
  is '取值';
comment on column zl_ACCESS_LOG_DICTS.type
  is '类型 0： prefix前缀枚举  1：access_type枚举';
