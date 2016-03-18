Ext.define("app.store.VideoProgTypeStore", {
    extend: "Ext.data.Store",
    alias: 'store.videoprog-type-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['progTypeId', 'progTypeName'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/prog_type_cont_video.do',
    	reader: {
    		type: 'json',
    		root: 'prog_type_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'progTypeId',
        direction:'ASC'
    }]
});