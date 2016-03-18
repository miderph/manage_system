Ext.define("app.store.TestGroupByZoneStore", {
    extend: "Ext.data.Store",
    alias: 'store.testgroup-byzone-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['ug_id', 'ug_name'],
    proxy: {
    	type: 'ajax',
    	url: 'usergroup/query_all.do?type=zone',
    	reader: {
    		type: 'json',
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'a_id',
        direction:'ASC'
    }]
});