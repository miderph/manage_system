Ext.define("app.store.ChannelStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
	autoLoad: true,
    model: 'app.model.ChannelModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'contchannel/query.do',
    	reader: {
    		type: 'json',
    		totalProperty : "total",
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 's_id',
        direction:'ASC'
    }]
});