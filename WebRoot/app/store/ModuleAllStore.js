Ext.define("app.store.ModuleAllStore", {
    extend: "Ext.data.Store",
    alias: 'store.module-all-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['m_id', 'm_name'],
    proxy: {
    	type: 'ajax',
    	url: 'module/query_all.do',
    	reader: {
    		type: 'json',
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'id',
        direction:'ASC'
    }]
});