-- Add/modify columns 
alter table ZL_RELA_MENU_CONT add is_url_used NUMBER(5);
-- Add comments to the columns 
comment on column ZL_RELA_MENU_CONT.is_url_used
  is '0ʹ��url_little,��ͼ 1 ʹ��url��2 ʹ��url_icon��3 ʹ�� URL_4_SQUARES��4ʹ�� URL_6_SQUARES';
