Ext.define('app.view.module.channel.ContChannel', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.channel.ContChannelController'
    ],
    
	alias: 'widget.contChannel',
	
	title: '管理员用户',
	controller: 'cont-channel',
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;

		if(mainViewModel.get('channelTypeStore') == null) {
			mainViewModel.set('channelTypeStore', Ext.create('app.store.ChannelTypeStore'));
		}

		var store = Ext.create('app.store.ChannelStore', {
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
		header: 'ID',
		dataIndex: 's_id',
		width: 80
	}, {
		header: '类型',
		dataIndex: 'type',
		align: 'center',
		width: 100,
		renderer: function(val) {
			var str = '';
			if(val == 'apk') {
				str = 'apk发布渠道';
			} else if(val == 'cont') {
				str = '内容接入渠道';
			}

			return str
		}
	}, {
		header: '渠道号',
		dataIndex: 'channel',
		flex: 1
	}, {
//		header: '二维码模板',
//		dataIndex: 'url_template',
//		flex: 1
//	}, {
		header: '简介',
		dataIndex: 'intro',
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
    	itemdblclick: function(grid, record, item, index, e, eOpts) {
    		this.getController().onModify(this, -1, -1, null, e, record);
    		
    	}
    },
    
    tbar: [{
    	text: '添加',
    	iconCls: 'add',
    	handler: 'onAddBtn'
    }],
	
	iconCls: 'icon-grid'
});