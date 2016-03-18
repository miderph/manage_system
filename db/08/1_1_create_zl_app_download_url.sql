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
  is '应用资产id';
comment on column ZL_APP_DOWNLOAD_URL.c_type
  is '应用的类型：1,应用软件  2，软件升级';
comment on column ZL_APP_DOWNLOAD_URL.package_name
  is '包名';
comment on column ZL_APP_DOWNLOAD_URL.version
  is '对应versionName';
comment on column ZL_APP_DOWNLOAD_URL.version_code
  is '对应versionCode';
comment on column ZL_APP_DOWNLOAD_URL.site
  is '网站域名';
comment on column ZL_APP_DOWNLOAD_URL.download_url
  is '下载地址';
comment on column ZL_APP_DOWNLOAD_URL.url_type
  is '升级地址的类型：0 自己服务器地址,1 普通地址,2 baidu云盘,3 360云盘';
comment on column ZL_APP_DOWNLOAD_URL.upgrade_temp_url
  is '升级临时地址（存放解析云盘后的实际下载地址）';
comment on column ZL_APP_DOWNLOAD_URL.temp_url_expire_time
  is '升级临时地址过期时间';
comment on column ZL_APP_DOWNLOAD_URL.share_password
  is '云盘地址分享密码';
