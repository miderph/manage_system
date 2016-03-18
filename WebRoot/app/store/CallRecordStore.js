Ext.define("app.store.CallRecordStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    model: 'app.model.CallRecordModel',
    pageSize: 3,
    proxy: {
    	type: 'ajax',
    	url: 'orders/query_call.do?auth=1',
    	extraParams: {
    		c_num: ''
    	},
		actionMethods : {
			read : 'POST'
		},
    	reader: {
    		type: 'json',
    		totalProperty : "total",
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'c_id',
        direction:'ASC'
    }]
});