Ext.define("app.store.GoodsInfoStore", {
    extend: "Ext.data.Store",
    model: 'app.model.GoodsInfoModel',
    autoDestroy: true,
    autoLoad: false,
    pageSize: 30,
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