create or replace view v_getsalesdetail_client as
select
    ct.ID c_id, ct.name, ct.description, ct.provider_id
   , sales.HOT_INFO,FAKE_PRICE,SALE_PRICE,REAL_PRICE
   ,DISACCOUNT,KEY_WORDS,CP_NAME,PAY_TYPE_IDS,SALES_NO,SUB_CP_NAME,POST_PRICE
   ,detail_Pic_File
from zl_cont ct
     join ZL_CONT_SALES sales on (ct.status >= 1 and sysdate between ct.active_time and ct.deactive_time and sales.c_id = ct.id );