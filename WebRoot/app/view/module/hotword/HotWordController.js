Ext.define('app.view.module.hotword.HotWordController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.hotword',
    
    onAddBtn: function(btn, e) {
    	var win = this.lookupReference('hotword_window');
    	var viewModel = this.getView().getViewModel();
    	
    	if (!win) {
            win = Ext.create('app.view.module.hotword.HotWordWindow', {
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
    	
    	var win = this.lookupReference('hotword_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.hotword.HotWordWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('修改：' + record.raw.hotword);
    	
    	win.down('form').loadRecord(record);
    	
    	win.show();
    },

    onRemove: function(grid, row, col, item, e, record, title) {
    	var viewModel = this.getView().getViewModel();
    	if (record.raw.site_id == 0 && viewModel.get('hw_site_id') != 0){//
    		return ;
    	}
    	Ext.Msg.show({
		    title:title,
		    message: '热词：' + record.raw.hotword +"，站点："+this.parseSiteV(""+record.raw.site_id),
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
    onRelaDel: function(grid) {
		var gridselectrows = grid.getSelectionModel().getSelection();
		if(gridselectrows.length<=0) {
			Ext.MessageBox.alert('提示', '至少选中一个热词');
			return ;
		}
		var info = "";
		var ids = "";//资产
		for(var i=0; i<gridselectrows.length; i++){
			if (gridselectrows[i].get('site_id') != '0'){
				ids += gridselectrows[i].get('hw_id') + ',';
				info += gridselectrows[i].get('hotword') + ',';
			}
		}
		
		if (ids != ""){
			info = '热词：' + info.substring(0, info.length-1);
			ids = ids.substring(0, ids.length-1);
	    	Ext.Msg.show({
			    title:'解绑',
			    message: info,
			    buttons: Ext.Msg.YESNO,
			    icon: Ext.Msg.QUESTION,
			    scope: this,
			    fn: function(btn) {
			        if (btn === 'yes') {
			    		Ext.Ajax.request({
			    			url:'hotword/del_rela_hotword.do',
			    			params:{
			    				ids:ids
			    			},
			    			success:function(response) {
			    				var result = Ext.decode(response.responseText);
			    				if (result.success) {
			    					grid.store.reload();

			    					Ext.Msg.alert('Success', '解绑成功');
			    				} else {
			    					Ext.MessageBox.show({
			    								title : "错误提示",
			    								msg : result.msg,
			    								width : 110,
			    								buttons : Ext.Msg.OK,
			    								icon : Ext.Msg.ERROR
			    							});
			    				}
			    			},
			    			failure:function(){
			    				Ext.MessageBox.show({
			    					title:"提示",
			    					msg:"解绑失败",
			    					width:110,
			    					buttons:Ext.Msg.OK
			    				});
			    			}
			    		});
			        }
			    }
			});
		}
    },
    onRelaAddBtn: function(btn, e) {
    	var win = this.lookupReference('hotword_rela_window');
    	var viewModel = this.getView().getViewModel();
    	
    	if (!win) {
            win = Ext.create('app.view.module.hotword.HotWordRelaWindow', {
            	viewModel: viewModel
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('关联热词到站点');
    	
    	win.show();
    },
    onRelaAdd: function() {
    	console.log('---here-11111');
    	var win = this.lookupReference('hotword_rela_window');
		var grid_select_rela = win.down('gridpanel');
    	var viewModel = this.getView().getViewModel();
    	var site_id = viewModel.get('hw_site_id');
    	var grid = this.getView();

		var gridselectrows = grid_select_rela.getSelectionModel().getSelection();
		if(gridselectrows.length<=0) {
			Ext.MessageBox.alert('提示', '至少选中一个热词');
		} else {
		    Ext.MessageBox.confirm('提示框', '您确定要绑定到站点【'+this.parseSiteV(""+site_id)+'】？',function(btn){ 
			if(btn=='yes'){ 
				var ids = "";//资产
				for(var i=0; i<gridselectrows.length; i++){
					ids = ids + gridselectrows[i].get('hw_id') + ',';
				}
				ids = ids.substring(0, ids.length-1);
		    	console.log('---here-site_id='+site_id+',ids='+ids);
				if(site_id == '-1' || site_id == '0') {
					return null;
				}
				
				Ext.Ajax.request({
					url:'hotword/add_rela_hotword.do',
					params:{
						ids:ids,
						site_id:site_id
					},
					success:function(response) {
						var result = Ext.decode(response.responseText);
						if (result.success) {
							grid_select_rela.store.reload();
							grid.store.reload();

							Ext.Msg.alert('Success', '绑定成功');
						} else {
							Ext.MessageBox.show({
										title : "错误提示",
										msg : result.msg,
										width : 110,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
						}
					},
					failure:function(){
						Ext.MessageBox.show({
							title:"提示",
							msg:"保存失败",
							width:110,
							buttons:Ext.Msg.OK
						});
					}
				});
			}});
		}
	},
    del: function(grid, record) {
    	var p = this.getView();
    	p.mask('删除中...');
    	
    	Ext.Ajax.request({
    		url: 'hotword/del.do',
    		async: true,
    		params: {
    			hotword_del: record.raw.hotword,
    			site_id_del: record.raw.site_id,
    			hw_id: record.raw.hw_id
    		},
    		scope: this,
    		success: function(resp, opt) {
    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				grid.getStore().remove(record);
    			}
    			
    			p.unmask();
    			Ext.Msg.alert('提示', respJson.msg);
    		}
    	});
    },
    
    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('hotword_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	
    	var url = 'hotword/update.do';
    	if(values.hw_id === "-1") {
    		url = 'hotword/save.do';
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
	    		}
	    	});
    	}
    },
    
    mainViewModel: null,
    
    parseSiteV: function(v) {
    	
    	var str = '';
    	var store = 'siteAllStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    },
    
    onSearchVP: function() {
    	var store = this.getView().getStore();
    	var viewModel = this.getView().getViewModel();
    	
    	var hotword_site_id = viewModel.get('search_site');
    	var search_hotword = viewModel.get('search_hotword');
    	
    	viewModel.set('hw_site_id', hotword_site_id);
    	store.proxy.extraParams.hotword = search_hotword;
    	store.proxy.extraParams.site_id = hotword_site_id;
    	store.reload({params:{start:0}});

//    	if (hotword_site_id == '0' || hotword_site_id == '-10000'){
//    		button_new_hotword.setDisabled(false);
//    		button_modify_hotword.setDisabled(false);
//    		button_delete_hotword.setDisabled(false);
//    		button_rela_hotword.setDisabled(true);
//    	}
//    	else{
//    		button_new_hotword.setDisabled(true);
//    		button_modify_hotword.setDisabled(true);
//    		button_delete_hotword.setDisabled(true);
//    		button_rela_hotword.setDisabled(false);
//    	}
    },
    
    onHotWordChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_hotword', newValue);
    },
    
    onSiteSelect: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_site', combo.getValue());
    }
});
