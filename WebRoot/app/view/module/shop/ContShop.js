Ext.define('app.view.module.shop.ContShop', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.shop.ContShopController'
    ],
    
	alias: 'widget.contShop',
	
	title: '管理员用户',
	controller: 'cont-shop',
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		var store = Ext.create('app.store.ShopStore', {
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

	selModel: {selType: 'checkboxmodel',mode: 'SINGLE'},
	columns: [{
		header: '名称',
		dataIndex: 'name',
		flex: 1,
		width: 200,
		locked: true
	}, {
		header: 'ID',
		dataIndex: 's_id',
		width: 80
	}, {
		header: '信用度',
		dataIndex: 'credit',
		flex: 1
	}, {
		header: '促销信息',
		dataIndex: 'hot_info',
		flex: 1
	}, {
		header: '简介',
		dataIndex: 'intro',
		flex: 1
	}, {
		header: '图标',
		dataIndex: 'icon_url',
		renderer: 'parseIconUrl',
		width: 40
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
        header: '',
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
    }, '-', {
    	iconCls: 'edit',
    	text: '分渠道促销信息',
    	handler: 'onHotInfoByChannel'
    }],
	
	iconCls: 'icon-grid'
});