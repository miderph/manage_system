Ext.define("app.store.ColumnIsLocalStore", {
    extend: "Ext.data.Store",
    alias: 'store.column-islocal-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['islocalId', 'islocalName'],
    proxy: {
    	type: 'ajax',
    	url: 'menus/islocal_menu.do',
    	reader: {
    		type: 'json',
			totalProperty : "results",
    		root: 'islocal_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'islocalId',
        direction:'ASC'
    }]
});