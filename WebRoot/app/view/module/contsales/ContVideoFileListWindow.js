Ext.define('app.view.module.contsales.ContVideoFileListWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cs-videofilelist-window',
	
	reference: 'cs_videoinfo_window',
	
    closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	height: 520,
	scrollable: true,
	title: '视频播放地址列表',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.controller = this.onController;
		
		var store = Ext.create('app.store.CSVideoInfoStore');
		
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
				header: 'ID',
				dataIndex: 'vf_id',
				align: 'center',
				width: 80
			}, {
				header: '视频资产ID',
				dataIndex: 'c_id',
				align: 'center',
				width: 80
			}, {
				header: '视频资产名称',
				dataIndex: 'c_name',
				align: 'center',
				width: 100
			}, {
				header: '集数',
				dataIndex: 'order_num',
				align: 'center',
				width: 30
			}, {
				header: '标题',
				dataIndex: 'title',
				align: 'center',
				width: 30
			}, {
				header: '比特率',
				dataIndex: 'bit_rate',
				align: 'center',
				width: 40
			}, {
				header: '清晰度',
				dataIndex: 'rate_tag',
				align: 'center',
				width: 40
			}, {
				header: '供应商',
				dataIndex: 'provider_id',
				renderer: 'parseProviderV',
				align: 'center',
				width: 60
			}, {
				header: '播放地址',
				dataIndex: 'play_url',
				align: 'center',
				width: 500
			}],
			listeners: {
				scope: this,
		    	itemclick: function(view, record, item, index, e, eOpts) {
    				var editBtn = this.query('button[bTag=csvi_edit]')[0];
    				editBtn.record = record;
    				editBtn.setDisabled(false);
    				var delBtn = this.query('button[bTag=csvi_delete]')[0];
    				delBtn.record = record;
    				delBtn.setDisabled(false);
		    	}
		    },
		    tbar: [{
		    	text: '添加',
		    	iconCls: 'add',
				scope: this,
		    	handler: function() {
		    		this.controller.onAddCVI(this.videoFileDistinctStore, store, this.cs_id, this.provider_id);
		    	}
		    }, {
		    	text: '修改',
		    	bTag: 'csvi_edit',
		    	iconCls: 'edit',
		    	record: null,
		    	disabled: true,
				scope: this,
		    	handler: function(btn) {
		    		btn.record.raw.c_id = this.cs_id;
		        	btn.record.raw.provider_id = this.provider_id;
		    		this.controller.onModifyCVI(this.videoFileDistinctStore, store, btn.record);
		    	}
		    }, {
		    	text: '删除',
		    	bTag: 'csvi_delete',
		    	iconCls: 'delete',
		    	record: null,
		    	disabled: true,
				scope: this,
		    	handler: function(btn) {
		    		this.controller.onDeleteCVI(this.down('gridpanel'), btn.record);
		    		/*Ext.Msg.show({
					    title:'删除',
					    message: '资产视频：' + btn.record.raw.c_name + '， 第 ' + btn.record.raw.order_num + ' 集， ' + btn.record.raw.rate_tag,
					    buttons: Ext.Msg.YESNO,
					    icon: Ext.Msg.QUESTION,
					    scope: this,
					    fn: function(btn) {
					        if (btn === 'yes') {
					        	this.controller.onDeleteCVI(this.down('gridpanel'), btn.record);
					        }
					    }
					});*/
		    		
		    	}
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
	buttonAlign: 'center'
});