Ext.define("app.store.SiteStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    model: 'app.model.SiteModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'site/query.do',
    	reader: {
    		type: 'json',
    		totalProperty : "total",
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 's_id',
        direction:'ASC'
    }]
});