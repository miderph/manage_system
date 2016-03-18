Ext.define("app.model.ContRuleModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'rule_id', type: 'string'},
		{name: 'name', type: 'string'},
		{name: 'price', type: 'string'},
		{name: 'price_rela', type: 'string'},
		{name: 'price_right', type: 'string'},
		{name: 'provider_ids', type: 'string'},
		{name: 'provider_rela', type: 'string'},
		{name: 'shop_ids', type: 'string'},
		{name: 'shop_rela', type: 'string'},
		{name: 'category', type: 'string'},
		{name: 'category_rela', type: 'string'},
		{name: 'category_new_menu', type: 'string'}
    ]
});