Ext.define('app.view.module.contcheck.ContCheck', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.contcheck.ContCheckController',
       'app.view.module.contcheck.ContCheckModel'
    ],
    
	alias: 'widget.contCheck',
	
	uses: ['app.view.module.contcheck.ContCheckToolbar'],
	       
	title: '淘宝客商品',
	controller: 'cont-check',
	viewModel: {
    	type: 'cont-check'
	},
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;

		var store = Ext.create('app.store.ContCheckStore', {
			autoLoad: true
		});
		if(mainViewModel.get('contCheckStatusStore') == null) {
			mainViewModel.set('contCheckStatusStore', Ext.create('app.store.ContCheckStatusStore'));
		};

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
	        {header: '商品id', width: 100, sortable: false, dataIndex: 'code', locked: true},
	        {header: '商品名称', width: 100, sortable: true, dataIndex: 'name',locked:true},
	        {header: "商品分类",width: 100, sortable: true, dataIndex: 'classify',locked: true},
	        {header: "ID",width: 100, sortable: true, dataIndex: 'id'},
	        {header: '商品价格（元）', width: 80, sortable: true, dataIndex: 'price'},
	        {header: '状态', width: 40,  dataIndex: 'status'},
	        {header: '更新时间', width: 150, sortable: true, dataIndex: 'create_time'},
	        {header: '创建时间', width: 150, sortable: true, dataIndex: 'update_time',type: 'date', dateFormat: 'Y-m-d H:i:s'},
	        {header: '商品详细页', width: 150,  dataIndex: 'itemUrl'},
	        {header: '商品主图', width: 150,  dataIndex: 'icon'},
	        {header: '店铺名称', width: 100,  dataIndex: 'shop'},
	        {header: '商品月销量', width: 80,  dataIndex: 'sales_num'},
	        {header: '收入比率', width: 60,  dataIndex: 'bate'},
			{header: '阿里旺旺', width:100, dataIndex: 'wangwang'},
			{header: '淘宝客短链接', width: 150 ,dataIndex: 'taobaoke_url_short'},
			{header: '淘宝客链接',width: 150, dataIndex: 'taobaoke_url'}
			
	, {
        xtype: 'actioncolumn',
        header: '',
        locked: true,
        width: 70,
        sortable: false,
        menuDisabled: true,
        align: 'center',
        items: [{
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
    dockedItems: [{
        xtype: 'cont-check-toolbar',
        dock: 'top'
    }],
    tbar: [{
    	text: 'excel导入',
    	iconCls: 'add',
    	handler: 'onAddFromExcel'
    },{
    	text: '商品抓取',
    	iconCls: 'edit',
    	handler: 'onRobotSales'
    }],
	
	iconCls: 'icon-grid'
});