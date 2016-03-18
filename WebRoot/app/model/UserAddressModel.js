Ext.define("app.model.UserAddressModel", {
    extend: "Ext.data.Model",
    fields: [
		{name: 'a_id', type: 'string'}, //用户地址ID
		{name: 'user_name', type: 'string'}, //用户姓名
		{name: 'province_id', type: 'string'}, //省份ID
		{name: 'downtown_id', type: 'string'}, //市区ID
		{name: 'county_id', type: 'string'}, //县级ID
		{name: 'township_id', type: 'string'}, //乡镇ID
        {name: 'contact_addr', type: 'string'}, //用户地址
        {name: 'mobile_no', type: 'int'}, //用户本机号码
        {name: 'contact_no', type: 'string'}, //联系号码一
        {name: 'contact_no_other', type: 'string'}, //联系号码二
        {name: 'memo', type: 'string'}//备注
    ]
});