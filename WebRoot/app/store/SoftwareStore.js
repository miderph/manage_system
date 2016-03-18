Ext.define("app.store.SoftwareStore", {
    extend: "app.store.AStore",
    autoDestroy: true,
    model: 'app.model.SoftwareModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'software/query.do',
    	reader: {
    		type: 'json',
    		totalProperty : "num",
    		root: 'rows'
    	},
    	timeout:3000000
    }/*,
    sorters: [{
    	property: 'name',
        direction:'ASC'
    }]*/
});