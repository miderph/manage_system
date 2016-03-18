Ext.define("app.model.SoftwareModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'string'},
        {name: 'version_number', type: 'string'},
        {name: 'software_info', type: 'string'},
        {name: 'add_plat', type: 'string'},
        {name: 'enforce_flag', type: 'string'},
        {name: 'publish_time', type: 'string'},
        {name: 'description', type: 'string'},
        {name: 'usergroup_ids_mac', type: 'string'},
        {name: 'usergroup_names_mac', type: 'string'},
        {name: 'usergroup_ids_zone', type: 'string'},
        {name: 'usergroup_names_zone', type: 'string'},
        {name: 'usergroup_ids_model', type: 'string'},
        {name: 'usergroup_names_model', type: 'string'},
        {name: 'file_type', type: 'string'},
        {name: 'status', type: 'string'},
        {name: 'statusname', type: 'string'},
        {name: 'md5', type: 'string'},
        {name: 'url_general_id', type: 'string'},
        {name: 'update_url_general', type: 'string'},
        {name: 'url_360_id', type: 'string'},
        {name: 'update_url_360', type: 'string'},
        {name: 'share_password_360', type: 'string'},
        {name: 'upgrate_temp_url', type: 'string'},
        {name: 'temp_url_expire_time', type: 'string'}
    ]
});