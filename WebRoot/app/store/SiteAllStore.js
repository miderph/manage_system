Ext.define("app.store.SiteAllStore", {
    extend: "Ext.data.Store",
    alias: 'store.site-all-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['s_id', 's_name'],
    proxy: {
    	type: 'ajax',
    	url: 'site/query_all.do',
    	reader: {
    		type: 'json',
    		root: 'data'
    	},
    	timeout:3000000
    }
});