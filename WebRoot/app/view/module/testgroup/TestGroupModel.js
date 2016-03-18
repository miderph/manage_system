Ext.define('app.view.module.testgroup.TestGroupModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.testgroup',

    data: {
    	search_tg_type: '-10000',
    	search_tg_name: '',
    	
    	search_tg_type_id: ''
    },
    
    formulas: {
    	canSearch: function(get) {
			return get('search_tg_type') != '';
		}
    }
	
});