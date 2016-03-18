Ext.define('app.view.module.contcheck.ContCheckModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.cont-check',

    data: {
    	c_id: -1,
    	vf_id: -1,
    	
    	search_provider: -10000,
    	search_status: -10000,
    	search_name: '',
    	
    },
    
    formulas: {
    	canModify: function(get) {
			return get('c_id') >= 0;
		},
    	canSearch: function(get) {
			return true;//get('search_provider') > -1;
		},
    }
	
});