Ext.define('app.view.module.menurelacont.Model', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.menurelacont',

    data: {
    	column_c_id: -1,
    	
    	column_site: -1,
    	column_site_comb: -1,
    	column_status: -10000,
    	edit_sort_c_id: -1,
    	
    	column_site_copy: -1,
    	column_status_copy: -10000,
    	column_cid_copy: -1,
    	
    	isLocalStore: Ext.create('app.store.ColumnIsLocalStore'),

    	column_base_info: 1,
    	column_img_info: 1,
    	
    	empty: 'app/resources/images/empty.JPG',
    	c_img_url: '',
		c_img_little_url: '',
		c_img_icon_url: '',
		c_img_4_squares_url: '',
		c_img_6_squares_url: '',
    },
    
    formulas: {
    	is_selected_column: function(get) {
			return get('column_c_id') >= 0;
		},
		is_selected_site: function(get) {
			return get('column_site') >= 0;
		},
		is_showing_site_0: function(get) {
			return get('column_site') == 0;
		},
    	canSearch: function(get) {
			return get('column_site_comb') >= 0;
		},
		canSearch_copy: function(get) {
			return get('column_site_copy') >= 0;
		},
		canSubmitColumn_copy: function(get) {
			return get('column_cid_copy') >= 0;
		},
		hasBaseInfo: function(get) {
			return get('column_base_info') == 1;
		},
		hasImgInfo: function(get) {
			return get('column_img_info') == 1;
		}
    }
	
});