alter table ZL_CONT_SALES add sum_stock NUMBER(20);
comment on column ZL_CONT_SALES.sum_stock is '�������';

alter table zl_cont_sales_import add  bitmask_price varchar2(2) ; 
comment on column zl_cont_sales_import.bitmask_price is '���¸ļ۱�ʶ';

alter table zl_cont_sales add  bitmask_price varchar2(2) ; 
comment on column zl_cont_sales.bitmask_price is '���¸ļ۱�ʶ';

alter table ZL_CONT_SALES modify HOT_INFO VARCHAR2(4000);
alter table ZL_CONT_SALES_IMPORT modify HOT_INFO VARCHAR2(4000);

alter table ZL_CONT_SALES_IMPORT add   PIC_URL1         VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   PIC_URL2         VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   PIC_URL3         VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   PIC_URL4         VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   PIC_URL5         VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   PIC_URL6         VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   ISP_PIC_URL1     VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   ISP_PIC_URL2     VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   ISP_PIC_URL3     VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   ISP_PIC_URL4     VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   ISP_PIC_URL5     VARCHAR2(255);
alter table ZL_CONT_SALES_IMPORT add   ISP_PIC_URL6     VARCHAR2(255);
comment on column ZL_CONT_SALES_IMPORT.PIC_URL1         is '����ͼ';
comment on column ZL_CONT_SALES_IMPORT.PIC_URL2         is '����ͼ';
comment on column ZL_CONT_SALES_IMPORT.PIC_URL3         is '����ͼ';
comment on column ZL_CONT_SALES_IMPORT.PIC_URL4         is '����ͼ';
comment on column ZL_CONT_SALES_IMPORT.PIC_URL5         is '����ͼ';
comment on column ZL_CONT_SALES_IMPORT.PIC_URL6         is '����ͼ';
comment on column ZL_CONT_SALES_IMPORT.ISP_PIC_URL1     is 'ISPԭʼ����ͼ';
comment on column ZL_CONT_SALES_IMPORT.ISP_PIC_URL2     is 'ISPԭʼ����ͼ';
comment on column ZL_CONT_SALES_IMPORT.ISP_PIC_URL3     is 'ISPԭʼ����ͼ';
comment on column ZL_CONT_SALES_IMPORT.ISP_PIC_URL4     is 'ISPԭʼ����ͼ';
comment on column ZL_CONT_SALES_IMPORT.ISP_PIC_URL5     is 'ISPԭʼ����ͼ';
comment on column ZL_CONT_SALES_IMPORT.ISP_PIC_URL6     is 'ISPԭʼ����ͼ';

alter table ZL_CONT_SALES_IMPORT add   sum_stock        NUMBER;
comment on column ZL_CONT_SALES_IMPORT.sum_stock     is '���';
alter table ZL_CONT_SALES_IMPORT add   SUM_SALE        NUMBER;
comment on column ZL_CONT_SALES_IMPORT.SUM_SALE     is '����';

update ZL_CONT_SALES_IMPORT set modify_time=create_time where modify_time is null;