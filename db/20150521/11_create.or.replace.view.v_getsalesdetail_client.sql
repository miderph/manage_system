create or replace view v_getsalesdetail_client as
select
    ct.ID c_id, ct.name, ct.description, ct.provider_id
   , sales.HOT_INFO,sales.FAKE_PRICE,sales.SALE_PRICE,sales.REAL_PRICE,sales.bitmask_price
   ,sales.DISACCOUNT,sales.KEY_WORDS,sales.CP_NAME,sales.PAY_TYPE_IDS,sales.SALES_NO,sales.SUB_CP_NAME,sales.POST_PRICE,sales.price_desc
   ,sales.detail_Pic_File,sales.gift,sales.sum_sale,sales.sum_stock,sales.post_desc,sales.service_desc,sales.shop_id,sales.channel_id,sales.url link_url
   ,ct.video_seg_time
from zl_cont ct
     join ZL_CONT_SALES sales on (ct.status >= 1 and sysdate between ct.active_time and ct.deactive_time and sales.c_id = ct.id )
;