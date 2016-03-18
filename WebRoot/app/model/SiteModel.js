Ext.define("app.model.SiteModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 's_id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'status', type: 'int'},
        {name: 'intro', type: 'string'},
		{name: 'active_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'deactive_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});