Ext.define("app.store.TestGroupAllStore", {
    extend: "Ext.data.Store",
    alias: 'store.testgroup-all-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['ug_id', 'ug_name'],
    proxy: {
    	type: 'ajax',
    	url: 'usergroup/query_all.do',
    	reader: {
    		type: 'json',
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'ug_name',
        direction:'ASC'
    }]
});