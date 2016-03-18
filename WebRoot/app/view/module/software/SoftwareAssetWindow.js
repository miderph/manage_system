Ext.define('app.view.module.software.SoftwareAssetWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.software-asset-window',
	
	reference: 'software_asset_window',
	
	uses: [
	   'app.ux.form.MultiTypeUserGroup',
       'app.store.SoftwareStatusStore',
       'app.store.SoftwareForceStore'
    ],
	
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	scrollable: true,
	title: '新建版本信息',
	glyph: 0xf007,
	initComponent: function() {
		this.maxHeight = Ext.getBody().getHeight() - 20;
		this.callParent();
	},
	items:[{
		xtype: 'form',
		bodyPadding: 20,
		fieldDefaults: {
	        labelWidth: 100,
			labelAlign: 'right'
	    },
	    defaultType: 'textfield',
		items: [{
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'middle'
			},
			defaultType: 'textfield',
			items: [{
				name: 'id',
				value: '-1',
				fieldLabel: 'ID',
				readOnly: true,
				flex: 1
			}, {
				name: 'version_number',
				fieldLabel: '*版本号',
				allowBlank: false,
				flex: 1,
				emptyText: '输入版本号'
			}]
		}, {
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'middle'
			},
			defaultType: 'textfield',
			margin: '10, 0, 0, 0',
			items: [{
				name: 'add_plat',
				allowBlank: false,
				flex: 1,
				fieldLabel: '*平台信息',
				emptyText: '输入平台信息'
			}, {
				name: 'md5',
				allowBlank: false,
				flex: 1,
				fieldLabel: '*MD5值',
				emptyText: '输入软件MD5'
			}]
		}, {
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'middle'
			},
			margin: '10, 0, 0, 0',
			items: [{
						xtype: 'combobox',editable: false,

				name: 'status',
				displayField: 'status_name',
				valueField:'status_id',
				queryMode: 'local',
				allowBlank: false,
				flex: 1,
				store: {
	                type: 'software-status-store'
	            },
				fieldLabel: '*状态',
				emptyText: '选择状态'
			}, {
						xtype: 'combobox',editable: false,

				name: 'file_type',
				allowBlank: false,
				displayField: 's_name',
				valueField:'s_id',
				queryMode: 'local',
				flex: 1,
				value: '',
				bind: {
					store: '{softwareFileTypeStatusStore}'
				},
				fieldLabel: '*文件类型'
			}]
		}, {
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'middle'
			},
			margin: '10, 0, 0, 0',
			items: [{
				xtype: 'datefield',
				name: 'publish_time',
				fieldLabel: '*发布时间',
				format: 'Y-m-d H:i:s',
				flex: 1,
				allowBlank: false
			}, {
						xtype: 'combobox',editable: false,

				name: 'enforce_flag',
				displayField: 'force_name',
				valueField:'force_id',
				queryMode: 'local',
				value: '-10000',
				flex: 1,
				store: {
	                type: 'software-force-store'
	            },
				fieldLabel: '*是否强制升级',
				emptyText: '选择是否强制升级'
			}]
		}, {
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'middle'
			},
			margin: '10, 0, 0, 0',
			items: [{
				xtype: 'textareafield',
				name: 'software_info',
				allowBlank: false,
				flex: 1,
				fieldLabel: '*软件信息',
				emptyText: '输入软件信息'
			}, {
				xtype: 'textareafield',
				name: 'description',
				flex: 1,
				fieldLabel: '描述',
				emptyText: '输入描述'
			}]
		}, {
			xtype: 'multitypeusergroup',
	        title: '数据过滤（需选择测试组）',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'usergroupinfo',
			margin: '10, 2, 0, 0'
		}, {
			xtype: 'fieldset',
	        title: '普通升级地址',
	        defaultType: 'textfield',
	        defaults: {
	            anchor: '100%'
	        },
	        margin: '10, 0, 10, 0',
	        items: [{
	        	fieldLabel: '普通升级地址id',
	        	name: 'url_general_id',
	        	hidden: true,
	        	value: '-1'
	        }, {
//	        	fieldLabel: '*升级地址',
	        	fieldLabel: '升级地址',
	        	margin: '10, 0, 10, 0',
	        	name: 'update_url_general',
	        	emptyText: '输入升级地址'/*,
	        	allowBlank: false,  */
	        }]
		}, {
			xtype: 'fieldset',
	        title: '360云盘升级地址',
	        defaultType: 'textfield',
	        flex: 1,
	        items: [{
	        	name: 'url_360_id',
	        	hidden: true,
	        	value: '-1'
	        }, {
				xtype: 'container',
				layout: {
					type: 'hbox',
					align: 'middle'
				},
				margin: '10, 0, 0, 0',
				defaultType: 'textfield',
				items: [{
//					fieldLabel: '*升级地址',
					fieldLabel: '升级地址',
		        	name: 'update_url_360',
		        	emptyText: '输入360升级地址',
		        	flex: 1/*,
		        	allowBlank: false*/
				}, {
					fieldLabel: '360分享密码',
		        	name: 'share_password_360',
		        	flex: 1,
		        	emptyText: '输入360分享密码'
				}]
			}, {
				xtype: 'container',
				layout: {
					type: 'hbox',
					align: 'middle'
				},
				margin: '10, 0, 10, 0',
				items: [{
					xtype: 'datefield',
					name: 'temp_url_expire_time',
					fieldLabel: '临时地址过期时间',
					flex: 1,
					readOnly: true,
				}, {
					xtype: 'textfield',
					name: 'upgrate_temp_url',
					readOnly: true,
					flex: 1,
					fieldLabel: '升级临时地址'
				}]
			}]
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('software-asset-window').hide();
		}
	}],
	listeners: {
		resize: function(win, width, height) {
			var bodyH = Ext.getBody().getHeight();
			var y = (bodyH-height)/2;
			win.setY(y);
			win.setMaxHeight(bodyH-20);
		}
	}
});