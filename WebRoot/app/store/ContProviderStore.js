Ext.define("app.store.ContProviderStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    model: 'app.model.ContProviderModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'contprovider/query.do',
    	extraParams: {
    		c_name: ''
    	},
    	reader: {
    		type: 'json',
    		totalProperty : "total",
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'cp_id',
        direction:'ASC'
    }]
});