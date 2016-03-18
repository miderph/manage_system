alter table ZL_CONT_SALES modify URL VARCHAR2(1024);

alter table ZL_CONT_SALES add gift VARCHAR2(1024);
--SUM_STOCK 库存余量
alter table ZL_CONT_SALES add SUM_SALE VARCHAR2(1024);
alter table ZL_CONT_SALES add post_desc VARCHAR2(1024);
alter table ZL_CONT_SALES add service_desc VARCHAR2(1024);
comment on column ZL_CONT_SALES.gift is '赠品';
comment on column ZL_CONT_SALES.SUM_SALE is '销量';
comment on column ZL_CONT_SALES.post_desc is '运费描述';
comment on column ZL_CONT_SALES.service_desc is '服务描述';

alter table ZL_CONT_SALES add shop_id number(20);
alter table ZL_CONT_SALES add channel_id number(20);
alter table ZL_CONT_SALES add price_desc VARCHAR2(100);
comment on column ZL_CONT_SALES.shop_id is '商铺id';
comment on column ZL_CONT_SALES.channel_id is '渠道id';
comment on column ZL_CONT_SALES.price_desc is '价格区间（字符串），格式不固定，不为空时，替换销售价格字段，优先显示此字段';

