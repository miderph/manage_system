Ext.define("app.store.VideoContTypeStore", {
    extend: "Ext.data.Store",
    alias: 'store.videocont-type-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['conttypeId', 'conttypeName'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/conttype_cont_video.do',
    	reader: {
    		type: 'json',
    		root: 'conttype_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'conttypeId',
        direction:'ASC'
    }]
});