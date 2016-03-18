Ext.define('app.view.module.stbprefix.StbPrefix', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.stbprefix.StbPrefixController'
    ],
    
	alias: 'widget.stbPrefix',
	
	title: '管理员用户',
	controller: 'stbprefix',
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var store = Ext.create('app.store.StbPrefixStore', {
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
		header: '前缀',
		dataIndex: 'code',
		flex: 1,
		width: 200,
		locked: true
	}, {
		header: 'ID',
		dataIndex: 'sp_id',
		align: 'center',
		width: 100
	}, {
		header: '提供商',
		dataIndex: 'provider_name',
		width: 180
	}, {
		header: '站点',
		dataIndex: 'site_name',
		width: 180
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
		header: '操作',
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
//    		
//    	}
    },
    
    tbar: [{
    	text: '添加',
    	iconCls: 'add',
    	handler: 'onAddBtn'
    }],
	
	iconCls: 'icon-grid'
});