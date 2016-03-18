-- Add/modify columns 
alter table ZL_IMG add url_source VARCHAR2(1024);
alter table ZL_IMG add url_little_source VARCHAR2(1024);
alter table ZL_IMG add url_icon_source VARCHAR2(1024);
-- Add comments to the columns 
comment on column ZL_IMG.url_source
  is '横图/截屏下载源';
comment on column ZL_IMG.url_little_source
  is '竖图下载源';
comment on column ZL_IMG.url_icon_source
  is '小方图下载源';