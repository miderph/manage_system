Ext.define('app.view.module.contapp.ContAppModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.cont-app',

    data: {
    	
    	search_provider: '-10000',
    	search_type: '-10000',
    	search_status: '-10000',
    	search_name: '',
    	
    	cv_base_info: 1,
    	cv_img_info: 1,
    	
    	//empty: 'app/resources/images/empty.JPG',
    	c_img_url: '',
		c_img_little_url: '',
		c_img_icon_url: '',
		c_img_4_squares_url: '',
		c_img_6_squares_url: '',
    },
    
    formulas: {
    	canModify: function(get) {
			return get('c_id') >= 0;
		},
    	canSearch: function(get) {
			return get('search_provider') > -1;
		},
		hasBaseInfo: function(get) {
			return get('cv_base_info') == 1;
		},
		hasImgInfo: function(get) {
			return get('cv_img_info') == 1;
		},
		canModifyVF: function(get) {
			return get('vf_id') >= 0;
		}
    }
	
});