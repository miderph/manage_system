Ext.define('app.view.module.hotword.HotWord', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.hotword.HotWordController',
       'app.view.module.hotword.HotWordModel'
    ],

	alias: 'widget.hotWord',
	
	title: '热词管理',
	controller: 'hotword',
    viewModel: {
        type: 'hotword'
    },
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('siteAllStore') == null) {
			mainViewModel.set('siteAllStore', Ext.create('app.store.SiteAllStore'));
		}
		
		var store = Ext.create('app.store.HotWordStore');
		
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
    selModel: {selType:'checkboxmodel', mode: 'SIMPLE'},
	columns: [{
		header: '热词',
		dataIndex: 'hotword',
		flex: 1,
		width: 200,
		locked: true
	}, {
		header: 'ID',
		dataIndex: 'hw_id',
		width: 100
	}, {
		header: '站点',
		dataIndex: 'site_id',
		renderer: 'parseSiteV',
		width: 180
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
	}],
    
    listeners: {
//    	itemdblclick: function(grid, record, item, index, e, eOpts) {
//    		//this.getController().onModify(this, -1, -1, null, e, record);
//    	}
    },
    
    dockedItems: [{
    	xtype: 'toolbar',
        dock: 'top',
        items: [{
        			xtype: 'combobox',editable: false,

			name: 'search_site',
			fieldLabel: '站点',
			labelWidth: 56,
    		maxWidth: 200,
    		flex: 1,
			labelAlign: 'right',
			displayField: 's_name',
			valueField: 's_id',
			queryMode: 'local',
			value: '-10000',
			bind: {
				store: '{siteAllStore}'
			},
			listeners: {
				select: 'onSiteSelect'
			}
    	}, {
        	xtype: 'textfield',
        	name: 'search_hotword',
			fieldLabel: '关键字',
			emptyText : '请输入搜索关键字...',
    		labelWidth: 60,
    		maxWidth: 200,
			flex: 1,
			labelAlign: 'right',
    		listeners: {
    			change: 'onHotWordChange'
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
		bind: {
			disabled: '{!is_selected_site_0}'
		},
    	handler: 'onAddBtn'
    },{
    	text: '修改',
    	iconCls: 'edit',
		bind: {
			disabled: '{!is_selected_site_0}'
		},
    	listeners: {
    		click: function(btn) {
    			var grid = btn.up('hotWord');
				var lastSelected = grid.getSelectionModel().lastSelected;
				if(lastSelected) {
					grid.getController().onModify(grid, -1, -1, null, null, lastSelected);
				}
    		}
    	}
    }, {
    	text: '删除',
    	iconCls:'delete',
		bind: {
			disabled: '{!is_selected_site_0}'
		},
    	listeners: {
    		click: function(btn) {
    			var grid = btn.up('hotWord');
    			var gridselectrows = grid.getSelectionModel().getSelection();
    			if(gridselectrows.length!=1) {
    				Ext.MessageBox.alert('提示', '请选中一个热词删除！');
    				return ;
    			}
				var lastSelected = grid.getSelectionModel().lastSelected;
				if(lastSelected) {
					grid.getController().onRemove(grid, -1, -1, null, null, lastSelected, '删除热词');
				}
    		}
    	}
    },'-', {
    	text: '绑定热词到此站点',
    	iconCls: 'add',
		bind: {
			disabled: '{!can_rela}'
		},
    	handler: 'onRelaAddBtn'
    },{
    	text: '解绑选中的热词',
    	iconCls: 'delete',
		bind: {
			disabled: '{!can_delete_rela}'
		},
    	listeners: {
    		click: function(btn) {
    			var grid = btn.up('hotWord');
    			grid.getController().onRelaDel(grid);
    		}
    	}
    }],
	
	iconCls: 'icon-grid'
});