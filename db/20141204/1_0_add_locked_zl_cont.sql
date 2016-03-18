
    -- Add/modify columns 
alter table Zl_Cont add locked NUMBER(1);
-- Add comments to the columns 
comment on column Zl_Cont.locked
  is '1Ëø¶¨ 0Î´Ëø¶¨ ¿ÕÎ´Ëø¶¨';
