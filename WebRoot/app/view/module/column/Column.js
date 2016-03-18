Ext.define('app.view.module.column.Column', {
	extend: 'Ext.tree.Panel',
	requires: [
       'app.view.module.column.ColumnController',
       'app.view.module.column.ColumnModel',
       'app.view.module.column.ColumnWindow'
    ],
    
    uses: ['app.view.module.column.ColumnToolbar'],
    
	alias: 'widget.menuStructure',
	
	title: '栏目管理',
	controller: 'column',
    viewModel: {
        type: 'column'
    },
	
	frame: true,
//	useArrows: true,
    rootVisible: false,
    columnLines: true,
    
    dockedItems: [{
        xtype: 'column-toolbar',
        dock: 'top'
    }],
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('siteAllStore') == null) {
			mainViewModel.set('siteAllStore', Ext.create('app.store.SiteAllStore'));
		}
		
		if(mainViewModel.get('columnStatusStore') == null) {
			mainViewModel.set('columnStatusStore', Ext.create('app.store.ColumnStatusStore'));
		}
		
		if(mainViewModel.get('contProviderByAuthStore') == null) {
			mainViewModel.set('contProviderByAuthStore', Ext.create('app.store.ContProviderByAuthStore'));
		}
		
		if(mainViewModel.get('columnTypeStore') == null) {
			mainViewModel.set('columnTypeStore', Ext.create('app.store.ColumnTypeStore'));
		}
		
		if(mainViewModel.get('columnActTypeStore') == null) {
			mainViewModel.set('columnActTypeStore', Ext.create('app.store.ColumnActTypeStore'));
		}
		
		if(mainViewModel.get('columnResTypeStore') == null) {
			mainViewModel.set('columnResTypeStore', Ext.create('app.store.ColumnResTypeStore'));
		}
		if(mainViewModel.get('contTypeStore') == null) {
			mainViewModel.set('contTypeStore', Ext.create('app.store.ContTypeStore'));
		}
		if(mainViewModel.get('contStatusStore') == null) {
			mainViewModel.set('contStatusStore', Ext.create('app.store.ContStatusStore'));
		}
		var store = Ext.create('app.store.ColumnStore');
		
		Ext.apply(this, {
            store: store,
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            }
        });
		
        this.callParent();
	},
	
	columns: [{
		xtype: 'treecolumn',
		header: '栏目名称',
		dataIndex: 'title',
		flex: 1,
		width: 200,
		locked: true
	}, {
		header: '栏目ID',
		dataIndex: 'c_id',
		width: 80
	}, {
		header: '状态',
		align: 'center',
		dataIndex: 'status',
		renderer: 'parseStatusV',
		width: 60
	}, {
		header: '子栏目数量',
		dataIndex: 'sub_count',
		align: 'center',
		width: 70
	}, {
		header: '排序号',
		dataIndex: 'order_num',
		align: 'center',
		width: 60
	}, {
		header: '快捷方式',
		align: 'center',
		dataIndex: 'shortcut_contname',
//		renderer: function(val) {
//			var str = '否';
//			if(val == 1) {
//				str = '内容链接';
//			} else if(val == 2) {
//				str = '栏目链接';
//			}
//			
//			return str
//		},
		width: 80
	}, {
		header: '链接至',
		align: 'center',
		dataIndex: 'shortcut_linktoname',
		width: 100
	}, {
		header: '网卡地址白名单',
		dataIndex: 'usergroup_names_mac',
		width: 120
	}, {
		header: '网卡地址黑名单',
		dataIndex: 'usergroup_names_mac2',
		width: 120
	}, {
		header: '地区白名单',
		dataIndex: 'usergroup_names_zone',
		width: 120
	}, {
		header: '地区黑名单',
		dataIndex: 'usergroup_names_zone2',
		width: 120
	}, {
		header: '型号白名单',
		dataIndex: 'usergroup_names_model',
		width: 120
	}, {
		header: '型号黑名单',
		dataIndex: 'usergroup_names_model2',
		width: 120
	}, {
		header: '渠道白名单',
		dataIndex: 'usergroup_names_channel',
		width: 120
	}, {
		header: '渠道黑名单',
		dataIndex: 'usergroup_names_channel2',
		width: 120
	}, {
		header: '栏目类型',
		dataIndex: 'struct_type',
		renderer: 'parseTypeV',
		width: 90
	}, {
		header: '栏目下资产类型',
		dataIndex: 'resource_type',
		renderer: 'parseResourceTypeV',
		width: 120
	}, {
		header: '提供商',
		dataIndex: 'provider_id',
		renderer: 'parseProviderV',
		width: 120
	}, {
		header: '栏目动作类型',
		dataIndex: 'act_type',
		renderer: 'parseActTypeV',
		width: 130
	}, {
		header: '栏目版本',
		dataIndex: 'version',
		width: 120
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '生效时间',
		dataIndex: 'active_time',
		align: 'center',
		width: 136
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '失效时间',
		dataIndex: 'deactive_time',
		align: 'center',
		width: 136
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '创建时间',
		dataIndex: 'create_time',
		align: 'center',
		width: 136
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '修改时间',
		dataIndex: 'modify_time',
		align: 'center',
		width: 136
	},{
		header: '自动编排规则编号',
		dataIndex: 'rule_ids',
		//hidden : true,
		width : 120
	}, {
        xtype: 'actioncolumn',
        locked: true,
        width: 120,
        sortable: false,
        menuDisabled: true,
        align: 'center',
        items: [{
        	iconCls: 'add',
        	tooltip: '新建子栏目',
            handler: 'onAddSubClick'
        }, ' ', {
        	iconCls: 'edit',
        	tooltip: '编辑栏目',
            handler: 'onModifyClick'
        }, ' ', {
        	iconCls:'delete',
            tooltip: '删除栏目',
            handler: 'onRemoveClick'
        }, ' ', {
        	iconCls:'preview',
            tooltip: '预览图片',
            handler: 'onPreviewClick'
        }]
    }],
    
    listeners: {
    	itemclick: function(view, record, item, index, e, eOpts) {
    		view.toggleOnDblClick = false;
    		this.getViewModel().set('column_c_id', record.raw.c_id);
    	}
//    	,itemdblclick: function(view, record, item, index, e, eOpts) {
//    		this.getController().onModifyClick(this, -1, -1, null, e, record);
//    	}
    },
    
    tbar: [{
    	text: '新建',
    	iconCls: 'add',
		disabled: true,
		bind: {
			disabled: '{!is_selected_site}'
		},
    	menu: [{
    		text: '一级栏目',
    		disabled: true,
    		bind: {
    			disabled: '{!is_selected_site}'
    		},
//    		tooltip: '在站点中新建一级栏目',
    		bTag: 'one',
    		handler: 'onAddInfo'
    	}/*, '-', {
    		text: '基本信息',
    		tooltip: '只包含栏目的基本信息',
    		bTag: 'base',
    		disabled: true,
    		bind: {
    			disabled: '{!is_selected_column}'
    		},
    		handler: 'onAddInfo'
    	}*/, '-', {
    		text: '子栏目',
//    		tooltip: '包含基本信息和图片信息',
    		bTag: 'all',
    		disabled: true,
    		bind: {
    			disabled: '{!is_selected_column}'
    		},
    		handler: 'onAddInfo'
    	}]
    }, ' ', '-', ' ', {
    	text: '复制',
    	iconCls: 'copy',
		disabled: true,
		bind: {
			disabled: '{!is_selected_site}'
		},
    	menu: [{
    		text: '该站点复制到指定站点',
    		disabled: true,
    		bind: {
    			disabled: '{!is_selected_site}'
    		},
    		handler: 'onCopyToSite'
    	}, '-', {
    		text: '该栏目复制到指定站点',
    		disabled: true,
    		bind: {
    			disabled: '{!is_selected_column}'
    		},
    		handler: 'onCopyToSiteOne'
    	}, '-', {
    		text: '该栏目复制到指定栏目',
    		disabled: true,
    		bind: {
    			disabled: '{!is_selected_column}'
    		},
    		handler: 'onCopyToColumn'
    	}]
    }/*, ' ', '-', ' ', {
    	text: '修改',
    	iconCls: 'edit',
		disabled: true,
		bind: {
			disabled: '{!is_selected_column}'
		},
		handler: 'onModifyInfo'menu: [{
    		text: '基本信息',
    		tooltip: '修改栏目的基本信息',
    		bTag: 'base',
    		handler: 'onModifyInfo'
    	}, '-', {
    		text: '图片信息',
    		tooltip: '修改栏目的图片信息',
    		bTag: 'img',
    		handler: 'onModifyInfo'
    	}, '-', {
    		text: '全部信息',
    		tooltip: '修改栏目的基本信息和图片信息',
    		bTag: 'all',
    		handler: 'onModifyInfo'
    	}]
    }, ' ', '-', ' ', {
    	text: '删除',
    	iconCls: 'delete',
		disabled: true,
		bTag: 'seft',
		bind: {
			disabled: '{!is_selected_column}'
		},
		handler: 'onDeleteInfo'/*,
    	menu: [{
    		text: '本栏目',
    		tooltip: '删除选中的栏目信息',
    		bTag: 'seft',
    		handler: 'onDeleteInfo'
    	}, '-', {
    		text: '子栏目',
    		tooltip: '删除选中栏目的子栏目',
    		bTag: 'sub',
    		disabled: true,
    		handler: 'onDeleteInfo'
    	}, '-', {
    		text: '全部',
    		tooltip: '删除选中的栏目，及其子栏目',
    		bTag: 'all',
    		disabled: true,
    		handler: 'onDeleteInfo'
    	}]
    }*/, ' ', '-', ' ', {
    	text: '绑定资产',
    	iconCls: 'bind',
		disabled: true
    }, ' ', '-', ' ', {
    	text: '编排资产',
    	iconCls: 'edit_sort',
		disabled: true,
		handler: 'onEditSortCont'
    }, ' ', '-', ' ', {
		text: '自动编排规则',
		//disabled: true,
		bind: {
			disabled: '{!canModify}'
		},
		handler: 'showContRuleList'
	}],
	
	iconCls: 'icon-grid'
});