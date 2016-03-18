Ext.define('app.view.module.contsales.ContHotInfoListWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cs-hotinfolist-window',
	
	reference: 'cs_hotinfolist_window',
	uses: ['app.view.module.contsales.ContSalesToolbar'],

    closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	height: 520,
	scrollable: true,
	title: '商品促销信息列表',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.controller = this.onController;
		this.controller.onSearchHotInfo = this.onSearchHotInfo;
		this.controller.onModifyHotInfo_col = this.onModifyHotInfo_col;
		this.controller.onDeleteHotInfo_col = this.onDeleteHotInfo_col;

		var storeChannel = Ext.create('app.store.ChannelStoreByApk');
		var store = Ext.create('app.store.ContHotInfoStore');
		this.store = store;
		this.storeChannel = storeChannel;

		var pageSize = store.getPageSize();
		
		this.items = [{
			xtype: 'gridpanel',
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
            },
			columns: [{
				header: '渠道',
				dataIndex: 'channel',
				align: 'center',
				width: 80,
				locked: true
			}, {
				header: 'ID',
				dataIndex: 'id',
				align: 'center',
				width: 80
			}, {
				header: '归属ID',
				dataIndex: 'c_id',
				//hidden: true,
				align: 'center',
				width: 80
			}, {
				header: '类型（供应商、商铺、商品）',
				dataIndex: 'type',
				hidden: true,
				align: 'center',
				width: 100
			}, {
				header: '促销信息',
				dataIndex: 'hot_info',
				//align: 'center',
				width: 200
			}, {
				xtype: 'datecolumn',
				format: 'Y-m-d H:i:s',
				header: '创建时间',
				dataIndex: 'create_time',
				align: 'center',
				width: 125
			}, {
				xtype: 'datecolumn',
				format: 'Y-m-d H:i:s',
				header: '修改时间',
				dataIndex: 'modify_time',
				align: 'center',
				width: 125
			}, {
				header: '备注',
				dataIndex: 'description',
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
			    	handler: 'onModifyHotInfo_col'
		        }, ' ', {
		        	iconCls:'delete',
		        	tooltip: '删除',
			    	handler: 'onDeleteHotInfo_col'
		        }, ' ']
		    }],
			listeners: {
				scope: this/*,
		    	itemclick: function(view, record, item, index, e, eOpts) {
    				var editBtn = this.query('button[bTag=csvi_edit]')[0];
    				editBtn.record = record;
    				editBtn.setDisabled(false);
    				var delBtn = this.query('button[bTag=csvi_delete]')[0];
    				delBtn.record = record;
    				delBtn.setDisabled(false);
		    	}*/,
		    	itemdblclick: function(grid, record, item, index, e, eOpts) {
		    		this.controller.onModifyHotInfo_col(this, 0, 0, null, e, record);
		    	}
		    },
		    tbar: [{
		    	text: '添加',
		    	iconCls: 'add',
				scope: this,
		    	handler: function(btn) {
		    		this.onAddHotInfo(this.c_id,this.c_name,this.c_type,store,storeChannel);
		    	}
		    }/*,{
		    	text: '修改',
		    	iconCls: 'edit',
		    	bTag: 'csvi_edit',
				scope: this,
		    	handler: function(btn) {
		    		this.onModifyHotInfo(btn.record,this.c_name,this.c_type,store,storeChannel);
		    	}
		    },{
		    	text: '删除',
		    	iconCls: 'delete',
		    	bTag: 'csvi_delete',
				scope: this,
		    	handler: function(btn) {
		    		this.onDeleteHotInfo(btn.record,store);
		    	}
		    }*/],
	        dockedItems: [{
	        	xtype: 'toolbar',
	        	dock: 'top',
	        	items:['按条件搜索促销信息：',{
	        		xtype: 'textfield',
	        		name: 'search_name',
	        		fieldLabel: '渠道号',
	        		emptyText : '请输入id或渠道号或促销信息或备注',
	        		tooltip: '渠道号(模糊)',
	        		hideLabel : true,
	        		labelWidth: 70,
	        		maxWidth: 190,
	        		flex: 1,
	        		labelAlign: 'right'
	        	}, {
	        		xtype: 'button',
	        		text: '搜索',
	        		glyph: 0xf002,
	        		width: 60,
	        		margin: '0, 0, 0, 20',
			    	handler: 'onSearchHotInfo'
	        	}]
	        }]
		}];
		this.buttons = [{
			text: '取消',
			scope: this,
			handler: function() {
				this.hide();
			}
		}];
		
		this.callParent();
	},
	items:[],
	buttonAlign: 'center',
    onAddHotInfo: function(c_id,c_name,c_type,store,storeChannel) {
    	var win = this.controller.lookupReference('conthotinfo_addmod_window');
    	
    	if (!win) {
	    	var win = Ext.create('app.view.module.contsales.ContHotInfoAddModWindow', {
				gridstore: store,
				videoFileDistinctStore: storeChannel
			});
	        this.controller.getView().add(win);
    	}
    	win.setTitle('添加【'+c_name+'】的促销信息');

    	var form = win.down('form');
    	form.reset();

    	form.query('textfield[name=c_id]')[0].setValue(c_id);
    	form.query('textfield[name=type]')[0].setValue(c_type);

    	win.show();
    },
	onSearchHotInfo: function(){
    	var listwin = this.lookupReference('cs_hotinfolist_window');//this-->controller
        var channel = listwin.query('textfield[name=search_name]')[0].getValue();

        listwin.store.proxy.extraParams.channel = channel;
        listwin.store.load();
    }
    /*,
    onModifyHotInfo: function(record,c_name,c_type,store,storeChannel) {
    	var win = this.lookupReference('conthotinfo_addmod_window');
    	
    	if (!win) {
	    	var win = Ext.create('app.view.module.contsales.ContHotInfoAddModWindow', {
				gridstore: store,
				videoFileDistinctStore: storeChannel
			});
	        this.controller.getView().add(win);
    	}
    	win.setTitle('修改【'+this.c_name+'】的促销信息');

		var form = win.down('form');
    	form.reset();
   	
    	form.loadRecord(record);
    	
    	win.show();
    },
    onDeleteHotInfo: function(record,store) {
    	var view = this.controller.getView();
    	Ext.Msg.show({
		    title:'删除',
		    message: '促销信息：【' + record.raw.channel + '|'+record.raw.id + '|'+record.raw.hot_info+'】 ',
		    buttons: Ext.Msg.YESNO,
		    icon: Ext.Msg.QUESTION,
		    fn: function(btn) {
		        if (btn === 'yes') {
		        	view.mask('删除中...');
		        	Ext.Ajax.request({
		        		url: 'hotinfos/delete.do',
		        		async: true,
		        		params: {
		        			id: record.raw.id
		        		},
		        		scope: this,
		        		success: function(resp, opt) {
		        			var respJson = Ext.JSON.decode(resp.responseText);
		        			if(respJson.success) {
		        				store.remove(record);
		        			}
		        			view.unmask();
		        			Ext.toast({
		         				html: respJson.msg,
		         				title: '提示',
		         				saveDelay: 10,
		         				align: 'tr',
		         				closable: true,
		         				width: 200,
		         				useXAxis: true,
		         				slideInDuration: 500
		         			});
		        		}
		        	});
		        }
		    }
		});
    }*/,
    onModifyHotInfo_col: function(grid, row, col, item, e, record) {
    	var listwin = this.lookupReference('cs_hotinfolist_window');//this-->controller
    	var win = this.lookupReference('conthotinfo_addmod_window');
    	
    	if (!win) {
	    	var win = Ext.create('app.view.module.contsales.ContHotInfoAddModWindow', {
				gridstore: grid.store,
				videoFileDistinctStore: listwin.storeChannel
			});
	        this.getView().add(win);
    	}
    	win.setTitle('修改【'+listwin.c_name+'】的促销信息');

		var form = win.down('form');
    	form.reset();
   	
    	form.loadRecord(record);
    	
    	win.show();
    },
    onDeleteHotInfo_col: function(grid, row, col, item, e, record) {
    	var view = this.getView();//this-->controller
    	Ext.Msg.show({
		    title:'删除',
		    message: '促销信息：【' + record.raw.channel + '|'+record.raw.id + '|'+record.raw.hot_info+'】 ',
		    buttons: Ext.Msg.YESNO,
		    icon: Ext.Msg.QUESTION,
		    fn: function(btn) {
		        if (btn === 'yes') {
		        	view.mask('删除中...');
		        	Ext.Ajax.request({
		        		url: 'hotinfos/delete.do',
		        		async: true,
		        		params: {
		        			id: record.raw.id
		        		},
		        		scope: this,
		        		success: function(resp, opt) {
		        			var respJson = Ext.JSON.decode(resp.responseText);
		        			if(respJson.success) {
		        				grid.store.remove(record);
		        			}
		        			view.unmask();
		        			Ext.toast({
		         				html: respJson.msg,
		         				title: '提示',
		         				saveDelay: 10,
		         				align: 'tr',
		         				closable: true,
		         				width: 200,
		         				useXAxis: true,
		         				slideInDuration: 500
		         			});
		        		}
		        	});
		        }
		    }
		});
    },
});