Ext.define("app.store.ShortcutConStore", {
    extend: "Ext.data.Store",
    alias: 'store.shortcut-con-store',
    autoDestroy: true,
    pageSize: 100,
    fields: [
             'mar_s_contId', 
             'mar_s_contName',
             'mar_s_contType', 
             'mar_s_contProvider',
             'mar_s_contIntro'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/query_cont_for_mar.do?auth=1',
    	extraParams: {
    		contType: '',
			contProvider: '',
			keyWord: '',
			contStatus: '-10000',
			startTime: '2010-01-01',
			endTime: '',
			menuId: '-1'
    	},
		actionMethods : {
			read : 'POST'
		},
    	reader: {
    		type: 'json',
    		totalProperty : "results",
    		root: 'datastr'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'mar_s_contId',
        direction:'ASC'
    }]
});