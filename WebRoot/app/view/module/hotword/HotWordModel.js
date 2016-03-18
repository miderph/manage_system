Ext.define('app.view.module.hotword.HotWordModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.hotword',

    data: {
    	search_hotword: '',
    	search_site: '-10000',
    	
    	hw_site_id: '-1'
    },
    
    formulas: {
    	is_selected_site_0: function(get) {
			return get('hw_site_id') == 0;
		},
    	is_selected_site: function(get) {
			return get('hw_site_id') >= 0;
		},
    	can_rela: function(get) {
			return get('hw_site_id') > 0;
		},
    	can_delete_rela: function(get) {
			return get('hw_site_id') != 0;
		},
    	canSearch: function(get) {
			return true;//get('search_site') >= -1;
		}
    }
	
});