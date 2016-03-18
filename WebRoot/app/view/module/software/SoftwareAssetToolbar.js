Ext.define('app.view.module.software.SoftwareAssetToolbar', {
	extend: 'Ext.toolbar.Toolbar',
    
	alias: 'widget.software-asset-toolbar',
	
	uses: [
       'app.store.SoftwareStatusStore',
       'app.store.SoftwareForceStore',
    ],
	
	initComponent: function() {
		var viewModel = this.up('softwareAsset').getViewModel();
		
		this.items = [{
        	xtype: 'form',
    		bodyPadding: 5,
    		fieldDefaults: {
    	        labelWidth: 100,
    			labelAlign: 'right'
    	    },
    	    items: [{
    	    	xtype: 'container',
    			layout: 'hbox',
    			defaultType: 'textfield',
    			items: [{
    				name: 's_v_num',
    				fieldLabel: '版本号',
    				flex: 1
    			}, {
    				name: 's_info',
    				fieldLabel: '软件信息',
    				flex: 1
    			}, {
    				name: 's_plat',
    				fieldLabel: '平台信息',
    				flex: 1
    			}]
    	    }, {
    	    	xtype: 'container',
    			layout: 'hbox',
    			defaultType: 'combobox',
    			margin: '8, 0, 8, 0',
    			items: [{
    				name: 's_enforce',
    				displayField: 'force_name',
    				valueField:'force_id',
    				queryMode: 'local',
    				value: '-10000',
    				flex: 1,
    				store: {
    	                type: 'software-force-store'
    	            },
    				fieldLabel: '是否强制升级'
    			}, {
    						xtype: 'combobox',editable: false,

    				name: 's_tg_id',
    				fieldLabel: '测试组',
    	    		flex: 1,
    				displayField: 'ug_name',
    				valueField: 'ug_id',
    				queryMode: 'local',
    				value: '',
    				bind: {
    					store: '{testGroupAllStore}'
    				}
    			}, {
    						xtype: 'combobox',editable: false,

    				name: 's_file_type',
    				fieldLabel: '文件类型',
    	    		flex: 1,
    				displayField: 's_name',
    				valueField: 's_id',
    				queryMode: 'local',
    				value: '-10000',
    				bind: {
    					store: '{softwareFileTypeStatusStore}'
    				}
    			}]
    	    }, {
    	    	xtype: 'container',
    			layout: 'hbox',
    			items: [{
    	            		xtype: 'combobox',editable: false,

    				name: 's_status',
    				displayField: 'status_name',
    				valueField:'status_id',
    				queryMode: 'local',
    				flex: 1,
    				store: {
    	                type: 'software-status-store'
    	            },
    				fieldLabel: '状态'
    			}, {
    				xtype: 'textfield',
    				name: 's_desc',
    				fieldLabel: '描述',
    				flex: 1
    			}, {
    				xtype: 'container',
    				flex: 1,
    				layout: 'hbox',
        			items: [{
        				xtype: 'button',
        				text: '搜索',
        				glyph: 0xf002,
        				width: 60,
        				margin: '0, 20, 0, 80',
        				handler: function() {
        					var values = this.up('form').getValues();
        					var store = this.up('softwareAsset').getStore();
        					store.getProxy().extraParams = {
        						version_number: values.s_v_num,
        						software_info: values.s_info,
        						plat: values.s_plat,
        						enforce_flag: values.s_enforce,
        						usergroup_id: values.s_tg_id,
        						file_type: values.s_file_type,
        						description: values.s_desc,
        						status: values.s_status
        					};
        					
        					store.load({
        						params:{
        							start: 0,
        							limit: store.getPageSize()
        						}
        					});
        				}
        			}, {
        				xtype: 'button',
        				text: '重置',
        				glyph: 0xf112,
        				width: 60,
        				handler: function() {
        					this.up('form').reset();
        				}
        			}]
    			}]
    	    }]
        }];
		
		this.callParent();
	},
	
	dockedItems: [{
        xtype: 'toolbar',
        dock: 'top',
        items: []
    }]
});