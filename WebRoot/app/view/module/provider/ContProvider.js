Ext.define('app.view.module.provider.ContProvider', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.provider.ContProviderController'
    ],
    
	alias: 'widget.contProvider',

	uses: ['app.view.module.provider.ContProviderToolbar'],
	       
	title: '提供商列表',
	controller: 'cont-provider',
	
	frame: true,
    columnLines: true,
	
	viewConfig:{
		stripeRows:true,
	    enableTextSelection:true,
	},
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('siteAllStore') == null) {
			mainViewModel.set('siteAllStore', Ext.create('app.store.SiteAllStore'));
		}
		if(mainViewModel.get('contProviderByAuthStore') == null) {
			mainViewModel.set('contProviderByAuthStore', Ext.create('app.store.ContProviderByAuthStore'));
		}

		var store = Ext.create('app.store.ContProviderStore', {
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
		header: '提供商名称',
		dataIndex: 'name',
		width: 200,
		locked: true
	}, {
		header: 'ID',
		dataIndex: 'cp_id',
		width: 80
	}, {
		header: '状态',
		dataIndex: 'p_status',
		align: 'center',
		renderer: function(val) {
			var str = '';
			if(val == 1) {
				str = '正常-普通';
			} else if(val == 2) {
				str = '正常-高级';
			} else if(val == 0) {
				str = '待审';
			} else if(val == -1) {
				str = '禁用';
			}
			
			return str
		},
		lockable: true,
		width: 80
	}, {
		header: 'Prefix前缀',
		dataIndex: 'stb_prefix_names',
		lockable: true,
		width: 150
	}, {
		header: '对应站点',
		dataIndex: 'site_id',
		renderer: 'parseSiteV',
		lockable: true,
		width: 120
	}, {
		header: '默认站点',
		align: 'center',
		dataIndex: 'isdefault',
		renderer: function(val) {
			var str = '';
			if(val == 1) {
				str = '是';
			} else if(val == 0) {
				str = '否';
			}
			
			return str
		},
		lockable: true,
		width: 60
	}, {
		header: '对应内容提供方',
		dataIndex: 'cont_provider_id_name',
		lockable: true,
		width: 120
	}, {
		header: '频道提供方',
		dataIndex: 'chn_provider_name',
		lockable: true,
		width: 80
	}, {
		header: '图标',
		dataIndex: 'icon_url',
		renderer: 'parseIconUrl',
		flex: 1
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
    }/*,
    dockedItems: [{
        xtype: 'cont-provider-toolbar',
        dock: 'top'
    }]*/,
	plugins: [{
		ptype: 'rowexpander',
		expandOnDblClick: false,
		rowBodyTpl: new Ext.XTemplate(
			'<p><b>ID:</b> {cp_id}</p>',
			'<p><b>说明:</b> {description}</p>',
			'<p><b>搜索cont提供商:</b> {cont_provider_names:this.formatNames}</p>',
			'<p><b>搜索epg提供方:</b> {epg_provider_names:this.formatNames}</p>',
			'<p><b>EPG提供商优先级:</b> {epg_priority}</p>',
			'<p><b>XMPP:</b> {xmpp_index:this.parserXmppV}</p>',
			'<p><b>UAP:</b> {need_check_uap:this.parserV}</p>',
			'<p><b>直播切屏:</b> {can_switchtv:this.parserV}',
			'{0:this.addSpace_1}<b>点播切屏:</b> {can_playvideo:this.parserV}',
			'{0:this.addSpace_1}<b>云盘下载:</b> {can_download:this.parserV}</p>',
			'<p><b>直播节目录制:</b> {can_recording:this.parserV}',
			'{0:this.addSpace_2}<b>直播节目时移:</b> {can_timeshift:this.parserV}',
			'{0:this.addSpace_2}<b>回放:</b> {can_playback:this.parserV}</p>',
			'<p><b>直播切屏播放提供商:</b> {can_switchtv_names}</p>',
			'<p><b>点播切屏播放提供商:</b> {can_playvideo_names}</p>', 
			'<p><b>云盘下载提供商:</b> {can_download_names}</p>', 
			'<p><b>直播节目录制提供商:</b> {can_recording_names}</p>', 
			'<p><b>直播节目时移提供商:</b> {can_timeshift_names}</p>', 
			'<p><b>直播节目回放提供商:</b> {can_playback_names}</p>', {
			formatSInfo: function(v) {
				var arr = v.split('\\n');
				return arr.join('<br>&nbsp;&nbsp;&nbsp;&nbsp;');
			},
			parserV: function(v) {
				var str = '';
				
				if(v==1) {
					str = '能'
				} else if(v==0) {
					str = '不能'
				}
				
				return str
			},
			parserXmppV: function(v) {
				var str = '';
				
				if(v==1) {
					str = '智联'
				} else if(v==2) {
					str = '华数'
				}
				
				return str
			},
			formatNames: function(v) {
				var arr = v.split(',');
				return arr.join('&nbsp;&nbsp;');
			},
			addSpace_1: function() {
				var result = '';
				for(i=0; i<18; i++) {
					result += '&nbsp;'
				}
				return result;
			},
			addSpace_2: function() {
				var result = '';
				for(i=0; i<12; i++) {
					result += '&nbsp;'
				}
				return result;
			}
		})
	}],
    
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