Ext.define("app.model.ContSalesModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'c_id', type: 'string'},
		{name: 'c_name', type: 'string'},
		{name: 'c_type', type: 'string'},
		{name: 'ad_type', type: 'string'},
		{name: 'pinyin', type: 'string'},
		{name: 'c_status', type: 'string'},
		{name: 'is_locked', type: 'string'},
		{name: 'provider_id', type: 'string'},
		
		{name: 'cs_hot_info', type: 'string'},
		{name: 'cs_fake_price', type: 'string'},
		{name: 'cs_sale_price', type: 'string'},
		{name: 'cs_real_price', type: 'string'},
		{name: 'cs_post_price', type: 'string'},
		{name: 'cs_disaccount', type: 'string'},
		{name: 'cs_cp_name', type: 'string'},
		{name: 'cs_sub_cp_name', type: 'string'},
		{name: 'cs_sales_no', type: 'string'},
		{name: 'cs_sum_stock', type: 'string'},
		{name: 'cs_gift', type: 'string'},
		{name: 'cs_sum_sale', type: 'string'},
		{name: 'cs_post_desc', type: 'string'},
		{name: 'cs_service_desc', type: 'string'},
		{name: 'cs_key_words', type: 'string'},
		{name: 'cs_pay_type_ids', type: 'string'},
		{name: 'cs_pay_type_names', type: 'string'},
		
		{name: 'cv_alias', type: 'string'},
		{name: 'superscript_id', type: 'string'},
		{name: 'cv_play_url', type: 'string'},
		
		{name: 'active_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'deactive_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});