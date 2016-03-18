Ext.define("app.store.TestGroupStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    model: 'app.model.TestGroupModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'usergroup/query.do',
    	extraParams: {
    		tg_type: '',
    		tg_name: ''
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
    	property: 'ug_id',
        direction:'ASC'
    }]
});