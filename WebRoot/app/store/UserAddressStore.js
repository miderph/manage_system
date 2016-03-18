Ext.define("app.store.UserAddressStore", {
    extend: "Ext.data.Store",
    model: 'app.model.UserAddressModel',
    autoDestroy: true,
    autoLoad: false,
    pageSize: 3,
    proxy: {
    	type: 'ajax',
    	url: 'orders/query_addr.do?auth=1',
    	extraParams: {
    		c_name: ''
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
    	property: 'a_id',
        direction:'ASC'
    }]
});