Ext.define("app.store.GoodsOrderStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    model: 'app.model.GoodsOrderModel',
    pageSize: 10,
    proxy: {
    	type: 'ajax',
    	url: 'orders/query_cont.do?auth=1',
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
    	property: 'c_id',
        direction:'ASC'
    }]
});