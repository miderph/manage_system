Ext.define("app.store.ColumnStore", {
    extend: "Ext.data.TreeStore",
    model: 'app.model.ColumnModel',
    autoDestroy: true,
    autoLoad: false,
    defaultRootId: 0,
    proxy: {
    	type: 'ajax',
    	url: 'column/query.do',
    	extraParams: {
    		c_pid: 0,
    		site_id: -1,
    		status: -10000
    	},
    	timeout:3000000
    },
    root: {
    	text: '栏目根',
    	expanded: true,
    	c_id: 0
    },
    listeners: {
    	beforeload: function(store, operation) {
    		store.proxy.extraParams.c_pid = operation.node.data.c_id;
    	}
    }
});