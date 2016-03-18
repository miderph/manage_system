-- Add/modify columns 
alter table ZL_IMG add url_4_squares VARCHAR2(1024);
alter table ZL_IMG add url_6_squares VARCHAR2(1024);
-- Add comments to the columns 
comment on column ZL_IMG.url_4_squares
  is '4方格图';
comment on column ZL_IMG.url_6_squares
  is '6方格图';
