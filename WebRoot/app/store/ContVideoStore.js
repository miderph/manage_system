Ext.define("app.store.ContVideoStore", {
    extend: "app.store.AStore",
    model: 'app.model.ContVideoModel',
    autoDestroy: true,
    autoLoad: false,
    pageSize: 30,
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/query.do',
    	extraParams: {
    		provider_id: '',
    		c_type: '',
    		c_status: '',
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
        direction:'DESC'
    }]
});