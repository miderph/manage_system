Ext.define("app.store.VideoAdTypeStore", {
    extend: "Ext.data.Store",
    alias: 'store.vide-adtype-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['contAdTypeId', 'contAdTypeName'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/cont_adtype.do',
    	reader: {
    		type: 'json',
    		root: 'contAdType_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'contAdTypeId',
        direction:'ASC'
    }]
});