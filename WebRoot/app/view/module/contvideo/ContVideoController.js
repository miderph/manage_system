Ext.define('app.view.module.contvideo.ContVideoController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.cont-video',
    
    onAddBtn: function(btn, e) {
    	var viewModel = this.getView().getViewModel();
//    	viewModel.set('cv_base_info', 1);
//    	viewModel.set('cv_img_info', 1);
    	
    	var win = this.lookupReference('cont_video_window');
    	if (win){//不重用，解决上次新建时的残留图片预览
    		//Ext.destroy( win );//有问题
    		win = null;
    	}
    	if (!win) {
            win = Ext.create('app.view.module.contvideo.ContVideoWindow', {
            	viewModel: viewModel
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('新建');
    	var form = win.down('form');
    	form.reset();
    	
		form.query('combobox[name=c_status]')[0].setValue("11");//状态
		form.query('combobox[name=cv_prog_type]')[0].setValue("-1000");//节目类型：-1000未分类
		form.query('combobox[name=cv_cont_type]')[0].setValue("2");//视频类型：2 喜剧
		form.query('combobox[name=cv_region]')[0].setValue("0");//制片地区：0 大陆
		form.query('combobox[name=cv_quality_type]')[0].setValue("6");//视频清晰度：6 普清480i
		form.query('combobox[name=cv_has_volume]')[0].setValue("0");//是否剧集：0 没剧集
		form.query('textfield[name=cv_vol_total]')[0].setValue("1");//总集数：1

    	var search_provider = viewModel.get('search_provider');
    	if(search_provider != -10000) {
	    	form.query('combobox[name=provider_id]')[0].setValue(search_provider);
    	}
    	var search_status = viewModel.get('search_status');
    	if(search_status != -10000) {
	    	form.query('combobox[name=c_status]')[0].setValue(search_status);
    	}
    	var search_type = viewModel.get('search_type');
    	if(search_type != -10000) {
	    	form.query('combobox[name=c_type]')[0].setValue(search_type);
    	}
    	win.show();
    },
    
    onModify: function(tree, row, col, item, e, record) {
    	var viewModel = this.getView().getViewModel();
    	var me = this;
    	me.getView().mask('载入信息...');

//    	if(row != -1) {
//	    	viewModel.set('cv_base_info', 1);
//	    	viewModel.set('cv_img_info', 1);
//    	}
    	
    	var win = this.lookupReference('cont_video_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.contvideo.ContVideoWindow', {
            	viewModel: viewModel,
            	myviewModel: viewModel
            });
        	win.hide();
            this.getView().add(win);
        }
    	
    	win.setTitle('修改：' + record.raw.c_name);
    	var winForm = win.down('form');
    	winForm.reset();

    	if(viewModel.get('cv_img_info') == 1) {
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
    			type: '1',
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
    
    onModifyBtn: function(btn, e) {
    	var tree = this.getView();
    	
    	var record = tree.getSelectionModel().lastSelected;
		var rows = view.getSelectionModel().getSelection();// 返回值为Record数组

		if(rows.length != 1) {
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
			var viewModel = tree.getViewModel();
//	    	viewModel.set('cv_base_info', base);
//	    	viewModel.set('cv_img_info', img);
	    	this.onModify(tree, -1, -1, null, e, record);
		}
    },
    
    onDelete: function(tree, row, col, item, e, record) {
    	Ext.Msg.show({
		    title:'删除',
		    message: '视频名称：' + record.raw.c_name,
		    buttons: Ext.Msg.YESNO,
		    icon: Ext.Msg.QUESTION,
		    scope: this,
		    fn: function(btn) {
		        if (btn === 'yes') {
		        	this.delVideo(tree, record);
		        }
		    }
		});
    },
    
    onDeleteBtn: function(btn, e) {
    	var view = this.getView();
    	var record = view.getSelectionModel().lastSelected;
		var rows = view.getSelectionModel().getSelection();// 返回值为Record数组

		if(rows.length != 1) {
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
	    	this.onDelete(view, -1, -1, null, e, record);
		}
    },
    
    delVideo: function(grid, record) {
    	var p = this.getView();
    	p.mask('删除中...');
    	Ext.Ajax.request({
    		url: 'contvideo/del.do',
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
    onPreviewClick: function(tree, row, col, item, e, record) {
    	var me = this;
    	me.getView().mask('载入图片...');

    	Ext.Ajax.request({
    		url: 'img/query_by_cid.do?type=1',
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
    	
    	win.setTitle('“' + record.raw.c_name + '” 资产图片');
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
    onSetSuperscript: function(btn, e) {
    	var grid = this.getView();
    	var record = grid.getSelectionModel().lastSelected;
		var rows = grid.getSelectionModel().getSelection();// 返回值为Record数组

		if(rows.length != 1) {
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
			var win = this.lookupReference('setsuperscript_window');
	    	
	    	if (!win) {
	            win = Ext.create('app.view.module.contsales.SetSuperscriptWindow', {
	            	viewModel: grid.getViewModel(),
	            	grid: grid
	            });
	            grid.add(win);
	        }
	    	
	    	win.down('combobox').setValue(record.raw.superscript_id);
	    	
	    	win.show();
		}
		
    	
    },
    
    onDelSuperscript: function(btn, e) {
    	var view = this.getView();
    	var record = view.getSelectionModel().lastSelected;
		var rows = view.getSelectionModel().getSelection();// 返回值为Record数组

		if(rows.length != 1) {
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
			Ext.Msg.show({
			    title:'删除角标',
			    message: '资产名称：' + record.raw.c_name,
			    buttons: Ext.Msg.YESNO,
			    icon: Ext.Msg.QUESTION,
			    scope: this,
			    fn: function(btn) {
			        if (btn === 'yes') {
			        	this.delSuperscript(view, record);
			        }
			    }
			});
		}
    },
    
    delSuperscript: function(grid, record) {
    	var grid = this.getView();
    	grid.mask('删除中...');
    	Ext.Ajax.request({
    		url: 'contvideo/delete_cont_superscript.do',
    		async: true,
    		params: {
    			ids: record.raw.c_id
    		},
    		scope: this,
    		success: function(resp, opt) {
    			var respJson = Ext.JSON.decode(resp.responseText);
    			var msg = '删除角标成功'
    			if(!respJson.success) {
    				msg = '删除角标失败'
    			}
    			
    			grid.unmask();
    			Ext.toast({
     				html: msg,
     				title: '提示',
     				saveDelay: 10,
     				align: 'tr',
     				closable: true,
     				width: 200,
     				useXAxis: true,
     				slideInDuration: 500
     			});
				grid.getStore().reload();
    		}
    	});
    },
    
    videoFileDistinctStore: false,
    
    showVideoInfo: function(btn, e) {
    	var view = this.getView();
    	var record = view.getSelectionModel().lastSelected;
		var rows = view.getSelectionModel().getSelection();// 返回值为Record数组

		if(rows.length != 1) {
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
			var win = this.lookupReference('cs_videofilelist_window');
			
			if(!this.videoFileDistinctStore) {
				this.videoFileDistinctStore = Ext.create('app.store.VideoFileDistinctStore');
			}

	    	if (!win) {
	            win = Ext.create('app.view.module.contsales.ContVideoFileListWindow', {
	            	onController: this.getView().getController(),
	            	videoFileDistinctStore: this.videoFileDistinctStore,
	            	cs_id: record.raw.c_id,
	            	provider_id: record.raw.provider_id
	            });
	            this.getView().add(win);
	        }
	    	
	    	var store = win.down('gridpanel').getStore();
	    	store.proxy.extraParams.c_id = record.raw.c_id;
			store.proxy.extraParams.c_name = record.raw.c_name;
			
	    	win.show();
	    	store.removeAll();
			store.load();
		}
		
    },
    
    onAddCVI: function(vfdStore, csviStore, cs_id, provider_id) {
    	var win = this.lookupReference('contvideofile_addmod_window');
    	
    	if (!win) {
	    	var win = Ext.create('app.view.module.contsales.ContVideoFileAddModWindow', {
				videoFileDistinctStore: vfdStore,
				csviStore: csviStore
			});
	        this.getView().add(win);
    	}
		
		var form = win.down('form');
    	form.reset();
    	
    	var cidComp = form.query('textfield[name=c_id]');
    	if(cidComp && cidComp.length > 0) {
    		cidComp = cidComp[0];
    		cidComp.setValue(cs_id);
    	}
    	
    	var pComp = form.query('textfield[name=provider_id]');
    	if(pComp && pComp.length > 0) {
    		pComp = pComp[0];
    		pComp.setValue(provider_id);
    	}
    	
    	win.show();
    },
    
    onModifyCVI: function(vfdStore, csviStore, record) {
    	var win = this.lookupReference('contvideofile_addmod_window');
    	
    	if (!win) {
	    	var win = Ext.create('app.view.module.contsales.ContVideoFileAddModWindow', {
				videoFileDistinctStore: vfdStore,
				csviStore: csviStore
			});
	        this.getView().add(win);
    	}
		
		var form = win.down('form');
    	form.reset();
    	
    	console.log(record);
    	
    	form.loadRecord(record);
    	
    	win.show();
    },
    
    onDeleteCVI: function(grid, record) {
    	var p = this.getView();
    	p.mask('删除中...');
    	Ext.Ajax.request({
    		url: 'videofiles/delete.do',
    		async: true,
    		params: {
    			vf_id: record.raw.vf_id
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
    
    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('cont_video_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	
    	var url = 'contvideo/update.do';
    	if(values.c_id === "-1") {
    		url = 'contvideo/save.do';
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
	    			
	    			Ext.toast({
	     				html: action.result.msg,
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
    
    onCancel: function() {
    	var win = this.lookupReference('cont_video_window');
    	win.down('form').reset();
    	win.hide();
//        win.destroy();
    	win = null;
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
            	names.setValue(tArr.join('， '));
        		w.destroy();
        	},
        	onCancel: function(w) {
        		w.destroy();
        	}
        });
    	
        this.getView().add(win);
    	
    	win.show();
    },
    
    requestChoiceD: function(win, url, reader, cb) {
    	
    	win.mask('读取中...');
    	Ext.Ajax.request({
    		url: url,
    		async: true,
    		scope: this,
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
    
    onSearchContSales: function() {
    	var store = this.getView().getStore();
    	var viewModel = this.getView().getViewModel();
    	
    	store.proxy.extraParams.provider_id = viewModel.get('search_provider');
    	store.proxy.extraParams.c_status = viewModel.get('search_status');
    	store.proxy.extraParams.c_type = viewModel.get('search_type');
    	store.proxy.extraParams.c_name = viewModel.get('search_name');
    	store.load();
    	
    	viewModel.set('c_id', -1);
    },
    
    onProviderSelect: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_provider', record.raw.cp_id);
    },
    
    onTypeSelect: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_type', record.raw.s_id);
    },
    
    onStatusSelect: function(combo, record) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_status', record.raw.s_id);
    },
    
    onTitleChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_name', newValue);
    },
    
    onIdChange: function(tf, newValue, oldValue, eOpts) {
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_sales_no', newValue);
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
    	var store = 'contStatusStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    },
    
    parseTypeV: function(v) {
    	var str = '';
    	var store = 'contTypeStore';
    	
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
    
    parseProgTypeV: function(v) {
    	return this.parseValue(v, 'videoProgTypeStore', 'progTypeId', 'progTypeName');
    },
    
    parseContTypeV: function(v) {
    	return this.parseValue(v, 'videoContTypeStore', 'conttypeId', 'conttypeName');
    },
    
    parseRegionV: function(v) {
    	return this.parseValue(v, 'videoRegionStore', 'regionId', 'regionName');
    },
    
    parseQualityV: function(v) {
    	return this.parseValue(v, 'videoQualityStore', 'qualityTypeId', 'qualityTypeName');
    },
    
    parseHasVolumeV: function(v) {
    	return this.parseValue(v, 'videoHasVolumeStore', 'hasVolumeId', 'hasVolumeName');
    },
    
    parseAdTypeV: function(v) {
    	return this.parseValue(v, 'videoAdTypeStore', 'contAdTypeId', 'contAdTypeName');
    },
    
    onChoiceMac: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('cont_video_window'),
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
			mWin: this.lookupReference('cont_video_window'),
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
			mWin: this.lookupReference('cont_video_window'),
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
			mWin: this.lookupReference('cont_video_window'),
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
	/*
	内容类型：1文章,2音乐,3视频,4静态链接,5图片,6推荐小吃,7应用商店,8购物,9剧集综艺,10电影,
	11电视剧,12视频新闻,13广告,15直播,16 html,17 栏目
	*/
  	contChangCmpVisible: function (cont_video_type){
		if(cont_video_type==4 || cont_video_type==13 || cont_video_type==16){
//			contZipPanel_form.show();
//			button_url_link.show();
//			videofile_grid.hide();//视频播放地址列表
//            //修改fieldLabel   
//			if (cont_video_type==16)
//			   cont_video_play_url_form.setFieldLabel('关于预览地址:');
//			else
//			   cont_video_play_url_form.setFieldLabel('静态链接地址:');
//			changestatus(true);
		}else{
//			contZipPanel_form.hide();
//			button_url_link.hide();
//			videofile_grid.show();//视频播放地址列表
//	 		//修改fieldLabel   
//			cont_video_play_url_form.setFieldLabel('视频播放地址:');
//			changestatus(false);
		}
  	},
    onSearchReset: function () {
		var panel = this.getView();
		panel.query("combobox[name=c_s_provider]")[0].setValue("");
		panel.query("combobox[name=c_s_type]")[0].setValue("");
		panel.query("combobox[name=c_s_status]")[0].setValue("");
		panel.query("textfield[name=search_name]")[0].setValue("");
    	var viewModel = this.getView().getViewModel();
    	viewModel.set('search_provider', -10000);
    	viewModel.set('search_type', -10000);
    	viewModel.set('search_status', -10000);
    	viewModel.set('search_name', "");
	}
});
