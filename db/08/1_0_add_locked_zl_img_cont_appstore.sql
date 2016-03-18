-- Add/modify columns 
alter table ZL_IMG add locked NUMBER(1);
-- Add comments to the columns 
comment on column ZL_IMG.locked
  is '1锁定 0未锁定 空未锁定';
  
  -- Add/modify columns 
alter table Zl_Cont_Appstore add locked NUMBER(1);
-- Add comments to the columns 
comment on column Zl_Cont_Appstore.locked
  is '1锁定 0未锁定 空未锁定';
