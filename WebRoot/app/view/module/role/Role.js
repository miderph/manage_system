Ext.define('app.view.module.role.Role', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.role.RoleController'
    ],
    
	alias: 'widget.roles',
	
	title: '角色用户',
	controller: 'role',
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		var store = Ext.create('app.store.RoleStore', {
			autoLoad: true
		});

		mainViewModel.set('roleStore_for_refresh', store);
		
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
		header: '角色名称',
		dataIndex: 'name',
		flex: 1,
		width: 200,
		locked: true
	}, {
		header: 'ID',
		dataIndex: 'r_id',
		width: 80
	}, {
		header: '站点',
		dataIndex: 'site_names',
		width: 300
	}, {
		header: '提供商',
		dataIndex: 'provider_names',
		width: 300
	}, {
		header: '模块',
		dataIndex: 'module_names',
		width: 300
	}, {
		header: '链接栏目',
		dataIndex: 'menu_names',
		width: 300
	}, {
		header: '测试组',
		dataIndex: 'group_names',
		width: 300
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '创建时间',
		dataIndex: 'create_time',
		align: 'center',
		width: 150
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '修改时间',
		dataIndex: 'modify_time',
		align: 'center',
		width: 150
	}, {
        xtype: 'actioncolumn',
        locked: true,
        width: 80,
        sortable: false,
        menuDisabled: true,
        align: 'center',
        items: [' ', {
        	iconCls: 'edit',
        	tooltip: '修改',
            handler: 'onModify'
        }, ' ', {
        	iconCls:'delete',
        	tooltip: '删除',
            handler: 'onRemove'
        }, ' ']
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
			'<p><b>站点:</b> {site_names:this.formatNames}</p>',
			'<p><b>提供商:</b> {provider_names:this.formatNames}</p>',
			'<p><b>模块:</b> {module_names:this.formatNames}</p>', {
			formatNames: function(v) {
				var arr = v.split(',');
				return arr.join('&nbsp;&nbsp;');
			}
		})
	}],
    
    tbar: [{
    	text: '添加',
    	iconCls: 'add',
    	handler: 'onAddBtn'
    }],
	
	iconCls: 'icon-grid'
});