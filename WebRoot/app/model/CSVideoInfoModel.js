Ext.define("app.model.CSVideoInfoModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'vf_id', type: 'string'},
		{name: 'c_id', type: 'string'},
		{name: 'c_name', type: 'string'},
		{name: 'bit_rate', type: 'string'},
		{name: 'order_num', type: 'string'},
		{name: 'play_url', type: 'string'},
		{name: 'rate_tag', type: 'string'},
		{name: 'rate_tag_eng', type: 'string'},
		{name: 'title', type: 'string'},
		{name: 'provider_id', type: 'string'}
    ]
});