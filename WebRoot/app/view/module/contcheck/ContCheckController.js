Ext.define('app.view.module.contcheck.ContCheckController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.cont-check',

    refreshBaseStore: function(){
		var mainViewModel = this.getView().up('app-main').getViewModel();
		
		/*
    	console.log("--refreshBaseStore:paytypeArr");
    	if (AppUtil.ChoiceData["paytypeArr"]){
    		AppUtil.ChoiceData["paytypeArr"] = null;
    	}
		if(mainViewModel.get('contSalesStore_for_refresh') != null) {
			mainViewModel.get('contSalesStore_for_refresh').reload();
		}
		*/
    },
    
	onAddFromExcel: function() {
		var win = this.lookupReference('update_excle_window');
		
		if(win) {
			win.destroy();
		}
		
		win = Ext.create('app.ux.window.UpdateExcelWindow', {
	    	title: '批量添加淘宝客购物资产',
	    	reqUrl: 'contChecks/save_excel.do',
	    	reloadGrid: this.getView().getStore(),
	    	excelTemplate: 'template/contcheck_insert_template.xls'
	    });
	    this.getView().add(win);
		
		win.down('form').reset();
		
		win.show();
	},

    onRemove: function(grid, row, col, item, e, record) {
    	
    	Ext.Msg.show({
		    title:'删除',
		    message: '淘宝客商品：' + record.raw.name,
		    buttons: Ext.Msg.YESNO,
		    icon: Ext.Msg.QUESTION,
		    scope: this,
		    fn: function(btn) {
		        if (btn === 'yes') {
		        	this.del(grid, record);
		        }
		    }
		});
    	
    },
    
    del: function(grid, record) {
    	var p = this.getView();
    	p.mask('删除中...');
    	var self = this;

    	Ext.Ajax.request({
    		url: 'contChecks/delete.do',
    		async: true,
    		params: {
    			id: record.raw.id
    		},
    		scope: this,
    		success: function(resp, opt) {
    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				grid.getStore().remove(record);
    			}
    			
    			p.unmask();
    			Ext.Msg.alert('提示', respJson.msg);
    			self.refreshBaseStore();
    		}
    	});
    },
    onRobotSales:function(){
    	var p = this.getView();
    	p.mask('任务添加中...');
    	var self = this;

    	Ext.Ajax.request({
    		url: 'contChecks/robot.do',
    		async: true,
    		params: {},
    		scope: this,
    		success: function(resp, opt) {
    			var respJson = Ext.JSON.decode(resp.responseText);
    			p.unmask();
    			Ext.Msg.alert('提示', respJson.msg);
    			self.refreshBaseStore();
    		},
    		failure:function(){
    		}
    		
    	});
    },
    
    onSearch: function() {
    	var store = this.getView().getStore();
    	var viewModel = this.getView().getViewModel();
    	var panel = this.getView();
    	viewModel.set('search_start_time', panel.query("textfield[name=search_start_time]")[0].getValue());
    	viewModel.set('search_end_time', panel.query("textfield[name=search_end_time]")[0].getValue());
    	
    	store.proxy.extraParams.c_status = viewModel.get('search_status');
    	store.proxy.extraParams.c_name = viewModel.get('search_name');
    	store.proxy.extraParams.c_price_from = viewModel.get('search_price_from');
    	store.proxy.extraParams.c_price_to = viewModel.get('search_price_to');
    	store.proxy.extraParams.start_time = viewModel.get('search_start_time');
    	store.proxy.extraParams.end_time = viewModel.get('search_end_time');
    	store.load();

    	viewModel.set('c_id', -1);
    },
    parseStatusV: function(v) {
    	var str = '';
    	var store = 'contCheckStatusStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    },
    onStatusSelect: function(combo, record) {
    	
    	Ext.Msg.alert(record.raw.s_id,record.raw+ record.raw.s_name+combo)
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_status', record.raw.s_id);
    },
    onStatusChange: function(combo, newValue, oldValue, eOpts ) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_status', newValue);
	},
    
    onTitleChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_name', newValue);
    },
    onPriceFromChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_price_from', newValue);
    },
    onPriceToChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_price_to', newValue);
    },
    onStartTimeChange: function(tf,newValue,oldValue,eOpts){
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_start_time',newValue);
    },
    onEndTimeChange: function(tf,newValue,oldValue,eOpts){
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_end_time',oldValue);
    },
    onSearchReset: function () {
		var panel = this.getView();
		panel.query("combobox[name=c_s_status]")[0].setValue("");
		panel.query("textfield[name=search_name]")[0].setValue("");
		panel.query("textfield[name=search_price_from]")[0].setValue("");
		panel.query("textfield[name=search_price_to]")[0].setValue("");
		var start_time = Ext.Date.add(Ext.Date.clearTime(new Date()), Ext.Date.MONTH, -1);
		var start_time_format = Ext.util.Format.date(start_time, "Y-m-d H:i:s");
		var end_time_format =  Ext.util.Format.date(new Date(), "Y-m-d H:i:s");
		panel.query("textfield[name=search_start_time]")[0].setValue(start_time_format);
		panel.query("textfield[name=search_end_time]")[0].setValue(end_time_format);
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_status', -10000);
    	viewModel.set('search_name', "");
    	viewModel.set('search_price_from', "");
    	viewModel.set('search_price_to', "");
    	viewModel.set('search_start_time', start_time_format);
    	viewModel.set('search_end_time', end_time_format);
	},
	
    mainViewModel: null
});
