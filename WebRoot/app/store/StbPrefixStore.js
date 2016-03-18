Ext.define("app.store.StbPrefixStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    model: 'app.model.StbPrefixModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'stbprefixe/query.do',
    	reader: {
    		type: 'json',
    		totalProperty : "total",
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'sp_id',
        direction:'ASC'
    }]
});