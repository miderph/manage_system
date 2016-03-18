Ext.define("app.store.MenuRelaContStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    autoLoad: false,
      	fields: ['mar_s_relaId','mar_s_contId','mar_s_status','mar_s_contName','mar_s_contType','mar_s_contProvider','mar_s_date_time',
      	    'mar_s_active_time','mar_s_deactive_time','mar_s_create_time','mar_s_modify_time',
			'mar_s_contIntro','locked','is_url_used','cont_is_url_used','cont_superscript','is_url_used_id','cont_is_url_used_id'],
    proxy: {
    	type: 'ajax',
			url: 'relamenu/query_cont.do',
		extraParams: {
			menuId: -1
		},
    	timeout:3000000,
			reader: {
				totalProperty: "results",
				root: "datastr",
				idProperty: 'mar_s_contId'
			}
    },
  		pruneModifiedRecords:true,
    listeners: {
				 load: function (ostore, records, successful, eOpts) 
				 {
                    checkSelected();
				 }
    }
});