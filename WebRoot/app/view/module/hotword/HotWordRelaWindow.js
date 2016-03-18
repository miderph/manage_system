Ext.define('app.view.module.hotword.HotWordRelaWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.hotword-rela-window',
	
	reference: 'hotword_rela_window',
	
	uses: ['app.store.HotWordStore'],
	
    closable: true,
	closeAction: 'hide',
	resizable: true,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 500,
	height: 360,
	scrollable: true,
	title: '请在列表中选择一个或多个热词绑定到站点',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;

		var viewModel = this.getViewModel();
    	var site_id = viewModel.get('hw_site_id');
        //console.log("---here--site_id="+site_id);

		var store = Ext.create('app.store.HotWordStore', {
		});
		store.proxy.url = 'hotword/query_rela.do';
		store.proxy.extraParams.site_id = ''+site_id;
		store.load();
		
		var pageSize = store.getPageSize();
		
		this.items = [{
			xtype: 'gridpanel',
			store: store,
		    selModel: {selType:'checkboxmodel'},
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
        		header: '热词',
        		dataIndex: 'hotword',
        		flex: 1,
        		width: 200,
        		locked: true
        	}, {
        		header: 'ID',
        		dataIndex: 'hw_id',
        		align: 'center',
        		width: 100
        	}, {
        		header: '站点',
        		dataIndex: 'site_id',
        		renderer: 'parseSiteV',
        		align: 'center',
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
        	}],
		}];
		
		this.buttons = [{
			text: '提交',
			//scope: this,
			handler: 'onRelaAdd'
		}, {
			text: '关闭',
			scope: this,
			handler: function() {
				this.hide();
			}
		}];
		
		this.callParent();
	},
	items:[],
	buttonAlign: 'center',
	
	dockedItems: [{
        xtype: 'toolbar',
        dock: 'top',
        layout: 'hbox',
        items: [{
			xtype: 'textfield',
			name: 'c_sc_w_key',
			labelWidth: 56,
			maxWidth: 150,
			labelAlign: 'right',
			flex: 1,
			fieldLabel: '关键字',
			emptyText: '请输入关键字'
		}, {
			xtype: 'button',
			text: '搜索',
			margin: '0, 0, 0, 10',
			glyph: 0xf002,
			width: 70,
			handler: function() {
				var win = this.up('hotword-rela-window');
				var store = win.down('gridpanel').getStore();
				
		    	comp = win.query('textfield[name=c_sc_w_key]')[0];
		    	store.proxy.extraParams.hotword = comp.getValue();
		    	
				store.reload();
			}
		}]
    }]
});
