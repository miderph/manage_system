Ext.define("app.model.ContAppDownloadUrlModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'string'},
		{name: 'c_id', type: 'string'},
		{name: 'app_name', type: 'int'},
		{name: 'package_name', type: 'string'},
		{name: 'provider_id', type: 'int'},
		{name: 'version', type: 'string'},
		{name: 'version_code', type: 'int'},
		{name: 'site', type: 'string'},
		{name: 'capacity', type: 'string'},
		{name: 'md5sum', type: 'string'},
		
		{name: 'download_url', type: 'string'},
		{name: 'url_type', type: 'string'},
		{name: 'url_type_desc', type: 'string'},
		{name: 'upgrade_temp_url', type: 'string'},
		{name: 'share_password', type: 'string'},
		
		{name: 'temp_url_exepire_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'add_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});