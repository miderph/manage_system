Ext.define('app.view.module.column.ColumnWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.column-window',
	requires: [
	           'app.view.module.column.ColumnController',
	           'app.view.module.column.ColumnModel'
	        ],
	reference: 'column_window',
	
	uses: [
	       'app.store.ColumnShortCutStore',
	       'app.ux.form.MultiTypeUserGroup',
	       'app.ux.form.MultiImgGroup',
	       'app.ux.form.ColumnShortcutTextField',
	       'app.ux.form.PreviewFileField'],
	
    closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	scrollable: true,
	title: '添加栏目',
//	y: 10,
	glyph: 0xf007,
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.callParent();
	},
	items:[{
		xtype: 'form',
		bodyPadding: 20,
//		referenceHolder: true,
		fieldDefaults: {
	        labelWidth: 120,
			labelAlign: 'right'
	    },
		items: [{
			xtype: 'textfield',
			name: 'column_base_info',
			value: 1,
			bind: {
				//value: '{column_base_info}'
			},
		    hidden: true
		}, {
			xtype: 'fieldset',
	        title: '基本信息',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'baseinfo',
//			disabled: true,
	        //hidden: true,
	        bind: {
//	        	disabled: '{!hasBaseInfo}',
//	        	hidden: '{!hasBaseInfo}'
	        },
	        items: [{
				name: 'c_id',
				hidden: true,
				value: '-1'
			}, {
				name: 'c_pid',
				hidden: true,
				value: '0'
			}, {
				xtype: 'container',
				layout: 'hbox',
				defaultType: 'textfield',
				items: [{
					name: 'title',
					fieldLabel: '*栏目名称',
					allowBlank: false,
					flex: 1,
					emptyText: '输入栏目名称'
				}, {
					name: 'version',
					fieldLabel: '栏目版本',
					flex: 1,
					emptyText: '输入栏目版本'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'status',
					allowBlank: false,
					displayField: 's_name',
					valueField:'s_id',
					queryMode: 'local',
					flex: 1,
					value: '1',
					bind: {
						store: '{columnStatusStore}'
					},
					fieldLabel: '*栏目状态',
					emptyText: '选择栏目状态'
				},  {
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

					name: 'struct_type',
					allowBlank: false,
					displayField: 's_name',
					valueField:'s_id',
					queryMode: 'local',
					flex: 1,
					value: '3',
					bind: {
						store: '{columnTypeStore}'
					},
					fieldLabel: '*栏目类型',
					emptyText: '选择栏目类型'
				}, {
					xtype: 'textfield',
					name: 'order_num',
					flex: 1,
					value: 0,
					allowBlank: false,
					fieldLabel: '*排序号',
					emptyText: '输入排序号'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'resource_type',
					allowBlank: false,
					displayField: 's_name',
					valueField:'s_id',
					queryMode: 'local',
					flex: 1,
					value: 'CONTENT',
					bind: {
						store: '{columnResTypeStore}'
					},
					fieldLabel: '*栏目下资产类型',
					emptyText: '选择栏目下资产类型'
				}, {
					xtype: 'checkbox',
					name: 'is_autoplay',
					flex: 1,
					value: 0,
					fieldLabel: '支持自动播放'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				defaultType: 'combobox',
				items: [{
					name: 'act_type',
					allowBlank: false,
					displayField: 's_name',
					valueField:'s_id',
					queryMode: 'local',
					flex: 1,
					value: 'ShowEntries',
					bind: {
						store: '{columnActTypeStore}'
					},
					fieldLabel: '*栏目动作类型',
					emptyText: '选择栏目动作类型',
					listeners: {
						change: function(combo, newValue, oldValue, eOpts ) {
							var islocal = this.up('form').query('combobox[name=islocal]');
							if(islocal.length > 0) {
								islocal = islocal[0];
								if(newValue == 'getChannel') {
									islocal.setDisabled(false);
								} else {
									islocal.setDisabled(true);
								}
							}
							
							var hyperlink = this.up('form').query('textfield[name=link_url]');
							if(hyperlink.length > 0) {
								hyperlink = hyperlink[0];
								if(newValue == 'Hyperlink') {
									hyperlink.setDisabled(false);
								} else {
									hyperlink.setDisabled(true);
								}
							}
						}
					}
				}, {
					name: 'islocal',
					allowBlank: false,
					displayField: 'islocalName',
					valueField:'islocalId',
					queryMode: 'local',
					reference: 'c_w_is_local',
					disabled: true,
					flex: 1,
					bind: {
						store: '{isLocalStore}'
					},
					fieldLabel: '*是否地方频道栏目',
					emptyText: '选择是否地方频道栏目'
				}]
			}, {
				name: 'link_url',
				fieldLabel: '*超链接地址',
				allowBlank: false,
				margin: '10, 0, 0, 0',
				reference: 'c_w_link_url',
				emptyText: '请输入链接地址',
				anchor: '100%',
				disabled : true
			}, {
						xtype: 'combobox',editable: false,

				name: 'is_shortcut',
				displayField: 'is_shortcut_name',
				valueField:'is_shortcut_id',
				queryMode: 'local',
				margin: '10, 0, 0, 0',
				flex: 1,
				value: 0,
				store: {
					type: 'column-shortcut-store'
				},
				fieldLabel: '是否快捷方式',
				emptyText: '是否快捷方式',
				listeners: {
					change: function(combo, newValue, oldValue, eOpts ) {
						var shortcut = this.up('form').down('column-shortcut-textfield');
						var btn = shortcut.down('button');
						var text = shortcut.down('container').down('textfield');
						text.setValue("");
						//console.log('oldValue: ' + oldValue+',newValue: ' + newValue);

						var v = newValue;
						if(v == '1') {//内容链接
							text.setDisabled(false);
							btn.setDisabled(false);
							btn.setText('选择内容');
							btn.rTag = 'con';
						} else if(v == '2') {//栏目链接
							var win = this.up('column-window');
							var viewModel = win.getViewModel();

							var siteid = viewModel.get('column_site');
					    	if (oldValue != null && siteid == 0){//
					    		window.alert("栏目链接站点不允许有栏目快捷方式!");
					    		text.setDisabled(true);
								btn.setDisabled(true);
							}
					    	else{
					    		text.setDisabled(false);
								btn.setDisabled(false);
					    	}
							//btn.setDisabled(false);
							btn.setText('选择栏目');
							btn.rTag = 'col';
						} else if(v == '3') {//分渠道活动广告栏目,对客户端是内容连接，协议端需取编排的一个内容
				    		window.alert("分渠道活动广告栏目,将作为一个内容返回（栏目下不能有子栏目，栏目的图片将被忽略），返回的内容取自本栏目下编排的内容，按测试组过滤内容后，仍有多个内容时取第一个内容，没有内容时将不返回!");
							text.setDisabled(true);
							btn.setDisabled(true);
							btn.setText('选择');
							btn.rTag = 'no';
						} else {
							text.setDisabled(true);
							btn.setDisabled(true);
							btn.setText('选择');
							btn.rTag = 'no';
						}
					}
				}
			}, {
				xtype: 'column-shortcut-textfield',
				valueName: 'shortcut_contid',
				textName: 'shortcut_linktoname',
				fieldLabel: '快捷方式链接至',
				emptyText: '请选择...',
				margin: '10, 0, 0, 0',
				onHandler: 'onChoiceShortcut'
			}, {
				xtype: 'multitypeusergroup',
		        title: '数据过滤（需选择测试组）',
		        collapsible: true,
		        defaultType: 'textfield',
		        fieldName: 'usergroupinfo',
				margin: '10, 2, 0, 0'
			}]
		}, {
			xtype: 'textfield',
			name: 'column_img_info',
			value: 1,
			bind: {
				//value: '{column_img_info}'
			},
			hidden: true
		}, {
			xtype: 'multiimggroup',
	        title: '图片信息',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'imginfo',
			margin: '10, 2, 0, 0'
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
			var viewModel = this.getViewModel();
//			console.log('cw column_base_info: ' + viewModel.get('column_base_info'));
//	    	console.log('cw column_img_info: ' + viewModel.get('column_img_info'));
//			var hasBaseInfo = false;
//			var hasImgInfo = false;
//			if(viewModel.get('column_base_info') == 1) {
//				hasBaseInfo = true;
//			}
//			if(viewModel.get('column_img_info') == 1) {
//				hasImgInfo = true;
//			}
//			if (!hasBaseInfo && !hasImgInfo){
//				viewModel.set('column_base_info', 1);
//				viewModel.set('column_img_info', 1);
//				hasBaseInfo = true;
//				hasImgInfo = true;
//			}
//			console.log('cw hasBaseInfo: ' + hasBaseInfo);
//	    	console.log('cw hasImgInfo: ' + hasImgInfo);

//			this.query('textfield[name=title]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=status]')[0].setDisabled(!hasBaseInfo);
//			this.query('textfield[name=order_num]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=struct_type]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=provider_id]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=resource_type]')[0].setDisabled(!hasBaseInfo);
//			this.query('datefield[name=active_time]')[0].setDisabled(!hasBaseInfo);
//			this.query('datefield[name=deactive_time]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=act_type]')[0].setDisabled(!hasBaseInfo);
//			
//			this.query('combobox[name=c_img_plat_group]')[0].setDisabled(!hasImgInfo);
//			this.query('datefield[name=c_img_active_time]')[0].setDisabled(!hasImgInfo);
//			this.query('datefield[name=c_img_deactive_time]')[0].setDisabled(!hasImgInfo);
		}
	}
});