Ext.define('app.view.module.customer.CustomerController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.customer',
    
    onModifyCallRecord: function(e, c, eOpts) {
    	if(Ext.String.trim(c.originalValue) !== Ext.String.trim(c.record.data.memo)) {
    		console.log(c.record.data);
    	}
    	
    	c.record.commit();
    },
    
    onModifyUserAddress: function(e, c, eOpts) {
    	console.log(e);
    	console.log(c);
    	/*if(Ext.String.trim(c.originalValue) !== Ext.String.trim(c.record.data.memo)) {
    		console.log(c.record.data);
    	}*/
    	
    	c.record.commit();
    },
    
    onAddUserAddressBtn: function(btn, e) {
    	var grid = this.lookupReference('user_address_grid');
    	var store = grid.getStore();
    	var model = store.getModel();
    	var m = new model({
    		a_id: '-1',
    		user_name: '刘海迪',
    		province_id: 'bjs'
    	});
    	store.insert(0, m);
    },
    
    onCustomerNumChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_c_num', Ext.String.trim(newValue));
    },
    
    onCustomerNumEnter: function(field, e) {
    	if (e.getKey() == e.ENTER) {
    		this.onSearchCallRecordByNum();
    	}
    },
    
    onSearchCallRecordByNum: function() {
    	var viewModel = this.getView().getViewModel();
    	var num = viewModel.get('search_c_num');
    	var ed_num = viewModel.get('searched_c_num');
    	
    	if(!Ext.isEmpty(num) && num !== ed_num) {
    		console.log(num);
    		var store = this.lookupReference('call_record_grid').getStore();
    		store.proxy.extraParams.c_num = num;
        	store.load();
        	viewModel.set('searched_c_num', num);
    	}
    },
    
    onSearchCallRecording: function() {
    	var store = this.lookupReference('call_record_grid').getStore();
		store.proxy.extraParams.c_num = '';
    	store.load();
    }
});
