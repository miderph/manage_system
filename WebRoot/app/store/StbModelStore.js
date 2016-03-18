Ext.define("app.store.StbModelStore", {
    extend: "Ext.data.Store",
    alias: 'store.stbmodel-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['sm_id', 'sm_model'],
    proxy: {
    	type: 'ajax',
    	url: 'stbmodel/query.do',
    	reader: {
    		type: 'json',
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'sm_id',
        direction:'ASC'
    }]
});