Ext.define("app.model.ShopModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 's_id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'credit', type: 'string'},
        {name: 'hot_info', type: 'string'},
        {name: 'icon_url', type: 'string'},
        {name: 'intro', type: 'string'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});