Ext.define("app.store.ShopStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
	autoLoad: true,
    model: 'app.model.ShopModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'contshop/query.do',
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