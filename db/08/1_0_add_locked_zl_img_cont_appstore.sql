-- Add/modify columns 
alter table ZL_IMG add locked NUMBER(1);
-- Add comments to the columns 
comment on column ZL_IMG.locked
  is '1���� 0δ���� ��δ����';
  
  -- Add/modify columns 
alter table Zl_Cont_Appstore add locked NUMBER(1);
-- Add comments to the columns 
comment on column Zl_Cont_Appstore.locked
  is '1���� 0δ���� ��δ����';
