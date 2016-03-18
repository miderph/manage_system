Ext.define("app.store.ContRuleStore", {
    extend: "Ext.data.Store",
    model: 'app.model.ContRuleModel',
    autoDestroy: true,
    autoLoad: false,
    pageSize: 30,
    proxy: {
    	type: 'ajax',
    	url: 'contrule/query.do',
    	extraParams: {
    		rule_ids: '-1'
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
    	property: 'rule_id',
        direction:'DESC'
    }]
});