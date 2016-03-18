Ext.define("app.model.GoodsOrderModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'c_id', type: 'string'},
        {name: 'c_name', type: 'string'},
        {name: 'c_status', type: 'string'},
        {name: 'is_locked', type: 'string'},
        {name: 'provider_id', type: 'string'},
        {name: 'cs_sale_price', type: 'int'},
        {name: 'cs_sales_no', type: 'string'},
        {name: 'order_id', type: 'string'},	//订单号
        {name: 'order_price', type: 'string'},	//订单应付金额
        {name: 'channel_order_id', type: 'string'},	//渠道
        {name: 'order_pay_time', type: 'string'},	//订单支付时间
        {name: 'order_cont_amount', type: 'string'},	//商品数量
        {name: 'channel_name', type: 'string'},	//渠道名称
        {name: 'rewards', type: 'string'},	//积分
        {name: 'tiket_no', type: 'string'},	//优惠券编码
        {name: 'tiket_reward', type: 'string'}	//扣除礼金
    ]
});