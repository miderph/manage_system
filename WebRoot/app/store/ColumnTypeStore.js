Ext.define("app.store.ColumnTypeStore", {
    extend: "Ext.data.Store",
    alias: 'store.column-type-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['s_id', 's_name'],
    proxy: {
    	type: 'ajax',
    	url: 'status/query_for_column_structtype.do',
    	reader: {
    		type: 'json',
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 's_id',
        direction:'ASC'
    }]
});