Ext.define("app.model.HotWordModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'hw_id', type: 'string'},
        {name: 'hotword', type: 'string'},
        {name: 'site_id', type: 'int'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});