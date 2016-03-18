Ext.define("app.store.VideoFileDistinctStore", {
    extend: "Ext.data.Store",
    alias: 'store.videofile-distinct-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['contTypeId', 'contTypeName'],
    proxy: {
    	type: 'ajax',
    	url: 'videofiles/queryVideoDistinct.do',
    	reader: {
    		type: 'json',
    		root: 'contType_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'contTypeId',
        direction:'ASC'
    }],
    filters: [
        function(r) {
        	var ctid = r.raw.contTypeId;
        	if(ctid == 'cd' || ctid == -10000) {
        		return false;
        	} else {
        		return true;
        	}
        }
    ]
});