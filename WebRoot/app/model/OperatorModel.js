Ext.define("app.model.OperatorModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'o_id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'password', type: 'string'},
        {name: 'status', type: 'string'},
        {name: 'email', type: 'string'},
        {name: 'tel', type: 'string'},
        {name: 'intro', type: 'string'},
        {name: 'phone', type: 'string'},
        {name: 'role_ids', type: 'string'},
        {name: 'role_names', type: 'string'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});