Ext.define('app.view.module.column.ColumnController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.column',
    
    onAddInfo: function(btn, e) {
//    	var base = 1;
//		var img = 1;
//		if(btn.bTag === 'base') {
//			base = 1;
//			img = 0;
//		}
		
		var tree = this.getView();
		
		if(btn.bTag === 'one') {
			var viewModel = tree.getViewModel();
			if(viewModel.get('column_site') == -1) {
				Ext.toast({
	 				html: '请选择站点',
	 				title: '提示',
	 				saveDelay: 10,
	 				align: 'tr',
	 				closable: true,
	 				width: 200,
	 				useXAxis: true,
	 				slideInDuration: 500
	 			});
			} else {
		    	//viewModel.set('column_base_info', base);
		    	//viewModel.set('column_img_info', img);
		    	this.onAddClick(tree, -1, -1, null, e, null);
			}
		} else {
			var record = tree.getSelectionModel().lastSelected;
			if(record == undefined || record == null) {
				Ext.toast({
	 				html: '请选择一个栏目',
	 				title: '提示',
	 				saveDelay: 10,
	 				align: 'tr',
	 				closable: true,
	 				width: 200,
	 				useXAxis: true,
	 				slideInDuration: 500
	 			});
			} else {
				var viewModel = tree.getViewModel();
//		    	viewModel.set('column_base_info', base);
//		    	viewModel.set('column_img_info', img);
		    	this.onAddClick(tree, -1, -1, null, e, record);
			}
		}
    },

    onAddSubClick: function(tree, row, col, item, e, record) {
    	var viewModel = this.getView().getViewModel();
    	//viewModel.set('column_base_info', 1);
    	//viewModel.set('column_img_info', 1);
    	this.onAddClick(tree, -1, -1, null, e, record);
    },
    
    onAddClick: function(tree, row, col, item, e, record) {
    	var c_pid = '0';
    	var title = '新建一级栏目'
    	if(record != undefined && record != null) {
    		c_pid = record.raw.c_id;
    		title = '新建 “' + record.raw.title + '” 的子栏目';
    		
    		if(record.raw.struct_type == '2'
    			|| record.raw.is_shortcut == '1'
        		|| record.raw.is_shortcut == '2'
            	|| record.raw.is_shortcut == '3'
    				) {
        		Ext.toast({
     				html: '该栏目下不能新建子栏目',
     				title: '提示',
     				saveDelay: 10,
     				align: 'tr',
     				closable: true,
     				width: 200,
     				useXAxis: true,
     				slideInDuration: 500
     			});
        		
        		return;
        	}
    	}
    	
    	var win = this.lookupReference('column_window');
    	//console.log('onAddClick column_base_info: ' + this.getView().getViewModel().get('column_base_info'));
    	//console.log('onAddClick column_img_info: ' + this.getView().getViewModel().get('column_img_info'));
    	if (win){//不重用，解决上次新建时的残留图片预览
    		//Ext.destroy( win );//有问题
    		win = null;
    	}
    	if (!win) {
            win = Ext.create('app.view.module.column.ColumnWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle(title);
    	var form = win.down('form');
    	form.reset();
    	
    	var pidComp = form.query('textfield[name=c_pid]');
    	if(pidComp && pidComp.length > 0) {
    		pidComp = pidComp[0];
    		pidComp.setValue(c_pid);
    	}
    	
    	win.show();
    },
    
    onCopyToSite: function() {
    	var win = this.lookupReference('copysite_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.column.CopySiteWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.columnStore = this.getView().getStore();
    	
    	win.show();
    },
    
    onCopyToSiteOne: function() {
    	var win = this.lookupReference('copysiteone_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.column.CopySiteOneWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.columnStore = this.getView().getStore();
    	
    	win.show();
    },
    
    onCopyToColumn: function() {
    	var win = this.lookupReference('copycolumn_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.column.CopyColumnWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.columnStore = this.getView().getStore();
    	
    	win.show();
    },
    
    onModifyInfo: function(btn, e) {
    	var tree = this.getView();
    	
    	var record = tree.getSelectionModel().lastSelected;
    	if(record == undefined || record == null) {
			Ext.toast({
 				html: '请选择一个栏目',
 				title: '提示',
 				saveDelay: 10,
 				align: 'tr',
 				closable: true,
 				width: 200,
 				useXAxis: true,
 				slideInDuration: 500
 			});
		} else {
//			var base = 1;
//			var img = 1;
//			if(btn.bTag === 'base') {
//				base = 1;
//				img = 0;
//			} else if(btn.bTag === 'img') {
//				base = 0;
//				img = 1;
//			}
//			var viewModel = tree.getViewModel();
//	    	viewModel.set('column_base_info', base);
//	    	viewModel.set('column_img_info', img);
	    	this.onModifyClick(tree, -1, -1, null, e, record);
		}
    	
    },
    
    onModifyClick: function(tree, row, col, item, e, record) {
    	var viewModel = this.getView().getViewModel();
    	var me = this;
    	me.getView().mask('载入信息...');

    	var win = this.lookupReference('column_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.column.ColumnWindow', {
            	viewModel: viewModel,
            	myviewModel: viewModel
            });
        	win.hide();
            this.getView().add(win);
        }
    	
    	win.setTitle('修改：' + record.raw.title);
    	var winForm = win.down('form');
    	winForm.reset();

    	if(viewModel.get('column_img_info') == 1) {
    		this.requestImgData(win, winForm, record);
    	} else {
			me.getView().unmask();
    		winForm.loadRecord(record);
        	win.show();
    	}
    	
    },
    
    requestImgData: function(win, winForm, record) {
    	var me = this;
    	Ext.Ajax.request({
    		url: 'img/query_by_cid.do',
    		async: true,
    		params: {
    			type: '0',
    			c_id: record.raw.c_id
    		},
    		scope: this,
    		success: function(resp, opt) {
    			me.getView().unmask();

    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				record.raw.c_img_id = respJson.imgData.c_img_id;
    				record.raw.c_img_rec_postion = respJson.imgData.c_img_rec_postion;
    				record.raw.c_img_plat_group = respJson.imgData.c_img_plat_group;
    				record.raw.c_img_intro = respJson.imgData.c_img_intro;
    				record.raw.c_img_locked = respJson.imgData.c_img_locked;
    				
    				var c_img_url = respJson.imgData.c_img_url;
    				if(Ext.isEmpty(c_img_url)) {
    					c_img_url = Ext.BLANK_IMAGE_URL;
    				}
    				record.raw.c_img_url = c_img_url;
    				
    				var c_img_little_url = respJson.imgData.c_img_little_url;
    				if(Ext.isEmpty(c_img_little_url)) {
    					c_img_little_url = Ext.BLANK_IMAGE_URL;
    				}
    				record.raw.c_img_little_url = c_img_little_url;
    				
    				var c_img_icon_url = respJson.imgData.c_img_icon_url;
    				if(Ext.isEmpty(c_img_icon_url)) {
    					c_img_icon_url = Ext.BLANK_IMAGE_URL;
    				}
    				record.raw.c_img_icon_url = c_img_icon_url;
    				
    				var c_img_4_squares_url = respJson.imgData.c_img_4_squares_url;
    				if(Ext.isEmpty(c_img_4_squares_url)) {
    					c_img_4_squares_url = Ext.BLANK_IMAGE_URL;
    				}
    				record.raw.c_img_4_squares_url = c_img_4_squares_url;
    				
    				var c_img_6_squares_url = respJson.imgData.c_img_6_squares_url;
    				if(Ext.isEmpty(c_img_6_squares_url)) {
    					c_img_6_squares_url = Ext.BLANK_IMAGE_URL;
    				}
    				record.raw.c_img_6_squares_url = c_img_6_squares_url;
    				
    				record.raw.c_img_active_time = respJson.imgData.c_img_active_time;
    				record.raw.c_img_deactive_time = respJson.imgData.c_img_deactive_time;
    			} else {
    				record.raw.c_img_url = Ext.BLANK_IMAGE_URL;
    				record.raw.c_img_little_url = Ext.BLANK_IMAGE_URL;
    				record.raw.c_img_icon_url = Ext.BLANK_IMAGE_URL;
    				record.raw.c_img_4_squares_url = Ext.BLANK_IMAGE_URL;
    				record.raw.c_img_6_squares_url = Ext.BLANK_IMAGE_URL;
    				
    				Ext.toast({
         				html: respJson.msg,
         				title: '提示',
         				saveDelay: 10,
         				align: 'tr',
         				closable: true,
         				width: 200,
         				useXAxis: true,
         				slideInDuration: 500
         			});
    			}
    			winForm.loadRecord(record);
	        	win.show();
    		},
    		failure : function(form, action) {
    			me.getView().unmask();
    				Ext.toast({
    					title: '提示',
         				html: '载入信息失败',
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
    
    onDeleteInfo: function(btn, e) {
    	var tree = this.getView();
    	
    	var record = tree.getSelectionModel().lastSelected;
    	if(record == undefined || record == null) {
			Ext.toast({
 				html: '请选择一个栏目',
 				title: '提示',
 				saveDelay: 10,
 				align: 'tr',
 				closable: true,
 				width: 200,
 				useXAxis: true,
 				slideInDuration: 500
 			});
		} else {
			if(btn.bTag === 'seft') {
				this.onRemoveClick(tree, -1, -1, null, e, record);
			} else if(btn.bTag === 'sub') {
				
			} else {
				
			}
		}
    	
    },
    
    onRemoveClick: function(tree, row, col, item, e, record) {
    	
    	Ext.Msg.show({
		    title:'删除',
		    message: '栏目名称：' + record.raw.title,
		    buttons: Ext.Msg.YESNO,
		    icon: Ext.Msg.QUESTION,
		    scope: this,
		    fn: function(btn) {
		        if (btn === 'yes') {
		        	this.delColumn(tree, record);
		        }
		    }
		});
    	
    },
    
    delColumn: function(grid, record) {
    	Ext.Ajax.request({
    		url: 'column/del.do',
    		async: true,
    		params: {
    			c_id: record.raw.c_id
    		},
    		scope: this,
    		success: function(resp, opt) {
    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				grid.getStore().remove(record);
    			}
    			
    			Ext.toast({
     				html: respJson.msg,
     				title: '提示',
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
    
    onEditSortCont: function(btn, e) {
    	var view = this.getView();
    	
    	var record = view.getSelectionModel().lastSelected;
    	if(record == undefined || record == null) {
			Ext.toast({
 				html: '请选择一个栏目',
 				title: '提示',
 				saveDelay: 10,
 				align: 'tr',
 				closable: true,
 				width: 200,
 				useXAxis: true,
 				slideInDuration: 500
 			});
		} else {
			var win = this.lookupReference('editsort_cont_window');
	    	
	    	if (!win) {
	            win = Ext.create('app.view.module.column.EditSortContWindow');
	            view.add(win);
	        }
	    	
	    	win.setTitle('编排资产：' + record.raw.title);
	    	
	    	var viewModel = view.getViewModel();
	    	if(viewModel.get('edit_sort_c_id') != record.raw.c_id) {
	    		viewModel.set('edit_sort_c_id', record.raw.c_id);
	    		
	    		var store = win.down('gridpanel').getStore();
	    		store.proxy.extraParams.menuId = record.raw.c_id;
				store.load();
	    	}
	    	
	    	win.c_id = record.raw.c_id;
	    	win.show();
		}
    	
    },
    
    onMoveColCont: function(btn, e) {
    	var win = this.lookupReference('editsort_cont_window');
    	var record = win.down('gridpanel').getSelectionModel().lastSelected;
    	var selectIndex = win.selectIndex;
    	var pageSize = win.pageSize;
		if(record == undefined || record == null) {
			Ext.toast({
 				html: '请选择一个资产',
 				title: '提示',
 				saveDelay: 10,
 				align: 'tr',
 				closable: true,
 				width: 200,
 				useXAxis: true,
 				slideInDuration: 500
 			});
		} else {
			win.mask('移动中...');
			Ext.Ajax.request({
				url:'modify_real_menu_cont_order.do',
				params:{
					tag: btn.tag,
					id: record.raw.mar_s_contId,
					menuId: win.c_id
				},
				success:function(response) {
					var grid = win.down('gridpanel');
					var store = grid.getStore();
					if(btn.tag == 'up' && selectIndex > 0) {
						store.remove(record);
						store.insert(selectIndex-1, record);
						win.selectIndex = selectIndex-1;
						grid.getView().refresh();
					} else if(btn.tag == 'down' && selectIndex < pageSize-1) {
						store.remove(record);
						store.insert(selectIndex+1, record);
						win.selectIndex = selectIndex+1;
						grid.getView().refresh();
					} else {
						store.load();
					}
					win.unmask();
				},
				failure:function(){
					win.unmask();
					Ext.toast({
		 				html: '操作失败',
		 				title: '提示',
		 				saveDelay: 10,
		 				align: 'tr',
		 				closable: true,
		 				width: 200,
		 				useXAxis: true,
		 				slideInDuration: 500
		 			});
				}
			});
		}
    },
    
    onEditColContImg: function() {
    	var view = this.getView();
    	
    	var win = this.lookupReference('editsort_cont_window');
    	var records = win.down('gridpanel').getSelectionModel().getSelection();
    	if(records.length == 0){
    		Ext.toast({
 				html: '最少选择一条记录，进行设置!',
 				title: '提示',
 				saveDelay: 10,
 				align: 'tr',
 				closable: true,
 				width: 200,
 				useXAxis: true,
 				slideInDuration: 500
 			});
    	} else {
    		var eids = "";
			var is_url_used_id = "";
			var cont_is_url_used_id = "";
			
			for(var i = 0; i < records.length; i++) {
				eids = eids + records[i].get('mar_s_contId') + ',';
				if("" === is_url_used_id && "null" !== records[i].get('is_url_used_id')) {
					is_url_used_id = records[i].get('is_url_used_id');
				}
				if("" === cont_is_url_used_id && "null" !== records[i].get('cont_is_url_used_id')) {
					cont_is_url_used_id = records[i].get('cont_is_url_used_id');
				}
			}
			if(eids.length>0) {
				eids = eids.substring(0, eids.length-1);
			}
			
			if("" === is_url_used_id) {
				is_url_used_id = cont_is_url_used_id;
			}
			
			if("" === is_url_used_id) {
				is_url_used_id = -1;
			}
			
			var ecc_win = this.lookupReference('editcolcontimg_window');
	    	
	    	if (!ecc_win) {
	    		ecc_win = Ext.create('app.view.module.column.EditColContImgWindow');
	            view.add(ecc_win);
	        }
	    	
	    	ecc_win.down('radiogroup').setValue({c_c_i_postion: is_url_used_id});
	    	ecc_win.eids = eids;
	    	ecc_win.menuId = win.c_id;
	    	ecc_win.gridStore = win.down('gridpanel').getStore();
	    	
	    	ecc_win.show();
    	}
    },
    
    onChoiceMac: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('column_window'),
			viewModel: this.mainViewModel,
			storeRef: 'testGroupByMacStore',
			store: 'app.store.TestGroupByMacStore',
			win_title: '选择网卡地址测试组',
			url: 'usergroup/query_all.do?type=mac',
			d_key: 'ugByMacArr',
			d_id: 'ug_id',
			d_name: 'ug_name'
		});
    	
    },
    
    onChoiceZone: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('column_window'),
			viewModel: this.mainViewModel,
			storeRef: 'testGroupByZoneStore',
			store: 'app.store.TestGroupByZoneStore',
			win_title: '选择地区测试组',
			url: 'usergroup/query_all.do?type=zone',
			d_key: 'ugByZoneArr',
			d_id: 'ug_id',
			d_name: 'ug_name'
		});
    },
    
    onChoiceModel: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('column_window'),
			viewModel: this.mainViewModel,
			storeRef: 'testGroupByModelStore',
			store: 'app.store.TestGroupByModelStore',
			win_title: '选择型号测试组',
			url: 'usergroup/query_all.do?type=model',
			d_key: 'ugByModelArr',
			d_id: 'ug_id',
			d_name: 'ug_name'
		});
    },
    
    onChoiceChannel: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('column_window'),
			viewModel: this.mainViewModel,
			storeRef: 'testGroupByChannelStore',
			store: 'app.store.TestGroupByChannelStore',
			win_title: '选择渠道测试组',
			url: 'usergroup/query_all.do?type=channel',
			d_key: 'ugByChannelArr',
			d_id: 'ug_id',
			d_name: 'ug_name'
		});
    },
    onChoiceShortcut: function(btn, e) {
    	
    	var ids = this.lookupReference(btn.rValueName);
    	var names = this.lookupReference(btn.rTextName);
    	
    	if(btn.rTag === 'con') {
    		var win = this.lookupReference('shortcut_con_window');
        	if (win) {
        		win.destroy();
            }
        	
	    	win = Ext.create('app.view.module.column.ShortcutConWindow', {
	        	viewModel: this.getView().getViewModel(),
	        	onSubmit: function(value, text) {
	        		ids.setValue(value);
	            	names.setValue(value + '|' + text);
	        	}
	        });
	    	win.show();
    	} else if(btn.rTag === 'col') {
    		var win = this.lookupReference('shortcut_col_window');
        	if (win) {
        		win.destroy();
            }
        	
    		win = Ext.create('app.view.module.column.ShortcutColWindow', {
    			onController: this.getView().getController(),
	        	onSubmit: function(value, text) {
	        		ids.setValue(value);
	            	names.setValue(value + '|' + text);
	        	}
	        });
	    	win.show();
    	} else {
    		ids.setValue('');
        	names.setValue('');
    	}
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
    
    onSearchColumn_copy: function() {
    	var win = this.lookupReference('copycolumn_window');
    	var store = win.down('treepanel').getStore();
    	var viewModel = this.getView().getViewModel();
    	
    	store.proxy.extraParams.site_id = viewModel.get('column_site_copy');
    	store.proxy.extraParams.status = viewModel.get('column_status_copy');
    	store.load();
    	viewModel.set('column_cid_copy', '-1');
    },
    
    onSiteSelect_copy: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('column_site_copy', record.raw.s_id);
    },
    
    onStatusSelect_copy: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('column_status_copy', record.raw.s_id);
    },
    
    onColumnSelect_copy: function(rm, record, index) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('column_cid_copy', record.raw.c_id);
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
    },
	showContRuleList: function(btn, e) {
		var view = this.getView();
		var record = view.getSelectionModel().lastSelected;
		if(record == undefined || record == null) {
			Ext.toast({
				html: '请选择一项',
				title: '提示',
				saveDelay: 10,
				align: 'tr',
				closable: true,
				width: 200,
				useXAxis: true,
				slideInDuration: 500
			});
		} else {
			var win = this.lookupReference('cont_rule_window');
			if (!win) {
				win = Ext.create('app.view.module.column.ContRuleWindow', {
					onController: this.getView().getController(),
					menu_id: record.raw.c_id
				});
				this.getView().add(win);
			}
	    	win.menu_id = record.raw.c_id;
            console.log("-----:win.menu_id=" + win.menu_id);

			var store = win.down('gridpanel').getStore();
			store.proxy.extraParams.menu_id = record.raw.c_id;

			win.show();
			store.removeAll();
			store.load();
		}

	},
	onAddContRule: function(contRuleStore, menu_id) {
		var win = this.lookupReference('contruleform_window');

		if (!win) {
			var win = Ext.create('app.view.module.column.ContRuleFormWindow', {
				contRuleStore: contRuleStore,
				menu_id: menu_id
			});
			this.getView().add(win);
		}

		win.setTitle('添加');
		var form = win.down('form');
		form.reset();

		var menuidComp = form.query('textfield[name=menu_id]');
		if(menuidComp && menuidComp.length > 0) {
			menuidComp = menuidComp[0];
			menuidComp.setValue(menu_id);
		}

		win.show();
	},
	onModifyContRule: function(contRuleStore, record) {
		var win = this.lookupReference('contruleform_window');

		if (!win) {
			var win = Ext.create('app.view.module.column.ContRuleFormWindow', {
				contRuleStore: contRuleStore
			});
			this.getView().add(win);
		}

		win.setTitle('修改');
		var form = win.down('form');
		form.reset();

		console.log(record);

		form.loadRecord(record);
		var prightComp = form.query('textfield[name=price_right]');
		if(prightComp && prightComp.length >0){
			prightComp = prightComp[0];
		}
		if(record.raw.price_rela == 'between'){
			prightComp.show();
		}else{
			prightComp.setValue('');
			prightComp.hide();
		}
		
		win.show();
	},

	onDeleteContRule: function(grid, record) {
		var me = this;
		Ext.Msg.show({
			title: '删除',
			message: '删除自动编排规则：' + record.raw.name,
			buttons: Ext.Msg.YESNO,
			icon: Ext.Msg.QUESTION,
			fn: function (btn) {
				if (btn === 'yes') {
					me.deleteContRule(grid, record);
				}
			}
		});
	},
	
	deleteContRule: function(grid, record) {
		var p = this.getView();
		p.mask('删除中...');
		Ext.Ajax.request({
			url: 'contrule/delete.do',
			async: true,
			params: {
				rule_id: record.raw.rule_id,
				menu_id: record.raw.menu_id
			},
			scope: this,
			success: function(resp, opt) {
				var respJson = Ext.JSON.decode(resp.responseText);
				if(respJson.issuc) {
					grid.getStore().remove(record);
				}
				p.unmask();
				Ext.toast({
					html: respJson.msg,
					title: '提示',
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
	onChoiceProvider: function(btn, e) {
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('contruleform_window'),
			viewModel: this.mainViewModel,
			storeRef: 'contProviderByAuthStore',
			store: 'app.store.ContProviderByAuthStore',
			win_title: '选择供应商',
			url: 'contprovider/query_with_auth.do',
			d_key: 'contproviderArr',
			d_id: 'cp_id',
			d_name: 'cp_name'
		});
    },
    onChoiceShop: function(btn, e) {
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('contruleform_window'),
			viewModel: this.mainViewModel,
			storeRef: 'contShopStore',
			store: 'app.store.ShopStore',
			win_title: '选择商铺',
			url: 'contshop/query.do',
			d_key: 'contshopArr',
			d_id: 's_id',
			d_name: 'name'
		});
    },
    onPriceRelaSelect : function(com,record){
    	var win = this.lookupReference('contruleform_window');

    	var form = win.down('form');
    	console.debug(form)
		var prightComp = form.query('textfield[name=price_right]');
		if(prightComp && prightComp.length > 0) {
			prightComp = prightComp[0]
		}
		console.debug(prightComp);
	
    	if(record.raw.value == 'between'){
    		prightComp.show();
    	}else{
    		prightComp.setValue('');
    		prightComp.hide();
    	}
    }
});
