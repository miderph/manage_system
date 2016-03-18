Ext.define('app.view.module.menurelacont.Controller', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.menurelacont',
    
    onPreviewClick: function(tree, row, col, item, e, record) {
    	var me = this;
    	me.getView().mask('载入图片...');

    	Ext.Ajax.request({
    		url: 'img/query_by_cid.do?type=0',
    		async: true,
    		params: {
    			c_id: record.raw.c_id
    		},
    		scope: this,
    		success: function(resp, opt) {
    			me.getView().unmask();
    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				imgObj = {
	    				c_img_url: respJson.imgData.c_img_url,
	    				c_img_little_url: respJson.imgData.c_img_little_url,
	    				c_img_icon_url: respJson.imgData.c_img_icon_url,
	    				c_img_4_squares_url: respJson.imgData.c_img_4_squares_url,
	    				c_img_6_squares_url: respJson.imgData.c_img_6_squares_url
    				};

    				record.raw.c_img_rec_postion = respJson.imgData.c_img_rec_postion;
    				me.showPreviewWin(record, imgObj);
    			} else {
    				Ext.toast({
         				html: respJson.msg,
         				title: '预览失败',
         				saveDelay: 10,
         				align: 'tr',
         				closable: true,
         				width: 200,
         				useXAxis: true,
         				slideInDuration: 500
         			});
    			}
    		},
    		failure : function(form, action) {
    			me.getView().unmask();
    				Ext.toast({
    					title: '提示',
         				html: '预览失败',
         				saveDelay: 10,
         				align: 'tr',
         				closable: true,
         				width: 200,
         				useXAxis: true,
         				slideInDuration: 500
         			});
			}
    	});
    },
    
    showPreviewWin: function(record, columnImg) {
    	var win = this.lookupReference('previewimg_window');
    	var viewModel = this.getView().getViewModel();
    	if (!win) {
    		win = Ext.create('app.view.module.column.PreviewImgWindow', {
            	viewModel: viewModel
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('“' + record.raw.title + '” 栏目图片');
    	var winForm = win.down('form');
    	winForm.reset();
    	
    	var empty = viewModel.get('empty');
    	viewModel.set('c_img_url', empty);
    	viewModel.set('c_img_little_url', empty);
    	viewModel.set('c_img_icon_url', empty);
    	viewModel.set('c_img_4_squares_url', empty);
    	viewModel.set('c_img_6_squares_url', empty);
    	
    	
    	if(!Ext.isEmpty(columnImg.c_img_url))
    		viewModel.set('c_img_url', columnImg.c_img_url);
    	if(!Ext.isEmpty(columnImg.c_img_little_url))
    		viewModel.set('c_img_little_url', columnImg.c_img_little_url);
    	if(!Ext.isEmpty(columnImg.c_img_icon_url))
    		viewModel.set('c_img_icon_url', columnImg.c_img_icon_url);
    	if(!Ext.isEmpty(columnImg.c_img_4_squares_url))
    		viewModel.set('c_img_4_squares_url', columnImg.c_img_4_squares_url);
    	if(!Ext.isEmpty(columnImg.c_img_6_squares_url))
    		viewModel.set('c_img_6_squares_url', columnImg.c_img_6_squares_url);

    	winForm.loadRecord(record);
    	win.show();
    },

    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('column_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	
    	var url = 'column/update.do';
    	if(values.c_id === "-1") {
    		url = 'column/save.do';
    	}
    	
    	values.site_id = grid.getViewModel().get('column_site');
    	var c_pid = values.c_pid;
    	
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
	    				
	    				if(c_pid<1) {
	    					grid.getStore().reload({
	    						params: {
	    							userid: grid.getRootNode().getId()
	    						}
	    					});
	    				} else {
	    					grid.getStore().reload();
	    				}
	    			}
	    			
	    			win.unmask();
	    			
	    			Ext.Msg.alert('提示', action.result.msg);
	    		}
	    	});
    	}
    },
    
    onCancel: function() {
    	var win = this.lookupReference('column_window');
    	win.down('form').reset();
    	win.hide();
//        win.destroy();
    	win = null;
    },
    
    onSearchColumn: function() {
    	var store = this.getView().getStore();
    	var viewModel = this.getView().getViewModel();
    	
    	viewModel.set('column_site', viewModel.get('column_site_comb'));
    	store.proxy.extraParams.site_id = viewModel.get('column_site');
    	store.proxy.extraParams.status = viewModel.get('column_status');
    	store.load();
    },
    
    onSiteSelect: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('column_site_comb', record.raw.s_id);
    },
    
    onStatusSelect: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('column_status', record.raw.s_id);
    },

    kvData: {
    	
    },
    
    parseValue: function(key, store, k, v) {
    	
    	if(!this.kvData[store]) {
    		this.kvData[store] = {};
    	}
    	
    	var kv = this.kvData[store];
    	
    	if(!kv['k' + key]) {
    		var items = this.getView().getViewModel().get(store).getData().items;
    		
    		Ext.Array.each(items, function(item, index) {
    			kv['k' + item.raw[k]] = item.raw[v];
			});
    		
    		this.kvData[store] = kv;
    	}
    	
    	return kv['k' + key];
    },
    
    mainViewModel: null,
    
    parseStatusV: function(v) {
    	var str = '';
    	var store = 'columnStatusStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    },
    
    parseProviderV: function(v) {
    	var str = '';
    	var store = 'contProviderByAuthStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 'cp_id', 'cp_name');
    	}
    	
    	return str;
    },
    
    parseTypeV: function(v) {
    	var str = '';
    	var store = 'columnTypeStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    },
    
    parseActTypeV: function(v) {
    	var str = '';
    	var store = 'columnActTypeStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    },
    
    parseResourceTypeV: function(v) {
    	var str = '';
    	var store = 'columnResTypeStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    }

});
