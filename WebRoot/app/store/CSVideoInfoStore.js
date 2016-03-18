Ext.define("app.store.CSVideoInfoStore", {
    extend: "Ext.data.Store",
    model: 'app.model.CSVideoInfoModel',
    autoDestroy: true,
    autoLoad: false,
    pageSize: 30,
    proxy: {
    	type: 'ajax',
    	url: 'videofiles/query.do',
    	extraParams: {
    		c_id: -1,
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
    	property: 'vf_id',
        direction:'DESC'
    }]
});