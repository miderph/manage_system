Ext.define('app.view.module.provider.ContProviderController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.cont-provider',
    mainViewModel: null,

    refreshBaseStore: function(){
		var mainViewModel = this.getView().up('app-main').getViewModel();
		if(mainViewModel.get('contProviderByAuthStore') != null) {
			mainViewModel.get('contProviderByAuthStore').reload();
		}
    	console.log("--refreshBaseStore:contproviderArr");
    	if (AppUtil.ChoiceData["contproviderArr"]){
    		AppUtil.ChoiceData["contproviderArr"] = null;
    	}
		if(mainViewModel.get('roleStore_for_refresh') != null) {
			mainViewModel.get('roleStore_for_refresh').reload();
		}
    },
    parseIconUrl: function(v) {
    	if (v == null || v == "")
    		return "";
    	else
    		return "<img src='"+v+"' height='20'>";
    },
    onAddBtn: function(btn, e) {
    	if(this.providerId !== '') {
    		this.isReqStbPre = true;
    		this.providerId = '';
    	}
    	
    	var win = this.lookupReference('cont_provider_window');
    	if (win){//不重用，解决上次新建时的残留图片预览
    		//Ext.destroy( win );//有问题
    		win = null;
    	}
    	if (!win) {
            win = Ext.create('app.view.module.provider.ContProviderWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('添加');
    	win.down('form').reset();
    	
    	win.show();
    },
    
    onModify: function(grid, row, col, item, e, record) {
    	console.log("------record.raw.cp_id="+record.raw.cp_id);
    	
    	if(this.providerId !== record.raw.cp_id) {
    		this.isReqStbPre = true;
    		this.providerId = record.raw.cp_id;
    	}
    	console.log("------record.raw.cp_id="+record.raw.cp_id);
    	var win = this.lookupReference('cont_provider_window');
    	if (!win) {
            win = Ext.create('app.view.module.provider.ContProviderWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }

    	win.setTitle('修改');
    	
    	win.down('form').loadRecord(record);
    	
    	win.show();
    },

    onRemove: function(grid, row, col, item, e, record) {
    	
    	Ext.Msg.show({
		    title:'删除',
		    message: record.raw.name,
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
    		url: 'contprovider/del.do',
    		async: true,
    		params: {
    			cp_id: record.raw.cp_id
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
    
    showChoiceWindow: function(title, btn, allData) {
    	var win = this.lookupReference('checkboxgroup_window');
    	var ids = this.lookupReference(btn.rValueName);
    	
    	var checkedData = [];
    	
    	var ids_v = ids.getValue();
    	if(ids_v != null && ids_v != undefined && ids_v.length > 0) {
    		checkedData = ids_v.split(',');
    	}
    	
    	
    	var names = this.lookupReference(btn.rTextName);
    	
    	if (win) {
    		win.destroy();
        }
    	
    	win = Ext.create('app.ux.window.CheckboxGroupWindow', {
        	width: 600,
        	title: title,
        	allData: allData,
        	checkedData: checkedData,
        	onSubmit: function(w, vArr, tArr) {
        		ids.setValue(vArr.join(','));
            	names.setValue(tArr.join(','));
        		w.destroy();
        	},
        	onCancel: function(w) {
        		w.destroy();
        	}
        });
    	
        this.getView().add(win);
    	
    	win.show();
    },
    
    providerStore: Ext.create('app.store.ProviderStore'),
    
    providerAllData: [],
    
    onChoice: function(btn) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('cont_provider_window'),
			viewModel: this.mainViewModel,
			storeRef: 'contProviderByAuthStore_multi_select',
			store: 'app.store.ContProviderByAuthStore',
			win_title: '选择供应商',
			url: 'contprovider/query_with_auth.do',
			d_key: 'contproviderArr',
			d_id: 'cp_id',
			d_name: 'cp_name'
		});
    },
    
    requestChoiceD: function(win, url, reader, cb) {
    	
    	win.mask('读取中...');
    	Ext.Ajax.request({
    		url: url,
    		async: true,
    		scope: this,
    		timeout: 5*60*1000,
    		success: function(resp){
    			var respJson = Ext.JSON.decode(resp.responseText);
    			
    			var data = [];
    			var root = reader.root;
    			if(root != null && root != undefined && root.length > 0) {
    				data = respJson[root];
    			} else {
    				data = respJson;
    			}
    			
    			var allData = [];
    			var id = reader.id;
    			var name = reader.name;
    			Ext.Array.each(data, function(item, index) {
    				if(item[id] > -1) {
    					allData.push({
    						text: item[name],
    						value: item[id]
    					});
    				}
				});
    			
    			cb(allData, this);
    			win.unmask();
    	    }
    	});
    },
    
    stbPrefixAllData: [],
    providerId: '',
    isReqStbPre: true,
    
    onChoiceStbPrefix: function(btn) {
    	
    	if(this.isReqStbPre || this.stbPrefixAllData.length < 1) {
    		this.isReqStbPre = false;
        	var win = this.lookupReference('cont_provider_window');
        	
    		this.requestChoiceD(win, 'stbprefixe/query_unbound.do?provider_id=' + this.providerId, {
    			id: 'sp_id',
    			name: 'sp_code',
    			root: 'data'
    		}, function(allData, self) {
    			self.stbPrefixAllData = allData;
    			self.showChoiceWindow('选择Prefix前缀', btn, self.stbPrefixAllData);
    		});
    	} else {
    		this.showChoiceWindow('选择Prefix前缀', btn, this.stbPrefixAllData);
    	}
    },
    
    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('cont_provider_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	var self = this;

    	var url = 'contprovider/update.do';
    	if(values.cp_id === "-1") {
    		url = 'contprovider/save.do';
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
    onHotInfoByChannel: function(btn, e) {
    	var view = this.getView();
		var rows = view.getSelectionModel().getCount();// 返回值为Record数组
        var cp_id = -1;
        var cp_name = '';
		if(rows == 1) {
			var record = view.getSelectionModel().lastSelected;
	        cp_id = record.raw.cp_id;
	        cp_name = record.raw.name;
		}
		//console.log("------provider cp_id="+cp_id);
		
		AppUtil.crudHotInfo(this, cp_id, cp_name, 'provider');
		
    },
    parseSiteV: function(v) {
    	
    	var str = '';
    	var store = 'siteAllStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    },
	onPayType: function(btn, e) {
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('cont_provider_window'),
			//viewModel: this.mainViewModel,
			//storeRef: 'contProviderByAuthStore',
			//store: 'app.store.ContProviderByAuthStore',
			win_title: '选择支付方式',
			url: 'paytypes/query_all.do',
			d_key: 'paytypeArr',
			d_id: 'id',
			d_name: 'description'
		});
    },
    onSearchProvider: function() {
    	var store = this.getView().getStore();
    	var viewModel = this.getView().getViewModel();
    	
    	store.proxy.extraParams.c_name = viewModel.get('search_name');
    	store.load();

    	viewModel.set('c_id', -1);
    },
    onSearchReset: function () {
		var panel = this.getView();
		panel.query("textfield[name=search_name]")[0].setValue("");
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_name', "");
	},
    onTitleChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_name', newValue);
    }
});
