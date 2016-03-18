Ext.define('app.view.module.operator.Operator', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.operator.OperatorController'
    ],
    
	alias: 'widget.operators',
	
	title: '管理员用户',
	controller: 'operator',
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('operatorStatusStore') == null) {
			mainViewModel.set('operatorStatusStore', Ext.create('app.store.OperatorStatusStore'));
		}
		
		var store = Ext.create('app.store.OperatorStore', {
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
		header: '名称',
		dataIndex: 'name',
		flex: 1,
		width: 200,
		locked: true
	}, {
		header: '状态',
		dataIndex: 'status',
		renderer: 'parseStatusV',
		align: 'center',
		width: 60
	}, {
		header: 'ID',
		dataIndex: 'o_id',
		width: 80
	}, {
		header: '角色',
		dataIndex: 'role_names',
		width: 180
	}, {
		header: '手机号码',
		dataIndex: 'phone',
		align: 'center',
		width: 100
	}, {
		header: '电话号码',
		dataIndex: 'tel',
		align: 'center',
		width: 100
	}, {
		header: '邮箱地址',
		dataIndex: 'email',
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
		header: '简介',
		dataIndex: 'intro',
		flex: 1
	}, {
        xtype: 'actioncolumn',
        locked: true,
        width: 100,
        sortable: false,
        menuDisabled: true,
        align: 'center',
        items: [{
        	iconCls: 'edit',
        	tooltip: '编辑用户',
        	isDisabled: function() {
        		return !this.up('app-main').getViewModel().isAdmin();
        	},
            handler: 'onModify'
        }, ' ', {
        	iconCls: 'info',
        	tooltip: '修改密码',
            handler: 'onModifyPsd'
        }, ' ', {
        	iconCls:'delete',
            tooltip: '删除用户',
        	isDisabled: function() {
        		return !this.up('app-main').getViewModel().isAdmin();
        	},
            handler: 'onRemove'
        }, ' ']
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
    	hidden: true,
    	bind: {
			hidden: '{!isAdmin}'
		},
    	handler: 'onAddBtn'
    }],
	
	iconCls: 'icon-grid'
});