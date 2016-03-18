Ext.define('app.view.module.testgroup.TestGroupController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.testgroup',

    refreshBaseStore: function(){
		var mainViewModel = this.getView().up('app-main').getViewModel();
		if(mainViewModel.get('testGroupAllStore') != null) {
			mainViewModel.get('testGroupAllStore').reload();
		}
    	console.log("--refreshBaseStore:groupArr");
    	if (AppUtil.ChoiceData["groupArr"]){
    		AppUtil.ChoiceData["groupArr"] = null;
    	}
    	if (AppUtil.ChoiceData["ugByMacArr"]){
    		AppUtil.ChoiceData["ugByMacArr"] = null;
    	}
    	if (AppUtil.ChoiceData["ugByZoneArr"]){
    		AppUtil.ChoiceData["ugByZoneArr"] = null;
    	}
    	if (AppUtil.ChoiceData["ugByModelArr"]){
    		AppUtil.ChoiceData["ugByModelArr"] = null;
    	}
    	if (AppUtil.ChoiceData["ugByChannelArr"]){
    		AppUtil.ChoiceData["ugByChannelArr"] = null;
    	}
		if(mainViewModel.get('roleStore_for_refresh') != null) {
			mainViewModel.get('roleStore_for_refresh').reload();
		}
    },
    
    onAddBtn: function(btn, e) {
    	var win = this.lookupReference('testgroup_window');
    	var viewModel = this.getView().getViewModel();
    	
    	if (!win) {
            win = Ext.create('app.view.module.testgroup.TestGroupWindow', {
            	viewModel: viewModel
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('添加');
    	win.down('form').reset();
    	
    	win.down('combobox').setValue(viewModel.get('hw_site_id'));
    	
    	win.show();
    },
    
    onModify: function(grid, row, col, item, e, record) {
    	
    	var win = this.lookupReference('testgroup_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.testgroup.TestGroupWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('修改：' + record.raw.name);
    	
    	win.down('form').loadRecord(record);
    	
    	win.show();
    },

    onRemove: function(grid, row, col, item, e, record) {
    	
    	Ext.Msg.show({
		    title:'删除',
		    message: '前缀名称：' + record.raw.name,
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
    		url: 'usergroup/del.do',
    		async: true,
    		params: {
    			ug_id: record.raw.ug_id
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
    
    onChoiceRaw: function(btn) {
    	var win = this.lookupReference('testgroup_window');
      	var v = win.down('combobox').getValue();
      	var form = win.down('form');
    	if (v == "zone"){
    		SelectArea.show("ids_value","raw_value",form);
    	} else if (v == "model"){
        	 AppUtil.showChoiceWindow({
     			view: this,
     			btn: {
     				rValueName: 'r_ids_value',
    				rTextName: 'r_raw_value'
     			},
     			mWin: win,
     			viewModel: this.mainViewModel,
     			storeRef: 'stbModelStore',
     			store: 'app.store.StbModelStore',
     			win_title: '选择型号',
     			url: 'stbmodel/query.do',
     			d_key: 'stbmodelArr',
     			d_id: 'sm_id',
     			d_name: 'sm_model'
     		});
         } else if (v == "channel"){
        	 AppUtil.showChoiceWindow({
     			view: this,
     			btn: {
     				rValueName: 'r_ids_value',
    				rTextName: 'r_raw_value'
     			},
     			mWin: win,
     			viewModel: this.mainViewModel,
     			storeRef: 'contChannelStoreByApk',
     			store: 'app.store.ChannelStoreByApk',
     			win_title: '选择apk发布渠道',
     			url: 'contchannel/query_all.do?type=apk',
     			d_key: 'contchannelArr',
     			d_id: 's_id',
     			d_name: 'name'
     		});
         }
    },
    
    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('testgroup_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	var self = this;

    	var url = 'usergroup/update.do';
    	if(values.ug_id === "-1") {
    		url = 'usergroup/save.do';
    	}
    	
    	if (form.isValid()){
    		win.mask('正在保存...');
    		
	    	form.submit({
	    		clientValidation: true,
	    	    url: url,
	    		params: values,
	    		submitEmptyText: false,
	    		success: function(form, action) {
	    			if(action.result.issuc) {
	    				form.reset();
	    				win.hide();
	//    				win.destroy();
	    				
	    				grid.getStore().reload();
	    			}
	    			win.unmask();
	    			
	    			Ext.Msg.alert('提示', action.result.msg);
	    			self.refreshBaseStore();
	    		}
	    	});
    	}
    },
    
    mainViewModel: null,
    
    parseTgTypeV: function(v) {
    	
    	var str = '';
    	var store = 'testGroupStatusStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    },
    
    onSearchVP: function() {
    	var store = this.getView().getStore();
    	var viewModel = this.getView().getViewModel();
    	store.proxy.extraParams.tg_name = viewModel.get('search_tg_name');
    	store.proxy.extraParams.tg_type = viewModel.get('search_tg_type');
    	viewModel.set('search_tg_type_id', viewModel.get('search_tg_type'));
    	
    	store.load();
    },
    
    onTgNameChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_tg_name', newValue);
    },
    
    onTgTypeSelect: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_tg_type', combo.getValue());
    }
});
