-- Add/modify columns 
alter table ZL_CONT add pinyin varchar2(255);
-- Add comments to the columns 
comment on column ZL_CONT.pinyin
  is 'ƴ������ĸ';

--��ʼ������
update zl_cont set pinyin=upper(F_TRANS_PINYIN_CAPITAL(name)) where pinyin is null and name is not null;
commit;

--��������

