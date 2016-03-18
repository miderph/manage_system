Ext.define('app.view.module.contapp.ContApp', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.contapp.ContAppController',
       'app.view.module.contapp.ContAppModel'
    ],
    
	alias: 'widget.contAppstore',
	
	uses: ['app.view.module.contapp.ContAppToolbar',
	       'app.view.module.contapp.ContAppWindow'],
	
	title: '应用资产',
	controller: 'cont-app',
    viewModel: {
        type: 'cont-app'
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
		
		if(mainViewModel.get('contSuperscriptStore') == null) {
			mainViewModel.set('contSuperscriptStore', Ext.create('app.store.ContSuperscriptStore'));
		}

		var store = Ext.create('app.store.ContAppStore');
		
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
		mode : 'SIMPLE'
		//mode : 'SINGLE'
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
		header: '包名',
		dataIndex: 'ca_package_name',
		width: 120
	}, {
		header: '版本',
		dataIndex: 'ca_version',
		width: 120
	}, {
		header: '版本代码',
		dataIndex: 'ca_version_code',
		width: 120
	}, {
		header: '开发人员',
		dataIndex: 'ca_staff',
		width: 120
	}, {
		header: '标签',
		dataIndex: 'ca_tags',
		width: 120
	}, {
		header: '发布时间',
		dataIndex: 'ca_time',
		width: 120
	}, {
		header: '文件大小',
		dataIndex: 'ca_capacity',
		width: 120
	}, {
		header: '文件md5',
		dataIndex: 'ca_md5',
		width: 120
	}, {
		header: '下载地址',
		dataIndex: 'ca_download_url_show',
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
			'<p><b>基本信息锁定:</b> {is_locked:this.isLocked}</p>',
			'<p><b>视频播放地址:</b> {cv_play_url}</p>',
			'<p><b>描述:</b> {cv_description}</p>', {
				isLocked: function(v) {
					return v==1 ? '是' : '否';
				}
		})
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
        xtype: 'cont-app-toolbar',
        dock: 'top'
    }],
    
    tbar: [{
    	text: '添加',
    	iconCls: 'add',
    	handler: 'onAddBtn'
    },/* ' ', '-', ' ', {
    	text: '修改',
    	iconCls: 'edit',
		disabled: true,
		bind: {
			disabled: '{!canModify}'
		},
    	handler: 'onModifyBtn'menu: [{
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
    	text: '应用下载地址',
		disabled: true,
		bind: {
			disabled: '{!canModify}'
		},
		handler: 'showDownloadUrlList'
    }, ' ', '-', ' ', {
    	text: '同步选中的应用',
		handler: 'recommend_sync_add'
    }, ' ', '-', ' ', {
    	text: '查看已同步应用列表',
		handler: 'showRecommendSyncList'
    }]
});