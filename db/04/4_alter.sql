-- Add/modify columns 
alter table ZL_APP_DOWNLOAD_LOG add access_type varchar2(100);
-- Add comments to the columns 
comment on column ZL_APP_DOWNLOAD_LOG.access_type
  is '访问类型';
-- Add/modify columns 
alter table ZL_APP_DOWNLOAD_LOG add menu_id varchar2(200);
-- Add comments to the columns 
comment on column ZL_APP_DOWNLOAD_LOG.menu_id
  is 'MENU_ID';

-- Add/modify columns 
alter table ZL_APP_DOWNLOAD_LOG add search_keyword VARCHAR2(200);
-- Add comments to the columns 
comment on column ZL_APP_DOWNLOAD_LOG.search_keyword
  is '搜索关键词';
