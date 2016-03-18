-- Create table
create table ZL_APP_DOWNLOAD_URL
(
  id                   NUMBER(20),
  c_id                 NUMBER(20),
  app_name             VARCHAR2(100),
  c_type               NUMBER(5),
  package_name         VARCHAR2(1024),
  provider_id          NUMBER(20),
  version              NUMBER(20),
  version_code         NUMBER(20),
  site                 VARCHAR2(1024),
  capacity             VARCHAR2(10),
  md5sum               VARCHAR2(50),
  add_time             DATE,
  create_time          DATE default SYSDATE,
  modify_time          DATE default SYSDATE,
  download_url         VARCHAR2(1024),
  url_type             NUMBER(5),
  upgrade_temp_url     VARCHAR2(1024),
  temp_url_expire_time DATE,
  share_password       VARCHAR2(100)
);
-- Add comments to the columns 
comment on column ZL_APP_DOWNLOAD_URL.id
  is 'id';
comment on column ZL_APP_DOWNLOAD_URL.c_id
  is 'Ӧ���ʲ�id';
comment on column ZL_APP_DOWNLOAD_URL.c_type
  is 'Ӧ�õ����ͣ�1,Ӧ�����  2���������';
comment on column ZL_APP_DOWNLOAD_URL.package_name
  is '����';
comment on column ZL_APP_DOWNLOAD_URL.version
  is '��ӦversionName';
comment on column ZL_APP_DOWNLOAD_URL.version_code
  is '��ӦversionCode';
comment on column ZL_APP_DOWNLOAD_URL.site
  is '��վ����';
comment on column ZL_APP_DOWNLOAD_URL.download_url
  is '���ص�ַ';
comment on column ZL_APP_DOWNLOAD_URL.url_type
  is '������ַ�����ͣ�0 �Լ���������ַ,1 ��ͨ��ַ,2 baidu����,3 360����';
comment on column ZL_APP_DOWNLOAD_URL.upgrade_temp_url
  is '������ʱ��ַ����Ž������̺��ʵ�����ص�ַ��';
comment on column ZL_APP_DOWNLOAD_URL.temp_url_expire_time
  is '������ʱ��ַ����ʱ��';
comment on column ZL_APP_DOWNLOAD_URL.share_password
  is '���̵�ַ��������';
