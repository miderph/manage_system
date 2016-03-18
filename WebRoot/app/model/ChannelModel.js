Ext.define("app.model.ChannelModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 's_id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'url_template', type: 'string'},
        {name: 'intro', type: 'string'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});