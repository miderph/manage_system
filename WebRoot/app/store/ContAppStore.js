Ext.define("app.store.ContAppStore", {
    extend: "app.store.AStore",
    model: 'app.model.ContAppModel',
    autoDestroy: true,
    autoLoad: false,
    pageSize: 20,
    proxy: {
    	type: 'ajax',
		  url : 'apps/query.do',
    	extraParams: {
    		provider_id: '',
    		c_status: '',
    		c_name: ''
    	},
		actionMethods : {
			read : 'POST'
		},
		reader : {
			totalProperty : "total",
			root : "data",
			idProperty : 'c_id'
		},
    	timeout:3000000
    },
    sorters: [{
    	property: 'contID',
        direction:'DESC'
    }]
});