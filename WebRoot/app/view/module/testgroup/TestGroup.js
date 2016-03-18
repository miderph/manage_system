Ext.define('app.view.module.testgroup.TestGroup', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.testgroup.TestGroupController',
       'app.view.module.testgroup.TestGroupModel'
    ],
    
	alias: 'widget.userTestGroupMgr',
	
	title: '热词管理',
	controller: 'testgroup',
    viewModel: {
        type: 'testgroup'
    },
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('testGroupStatusStore') == null) {
			mainViewModel.set('testGroupStatusStore', Ext.create('app.store.TestGroupStatusStore'));
		}
		
		if(mainViewModel.get('stbModelStore') == null) {
			mainViewModel.set('stbModelStore', Ext.create('app.store.StbModelStore'));
		}
		if(mainViewModel.get('contChannelStoreByApk') == null) {
			mainViewModel.set('contChannelStoreByApk', Ext.create('app.store.ChannelStoreByApk'));
		}

		var store = Ext.create('app.store.TestGroupStore', {
			autoLoad: true
		});
		
		var pageSize = store.getPageSize();
		
		Ext.apply(this, {
            store: store,
            bbar: {
				xtype: 'pagingtoolbar',
		        pageSize: pageSize,
		        store: store,
		        displayInfo: true,
		        plugins: Ext.create('app.ux.ProgressBarPager')
            },
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            }
        });
		
        this.callParent();
	},
	
	columns: [{
		header: '测试组名称',
		dataIndex: 'name',
		flex: 1,
		width: 200,
		locked: true
	}, {
		header: 'ID',
		dataIndex: 'ug_id',
		width: 100
	}, {
		header: '类型',
		dataIndex: 'type',
		renderer: 'parseTgTypeV',
		width: 180
	}, {
		header: '值集',
		dataIndex: 'raw_value',
		width: 260
	}, {
		header: '简介',
		dataIndex: 'intro',
		flex: 1,
		width: 100
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '创建时间',
		dataIndex: 'create_time',
		width: 150
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '修改时间',
		dataIndex: 'modify_time',
		width: 150
	}, {
        xtype: 'actioncolumn',
        locked: true,
        width: 70,
        sortable: false,
        menuDisabled: true,
        align: 'center',
        items: [{
        	iconCls: 'edit',
        	tooltip: '修改',
            handler: 'onModify'
        }, ' ', {
        	iconCls:'delete',
        	tooltip: '删除',
            handler: 'onRemove'
        }]
    }],
    
    listeners: {
//    	itemdblclick: function(grid, record, item, index, e, eOpts) {
//    		this.getController().onModify(this, -1, -1, null, e, record);
//    	}
    },
    

	
	plugins: [{
		ptype: 'rowexpander',
		expandOnDblClick: false,
		rowBodyTpl: new Ext.XTemplate(
			'<p><b>值集:</b> {raw_value}</p>', {
				formatInfo: function(v) {
					/*var arr = v.split(',');
					var result = "";
					var one = "";
					for(i=0; i<arr.length; i++) {
						one += arr[i] + '&nbsp;&nbsp;';
						if(i%10 == 0) {
							one += arr[i];
							result += '<div>' + one + '</div>'
							one = '';
						}
					}
					
					return result;*/
				}
			})
	}],
    
    dockedItems: [{
    	xtype: 'toolbar',
        dock: 'top',
        items: [{
        			xtype: 'combobox',editable: false,

			name: 'search_tg_type',
			fieldLabel: '类型',
			labelWidth: 56,
    		maxWidth: 200,
    		flex: 1,
			labelAlign: 'right',
			displayField: 's_name',
			valueField: 's_id',
			queryMode: 'local',
			value: '-10000',
			bind: {
				store: '{testGroupStatusStore}'
			},
			listeners: {
				select: 'onTgTypeSelect'
			}
    	}, {
        	xtype: 'textfield',
        	name: 'search_tg_name',
			fieldLabel: '测试组名',
    		labelWidth: 60,
    		maxWidth: 200,
			flex: 1,
			labelAlign: 'right',
    		listeners: {
    			change: 'onTgNameChange'
    		}
    	}, {
    		xtype: 'button',
    		text: '搜 索',
    		disabled: true,
    		bind: {
    			disabled: '{!canSearch}'
    		},
    		glyph: 0xf002,
    		width: 60,
    		margin: '0, 0, 0, 30',
    		handler: 'onSearchVP'
    	}]
    }],
    
    tbar: [{
    	text: '添加',
    	iconCls: 'add',
    	handler: 'onAddBtn'
    }],
	
	iconCls: 'icon-grid'
});