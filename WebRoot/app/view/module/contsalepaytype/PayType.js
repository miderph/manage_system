Ext.define('app.view.module.contsalepaytype.PayType', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.contsalepaytype.PayTypeController'
    ],
    
	alias: 'widget.contSalesPayType',
	
	title: '管理员用户',
	controller: 'cont-sale-paytype',
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;

		var store = Ext.create('app.store.ContSalesPayTypeStore', {
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
	
	columns: [
	        {header: '支付名称', width: 200, sortable: false, dataIndex: 'name', locked: true},
	        {header: "ID",width: 100, sortable: true, dataIndex: 'id'},
	        {header: '支付类型', width: 100, sortable: true, dataIndex: 'pay_type',hidden: true},
	        {header: '支付类型', width: 100, sortable: true, dataIndex: 'pay_type_name'},
	        {header: '二维码', width: 100, sortable: true, dataIndex: 'has_qrcode',hidden: true},
	        {header: '二维码', width: 100, sortable: true, dataIndex: 'has_qrcode_name'},
	        {header: '订购电话', width: 150, sortable: true, dataIndex: 'service_hotline'},
	        {header: '订购电话', width: 150, sortable: true, dataIndex: 'service_hotline',hidden: true},
	        {header: '描述', width: 150, sortable: true, dataIndex: 'description'},
			{header: '图标', dataIndex: 'icon_url',	renderer: 'parseIconUrl', flex: 1}
	, {
        xtype: 'actioncolumn',
        header: '',
        locked: true,
        width: 70,
        sortable: false,
        menuDisabled: true,
        align: 'center',
        items: [{
        	iconCls: 'edit',
        	tooltip: '编辑',
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