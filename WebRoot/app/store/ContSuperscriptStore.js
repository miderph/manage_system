Ext.define("app.store.ContSuperscriptStore", {
    extend: "Ext.data.Store",
    alias: 'store.cont-superscript-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['mar_s_contId', 'mar_s_contName', 'mar_s_contType', 'mar_s_contProvider', 'mar_s_contIntro'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/query_superscript.do',
    	reader: {
    		type: 'json',
    		root: 'datastr'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'mar_s_contId',
        direction:'ASC'
    }]
});