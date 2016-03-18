Ext.define("app.model.StbPrefixModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'sp_id', type: 'string'},
        {name: 'code', type: 'string'},
        {name: 'provider_id', type: 'string'},
        {name: 'provider_name', type: 'string'},
        {name: 'site_id', type: 'string'},
        {name: 'site_name', type: 'string'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});