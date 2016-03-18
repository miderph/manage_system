Ext.define('app.view.module.column.ShortcutColWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.shortcut-col-window',
	
	reference: 'shortcut_col_window',
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	height: 520,
	scrollable: true,
	title: '请在列表中选择一个栏目作为快捷链接',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		var store = Ext.create('app.store.ShortcutColStore', {
			autoLoad: true,
		});
		
		this.controller = this.onController
		
		this.items = [{
			xtype: 'treepanel',
			store: store,
			frame: true,
		    rootVisible: false,
		    columnLines: true,
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            },
            columns: [{
        		xtype: 'treecolumn',
        		header: '栏目名称',
        		dataIndex: 'title',
        		flex: 1,
        		width: 300,
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
        		width: 80
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
        	}]
		}];
		
		this.buttons = [{
			text: '提交',
			scope: this,
			handler: function() {
				var r = this.down('treepanel').getSelectionModel().lastSelected;
				
				if(r == undefined || r == null) {
					Ext.toast({
	     				html: '请选择一个资产再提交',
	     				title: '提示',
	     				saveDelay: 10,
	     				align: 'tr',
	     				closable: true,
	     				width: 200,
	     				useXAxis: true,
	     				slideInDuration: 500
	     			});
				} else {
					this.onSubmit(r.data.c_id, r.data.title);
					this.hide();
				}
			}
		}, {
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