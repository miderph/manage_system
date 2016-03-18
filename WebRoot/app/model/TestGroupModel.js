Ext.define("app.model.TestGroupModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'ug_id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'raw_value', type: 'string'},
        {name: 'ids_value', type: 'string'},
        {name: 'intro', type: 'string'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});