-- Add/modify columns 
alter table ZL_SOFTWARE_VERSION add share_password VARCHAR2(50);
-- Add comments to the columns 
comment on column ZL_SOFTWARE_VERSION.share_password
  is 'ÔÆÅÌµØÖ··ÖÏíÃÜÂë';
