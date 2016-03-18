Ext.define('app.view.module.contsales.ContSalesModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.cont-sales',

    data: {
    	c_id: -1,
    	vf_id: -1,
    	
    	search_provider: -10000,
    	search_status: -10000,
    	search_name: '',
    	search_sales_no: '',
    	
    	cs_base_info: 1,
    	cs_img_info: 1,

    	//empty: 'app/resources/images/empty.JPG',
    	c_img_url: '',
		c_img_little_url: '',
		c_img_icon_url: '',
		c_img_4_squares_url: '',
		c_img_6_squares_url: ''
    },
    
    formulas: {
    	canModify: function(get) {
			return get('c_id') >= 0;
		},
    	canSearch: function(get) {
			return true;//get('search_provider') > -1;
		},
		hasBaseInfo: function(get) {
			return get('cs_base_info') == 1;
		},
		hasImgInfo: function(get) {
			return get('cs_img_info') == 1;
		},
		canModifyVF: function(get) {
			return get('vf_id') >= 0;
		}
    }
	
});