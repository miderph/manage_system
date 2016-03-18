Ext.define('app.view.module.contapp.ContAppWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cont-app-window',
	
	reference: 'cont_app_window',
	
	uses: [
	       'app.store.ColumnShortCutStore',
	       'app.ux.form.MultiTextField',
	       'app.ux.form.MultiTypeUserGroup',
	       'app.ux.form.MultiImgGroup',
	       'app.ux.form.ColumnShortcutTextField',
	       'app.ux.form.PreviewFileField'],
	
    closable: true,
	closeAction: 'hide',
	resizable: true,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	scrollable: true,
	title: '添加资产',
//	y: 10,
	glyph: 0xf007,
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.callParent();
	},
	
	items:[{
		xtype: 'form',
		bodyPadding: 20,
		fieldDefaults: {
	        labelWidth: 120,
			labelAlign: 'right'
	    },
		items: [{
			xtype: 'multiimggroup',
	        title: '图片信息',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'imginfo',
			margin: '10, 2, 0, 0'
		}, {
			xtype: 'fieldset',
			title: '软件截图信息',
			collapsible: true,
			defaultType: 'textfield',
			fieldName: 'imginfo',
			hidden: true,
			bind: {
				hidden: '{!hasImgInfo}'
			},
			margin: '10, 2, 0, 0',
			items: [{
				xtype: 'contsalessnapshotspanel',
				name: 'contSalesSnapshotsPanel',
				bind: {
					hidden: '{!hasImgInfo}'
				}
			}]
		}, {
			xtype: 'textfield',
			name: 'cv_base_info',
			value: 1,
			bind: {
				//value: '{cv_base_info}'
			},
			hidden: true
		}, {
			xtype: 'fieldset',
	        title: '基本信息',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'baseinfo',
	        hidden: true,
	        bind: {
	        	hidden: '{!hasBaseInfo}'
	        },
	        items: [{
				xtype: 'textfield',
				name: 'c_id',
				hidden: true,
				value: '-1',
				listeners: {
					change: function(combo, newValue, oldValue, eOpts ) {
						var form = this.up('form');

						var contsalessnapshotspanel = form.down("contsalessnapshotspanel");
						contsalessnapshotspanel.reload(newValue);
					}
				}
			}, {
				xtype: 'textfield',
				name: 'link_url',
				hidden: true,
				value: ''
			}, {
				xtype: 'textfield',
				name: 'zip_download_url_show',
				hidden: true,
				value: '',
				listeners: {
					change: function(combo, newValue, oldValue, eOpts ) {
						var form = this.up('form');
						var btnDownload = form.query('button[fieldName=downloadHtmlZip]')[0];
						//console.log('oldValue: ' + oldValue+',newValue: ' + newValue);
							
						var show_url = newValue;
						if (show_url!=null && show_url!='') {
							btnDownload.setDisabled(false);
						}
						else{
							btnDownload.setDisabled(true);
						}
					}
				}
			},{
				xtype: 'container',
				layout: 'hbox',
				defaultType: 'textfield',
				items: [{
					xtype: 'checkboxfield',
					name: 'is_locked',
					fieldLabel: '基本信息锁定',
					value: 0,
					inputValue:1,
					flex: 1
				}, {
					name: 'cv_alias',
					fieldLabel: '软件别名',
					flex: 1,
					emptyText: '输入软件别名'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				defaultType: 'textfield',
				margin: '10, 0, 0, 0',
				items: [{
					name: 'c_name',
					fieldLabel: '*软件名称',
					allowBlank: false,
					flex: 1,
					emptyText: '输入软件名称'
				}, {
					xtype: 'datefield',
					name: 'active_time',
					allowBlank: false,
					flex: 1,
					fieldLabel: '*生效时间',
					value: new Date(),
					format: 'Y-m-d H:i:s'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'c_status',
					allowBlank: false,
					displayField: 's_name',
					valueField:'s_id',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{contStatusStore}'
					},
					fieldLabel: '*软件状态',
					emptyText: '选择软件状态'
				}, {
					xtype: 'datefield',
					name: 'deactive_time',
					allowBlank: false,
					flex: 1,
					fieldLabel: '*失效时间',
					value: Ext.Date.add(new Date(), Ext.Date.YEAR, 50),
					format: 'Y-m-d H:i:s'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'provider_id',
					allowBlank: false,
					displayField: 'cp_name',
					valueField:'cp_id',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{contProviderByAuthStore}'
					},
					fieldLabel: '*提供商',
					emptyText: '选择提供商'
				}, {
					xtype: 'datefield',
					name: 'ca_time',
					allowBlank: false,
					flex: 1,
					fieldLabel: '*上架时间',
					value: Ext.Date.add(new Date(), Ext.Date.YEAR, 50),
					format: 'Y-m-d H:i:s'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
		        	xtype: 'textfield',
		        	name: 'ca_staff',
		        	fieldLabel: '开发人员',
		        	emptyText: '',
		        	flex: 1
		        	//,anchor: '100%'
		        }, {
		        	xtype: 'textfield',
		        	name: 'ca_tags',
		        	fieldLabel: '软件标签',
		        	emptyText: '',
		        	flex: 1
		        	//,anchor: '100%'
		        }]
			}, {
						xtype: 'combobox',editable: false,

				margin: '10, 0, 0, 0',
				name: 'superscript_id',
				displayField: 'mar_s_contName',
				valueField:'mar_s_contId',
				queryMode: 'local',
				bind: {
					store: '{contSuperscriptStore}'
				},
				fieldLabel: '角标内容',
				anchor: '100%',
				listConfig: {
					itemTpl: ['<div data-qtip="{mar_s_contIntro}">{mar_s_contId} {mar_s_contName} {mar_s_contType} {mar_s_contProvider}</div>']
				}
			}, {
				xtype: 'textareafield',
				margin: '10, 0, 0, 0',
				anchor: '100%',
				name: 'c_description',
				fieldLabel: '应用描述',
				emptyText: '请对应用进行描述'
			}, {
	        	xtype: 'textfield',
				margin: '10, 0, 0, 0',
	        	name: 'cv_play_url',
	        	fieldLabel: '应用默认下载地址',
	        	emptyText: '',
				anchor: '100%'
	        },{
				xtype: 'multitypeusergroup',
		        title: '数据过滤（需选择测试组）',
		        collapsible: true,
		        defaultType: 'textfield',
		        fieldName: 'usergroupinfo',
				margin: '10, 2, 0, 0'
			}]
		}, {
			xtype: 'textfield',
			name: 'cv_img_info',
			value: 1,
			bind: {
				value: '{cv_img_info}'
			},
			hidden: true
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: 'onCancel'
	}],
	listeners: {
		resize: function(win, width, height) {
			var bodyH = Ext.getBody().getHeight();
			var y = (bodyH-height)/2;
			win.setY(y);
			win.setMaxHeight(bodyH-20);
		},
		show: function(win) {
//			var viewModel = this.getViewModel();
//			console.log('cw cv_base_info: ' + viewModel.get('cv_base_info'));
//	    	console.log('cw cv_img_info: ' + viewModel.get('cv_img_info'));
//			var hasBaseInfo = false;
//			var hasImgInfo = false;
//			if(viewModel.get('cv_base_info') == 1) {
//				hasBaseInfo = true;
//			}
//			if(viewModel.get('cv_img_info') == 1) {
//				hasImgInfo = true;
//			}
//			if (!hasBaseInfo && !hasImgInfo){
//				viewModel.set('cv_base_info', 1);
//				viewModel.set('cv_img_info', 1);
//				hasBaseInfo = true;
//				hasImgInfo = true;
//			}
//			console.log('cw hasBaseInfo: ' + hasBaseInfo);
//	    	console.log('cw hasImgInfo: ' + hasImgInfo);
//
//			this.query('textfield[name=c_name]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=c_status]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=provider_id]')[0].setDisabled(!hasBaseInfo);
//			this.query('datefield[name=active_time]')[0].setDisabled(!hasBaseInfo);
//			this.query('datefield[name=deactive_time]')[0].setDisabled(!hasBaseInfo);
//			
//			this.query('combobox[name=c_img_plat_group]')[0].setDisabled(!hasImgInfo);
//			this.query('datefield[name=c_img_active_time]')[0].setDisabled(!hasImgInfo);
//			this.query('datefield[name=c_img_deactive_time]')[0].setDisabled(!hasImgInfo);
		}
	}
});