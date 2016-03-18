-- Add/modify columns 
alter table ZL_CONT_VIDEO add link_url varchar2(200);
-- Add comments to the columns 
comment on column ZL_CONT_VIDEO.link_url
  is 'æ≤Ã¨¡¥Ω”µÿ÷∑';
