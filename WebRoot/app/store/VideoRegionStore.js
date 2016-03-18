Ext.define("app.store.VideoRegionStore", {
    extend: "Ext.data.Store",
    alias: 'store.videoregion-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['regionId', 'regionName'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/region_cont_video.do',
    	reader: {
    		type: 'json',
    		root: 'region_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'regionId',
        direction:'ASC'
    }]
});