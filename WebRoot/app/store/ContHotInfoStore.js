Ext.define("app.store.ContHotInfoStore", {
    extend: "Ext.data.Store",
    model: 'app.model.ContHotInfoModel',
    //fields: ['id','c_id','type','channel','hot_info','create_time','modify_time','description'],
    autoDestroy: true,
    autoLoad: false,
    proxy: {
    	type: 'ajax',
    	url: 'hotinfos/query_hotinfo.do',
    	extraParams: {
    		c_id: '',
    		c_name: ''
    	},
		actionMethods : {
			read : 'POST'
		},
    	reader: {
			totalProperty : "total",
    		type: 'json',
    		root: 'data',
			idProperty : 'id'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'channel',
        direction:'ASC'
    }],
    filters: [
        function(r) {
        	/*var ctid = r.raw.contTypeId;
        	if(ctid == 'cd' || ctid == -10000) {
        		return false;
        	} else {
        		return true;
        	}*/
        	return true;
        }
    ]
});