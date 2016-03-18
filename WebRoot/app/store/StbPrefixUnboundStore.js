Ext.define("app.store.StbPrefixUnboundStore", {
    extend: "Ext.data.Store",
    alias: 'store.stbprefix-unbound-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['sp_id', 'sp_code'],
    proxy: {
    	type: 'ajax',
    	url: 'stbprefixe/query_unbound.do',
    	reader: {
    		type: 'json',
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'sp_code',
        direction:'ASC'
    }]
});