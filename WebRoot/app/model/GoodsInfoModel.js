Ext.define("app.model.GoodsInfoModel", {
    extend: "Ext.data.Model",
    fields: [
		{name: 'c_id', type: 'string'},//商品ID
		{name: 'c_name', type: 'string'},//商品名称
        {name: 'cs_sales_no', type: 'string'},//商品编号
        {name: 'cs_sale_price', type: 'int'},//单价
        {name: 'order_cont_amount', type: 'string'}//商品数量
    ]
});