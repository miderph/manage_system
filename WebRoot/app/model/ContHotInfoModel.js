Ext.define("app.model.ContHotInfoModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'string'},
		{name: 'c_id', type: 'string'},
		{name: 'type', type: 'string'},
		{name: 'channel', type: 'string'},
		{name: 'hot_info', type: 'string'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'description', type: 'string'}

    ]
});