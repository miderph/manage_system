Ext.define("app.store.OperatorStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    model: 'app.model.OperatorModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'operator/query.do',
    	reader: {
    		type: 'json',
    		totalProperty : "total",
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'o_id',
        direction:'ASC'
    }]
});