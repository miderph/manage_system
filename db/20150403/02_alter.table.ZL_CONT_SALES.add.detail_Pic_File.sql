alter table ZL_CONT_SALES_IMPORT add detail_Pic_File VARCHAR2(255);
comment on column ZL_CONT_SALES_IMPORT.detail_Pic_File is '����ͼƬ�б���ļ�';

alter table ZL_CONT_SALES add detail_Pic_File VARCHAR2(255);
comment on column ZL_CONT_SALES.detail_Pic_File is '����ͼƬ�б���ļ�';
