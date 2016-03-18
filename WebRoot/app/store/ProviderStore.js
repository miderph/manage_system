Ext.define("app.store.ProviderStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
	autoLoad: true,
    fields: ['providerId', 'providerName'],
    proxy: {
        type: 'ajax',
        url: 'menus/provider_menu.do?siteId=-1&auth=1',
        reader: {
        	type: 'json',
    		root: 'provider_data'
        },
    	timeout:3000000
    },
    sorters: [{
    	property: 'status_id',
        direction:'ASC'
    }]
});