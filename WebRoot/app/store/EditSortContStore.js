Ext.define("app.store.EditSortContStore", {
    extend: "Ext.data.Store",
    alias: 'store.editsort-cont-store',
    autoDestroy: true,
    autoLoad: false,
    pageSize: 30,
    fields: [
             'mar_s_relaId',
             'mar_s_contId',
             'mar_s_contName',
             'mar_s_contType',
             'mar_s_contProvider',
             'mar_s_contIntro',
             'is_url_used',
             'cont_is_url_used',
             'cont_superscript',
             'is_url_used_id',
             'cont_is_url_used_id'],
    proxy: {
    	type: 'ajax',
    	url: 'relamenu/query_cont.do',
    	extraParams: {
    		menuId: -1
    	},
    	reader: {
    		type: 'json',
    		totalProperty : "results",
    		root: 'datastr',
			idProperty: 'mar_s_contId'
    	},
    	timeout:3000000
    },
    pruneModifiedRecords:true
/*,
    sorters: [{
    	property: 'mar_s_contId',
        direction:'ASC'
    }]*/
});