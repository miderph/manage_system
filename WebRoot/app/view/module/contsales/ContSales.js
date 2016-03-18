Ext.define('app.view.module.contsales.ContSales', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.contsales.ContSalesController',
       'app.view.module.contsales.ContSalesModel'
    ],
    
	alias: 'widget.contSales',
	
	uses: ['app.view.module.contsales.ContSalesToolbar',
	       'app.view.module.contsales.ContSalesWindow'],
	
	title: '购物资产',
	controller: 'cont-sales',
    viewModel: {
        type: 'cont-sales'
    },
	
	frame: true,
    columnLines: true,
	iconCls: 'icon-grid',
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('contStatusStore') == null) {
			mainViewModel.set('contStatusStore', Ext.create('app.store.ContStatusStore'));
		}
		
		if(mainViewModel.get('contProviderByAuthStore') == null) {
			mainViewModel.set('contProviderByAuthStore', Ext.create('app.store.ContProviderByAuthStore'));
		}
		if(mainViewModel.get('contShopStore') == null) {
			mainViewModel.set('contShopStore', Ext.create('app.store.ShopStore'));
		}
		if(mainViewModel.get('contChannelStoreByCont') == null) {
			mainViewModel.set('contChannelStoreByCont', Ext.create('app.store.ChannelStoreByCont'));
		}
		if(mainViewModel.get('contSuperscriptStore') == null) {
			mainViewModel.set('contSuperscriptStore', Ext.create('app.store.ContSuperscriptStore'));
		}
		
		var store = Ext.create('app.store.ContSalesStore');
		mainViewModel.set('contSalesStore_for_refresh', store);

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
	selModel : {
		selType : 'checkboxmodel',
		//mode : 'SIMPLE'
		mode : 'SINGLE'
	},
	columns: [{
		header: '名称',
		dataIndex: 'c_name',
		width: 200,
		locked: true
	}, {
		header: 'ID',
		dataIndex: 'c_id',
		align: 'center',
		width: 100
	}, {
		header: '状态',
		dataIndex: 'c_status',
		renderer: 'parseStatusV',
		align: 'center',
		width: 80
	}, {
		header: '提供商',
		dataIndex: 'provider_id',
		renderer: 'parseProviderV',
		align: 'center',
		width: 100
	}, {
		header: '拼音简写',
		dataIndex: 'pinyin',
		width: 150
	}, {
		header: '库存数量',
		dataIndex: 'cs_sum_stock',
		align: 'center',
		width: 60
	}, {
		header: '商品编号',
		dataIndex: 'cs_sales_no',
		align: 'center',
		width: 100
	}, {
		header: '市场价格',
		dataIndex: 'cs_fake_price',
		renderer: function(v) {
			return '¥'+v;
		},
		align: 'center',
		width: 60
	}, {
		header: '销售价格',
		dataIndex: 'cs_sale_price',
		renderer: function(v) {
			return '<span style="color:green;">¥' + v + '</span>';
		},
		align: 'center',
		width: 60
	}, {
		header: '运费',
		dataIndex: 'cs_post_price',
		renderer: function(v) {
			return '¥'+v;
		},
		align: 'center',
		width: 60
	}, {
		header: '折扣',
		dataIndex: 'cs_disaccount',
		renderer: function(v) {
			return '<span style="color:red;">' + v + '</span>';
		},
		width: 60
	}, {
		header: '价格区间',
		dataIndex: 'cs_price_desc',
		width: 120
	}, {
		header: '角标',
		dataIndex: 'cont_superscript',
		align: 'center',
		width: 120
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
		header: '服务信息(含字段：赠品、销量、运费描述、服务)',
		dataIndex: 'cs_service_info',
		width: 150
	}, {
		header: '视频打点',
		dataIndex: 'c_video_seg_time',
		width: 150
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '生效时间',
		dataIndex: 'active_time',
		align: 'center',
		width: 150
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '失效时间',
		dataIndex: 'deactive_time',
		align: 'center',
		width: 150
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
		header: '',
        xtype: 'actioncolumn',
        locked: true,
        width: 100,
        sortable: false,
        menuDisabled: true,
        align: 'center',
        items: [' ', {
        	iconCls: 'edit',
        	tooltip: '编辑资产',
            handler: 'onModify'
        }, ' ', {
        	iconCls: 'delete',
        	tooltip: '删除资产',
            handler: 'onDelete'
        }, ' ', {
        	iconCls:'preview',
            tooltip: '预览图片',
            handler: 'onPreviewClick'
        }]
    }],
	
	plugins: [{
		ptype: 'rowexpander',
		expandOnDblClick: false,
		rowBodyTpl: new Ext.XTemplate(
			'<p><b>别名:</b> {cv_alias}</p>',
			'<p><b>经销商-正标题:</b> {cs_cp_name}</p>',
			'<p><b>经销商-副标题:</b> {cs_sub_cp_name}</p>',
			'<p><b>基本信息锁定:</b> {is_locked:this.isLocked}</p>',
			'<p><b>关键词:</b> {cs_key_words}</p>',
			'<p><b>支付方式:</b> {cs_pay_type_names}</p>',
			'<p><b>视频播放地址:</b> {cv_play_url}</p>',
			'<p><b>商品信息:</b> {cs_hot_info}</p>', {
				isLocked: function(v) {
					return v==1 ? '是' : '否';
				}
			},
			'<p><b>服务信息(含字段：赠品、销量、运费描述、服务):</b> {cs_service_info}</p>',
			'<p><b>视频打点:</b> {c_video_seg_time}</p>'
			)
	}],
    
    listeners: {
    	itemclick: function(view, record, item, index, e, eOpts) {
    		this.getViewModel().set('c_id', record.raw.c_id);
    	}
//    	,itemdblclick: function(grid, record, item, index, e, eOpts) {
//    		this.getController().onModify(this, 0, 0, null, e, record);
//    	}
    },
    
    dockedItems: [{
        xtype: 'cont-sales-toolbar',
        dock: 'top'
    }],
    
    tbar: [{
    	text: '添加',
    	iconCls: 'add',
    	handler: 'onAddBtn'
    }/*, ' ', '-', ' ', {
    	text: '修改',
    	iconCls: 'edit',
		disabled: true,
		bind: {
			disabled: '{!canModify}'
		},
    	menu: [{
    		text: '基本信息',
    		tooltip: '修改资产的基本信息',
    		bTag: 'base',
    		handler: 'onModifyBtn'
    	}, '-', {
    		text: '图片信息',
    		tooltip: '修改资产的图片信息',
    		bTag: 'img',
    		handler: 'onModifyBtn'
    	}, '-', {
    		text: '全部',
    		tooltip: '修改资产的基本信息和图片信息',
    		bTag: 'all',
    		handler: 'onModifyBtn'
    	}]
    }, ' ', '-', ' ', {
    	text: '删除',
    	iconCls: 'delete',
		disabled: true,
		bind: {
			disabled: '{!canModify}'
		},
		handler: 'onDeleteBtn'
    }*/, ' ', '-', ' ', {
    	text: '设置角标',
		disabled: true,
		bind: {
			disabled: '{!canModify}'
		},
		handler: 'onSetSuperscript'
    }, ' ', '-', ' ', {
    	text: '删除角标',
		disabled: true,
		bind: {
			disabled: '{!canModify}'
		},
		handler: 'onDelSuperscript'
    }, ' ', '-', ' ', {
    	text: '视频播放地址',
		disabled: true,
		bind: {
			disabled: '{!canModify}'
		},
		handler: 'showVideoInfo'
    }, '-', {
    	iconCls: 'edit',
    	text: '分渠道促销信息',
    	handler: 'onHotInfoByChannel'
    }, '-', {
    	text: 'Excel添加',
    	iconCls: 'add',
    	listeners: {
    		click: 'onAddFromExcel'
    	}
    }, '-', {
    	text: 'Excel更新库存',
    	iconCls: 'sumstock',
    	listeners: {
    		click: 'onUpdateFromExcel'
    	}
    }]
});