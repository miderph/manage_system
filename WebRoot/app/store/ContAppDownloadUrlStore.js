Ext.define("app.store.ContAppDownloadUrlStore", {
    extend: "Ext.data.Store",
    model: 'app.model.ContAppDownloadUrlModel',
    autoDestroy: true,
    autoLoad: false,
    pageSize: 20,
					fields : ['id', 'c_id', 'app_name', 'package_name',
							'provider_id', 'version', 'version_code', 'site',
							'capacity', 'md5sum', 'add_time', 'create_time',
							'modify_time', 'download_url', 'url_type',
							'url_type_desc', 'upgrade_temp_url',
							'temp_url_exepire_time', 'share_password'],
    proxy: {
    	type: 'ajax',
			url : 'apps/query_app_download_url.do',
    	extraParams: {
							c_id : 0
    	},
		actionMethods : {
			read : 'POST'
		},
		reader : {
			totalProperty : "results",
			root : "datastr",
			idProperty : 'id'
		},
    	timeout:3000000
    },
    sorters: [{
    	property: 'id',
        direction:'DESC'
    }]
});