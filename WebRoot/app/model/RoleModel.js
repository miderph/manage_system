Ext.define("app.model.RoleModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'site_ids', type: 'string'},
        {name: 'site_names', type: 'string'},
        {name: 'provider_ids', type: 'string'},
        {name: 'provider_names', type: 'string'},
        {name: 'module_ids', type: 'string'},
        {name: 'module_names', type: 'string'},
        {name: 'menu_ids', type: 'string'},
        {name: 'menu_names', type: 'string'},
        {name: 'group_ids', type: 'string'},
        {name: 'group_names', type: 'string'},
        {name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
        {name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
    ]
});