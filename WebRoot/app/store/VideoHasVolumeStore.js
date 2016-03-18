Ext.define("app.store.VideoHasVolumeStore", {
    extend: "Ext.data.Store",
    alias: 'store.video-hasvolume-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['hasVolumeId', 'hasVolumeName'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/has_volume_cont_video.do',
    	reader: {
    		type: 'json',
    		root: 'has_volume_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'hasVolumeId',
        direction:'ASC'
    }]
});