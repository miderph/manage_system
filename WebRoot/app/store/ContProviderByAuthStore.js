Ext.define("app.store.ContProviderByAuthStore", {
    extend: "Ext.data.Store",
    alias: 'store.contprovider-byauth-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['cp_id', 'cp_name'],
    proxy: {
    	type: 'ajax',
    	url: 'contprovider/query_with_auth.do?is_combo=1',
    	reader: {
    		type: 'json',
    		root: 'data'
    	},
    	timeout:3000000
    }
});