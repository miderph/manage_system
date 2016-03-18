Ext.define("app.store.ContSalesPayTypeStore", {
    extend: "Ext.data.Store",
    alias: 'store.contsales-paytype-store',
    autoDestroy: true,
	autoLoad: true,
		fields: ['id','name','pay_type','pay_type_name','has_qrcode','has_qrcode_name','service_hotline','description'],
    proxy: {
    	type: 'ajax',
			url: 'paytypes/query.do',
			reader: {
				totalProperty: "results",
    			root: "datastr"
			},
    	timeout:3000000
    },
    sorters: [{
    	property: 'id',
        direction:'ASC'
    }]
});