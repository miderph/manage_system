-- Add/modify columns 
alter table ZL_IMG add url_source VARCHAR2(1024);
alter table ZL_IMG add url_little_source VARCHAR2(1024);
alter table ZL_IMG add url_icon_source VARCHAR2(1024);
-- Add comments to the columns 
comment on column ZL_IMG.url_source
  is '��ͼ/��������Դ';
comment on column ZL_IMG.url_little_source
  is '��ͼ����Դ';
comment on column ZL_IMG.url_icon_source
  is 'С��ͼ����Դ';