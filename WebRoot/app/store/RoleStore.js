Ext.define("app.store.RoleStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    model: 'app.model.RoleModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'role/query.do',
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
    	property: 'name',
        direction:'ASC'
    }]
});