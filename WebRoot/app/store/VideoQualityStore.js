Ext.define("app.store.VideoQualityStore", {
    extend: "Ext.data.Store",
    alias: 'store.videoquality-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['qualityTypeId', 'qualityTypeName'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/quality_type_cont_video.do',
    	reader: {
    		type: 'json',
    		root: 'quality_type_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'qualityTypeId',
        direction:'ASC'
    }]
});