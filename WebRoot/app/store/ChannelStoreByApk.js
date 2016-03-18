Ext.define("app.store.ChannelStoreByApk", {
    extend: "Ext.data.Store",
    autoDestroy: true,
	autoLoad: true,
    model: 'app.model.ChannelModel',
    pageSize: 20,
    proxy: {
    	type: 'ajax',
    	url: 'contchannel/query_all.do?type=apk',
    	reader: {
    		type: 'json',
    		//totalProperty : "total",
    		root: 'data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'channel',
        direction:'ASC'
    }]
});