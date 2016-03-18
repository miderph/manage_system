Ext.define("app.store.HotWordStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    autoLoad: false,
    model: 'app.model.HotWordModel',
    pageSize: 100,
    proxy: {
    	type: 'ajax',
    	url: 'hotword/query.do',
    	params:{
    		start:0,
    		limit:100
    	},
    	extraParams: {
    		site_id: '',
    		hotword: ''
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
    	property: 'hotword',
        direction:'ASC'
    }]
});