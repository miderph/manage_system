-- Drop columns 
alter table ZL_CONT_VIDEO drop column link_url;
alter table ZL_CONT_VIDEO add superscript_id number(20);
-- Add comments to the columns 
comment on column ZL_CONT_VIDEO.superscript_id
  is '…œΩ«±Íid';
