Ext.define('app.view.module.column.ContRuleWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cont-rule-window',

	reference: 'cont_rule_window',

	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 850,
	height: 520,
	scrollable: true,
	title: '自动编排规则列表',
	glyph: 0xf007,
	layout: 'fit',
	menu_id: -1,
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.controller = this.onController;
		
		var store = Ext.create('app.store.ContRuleStore');
		
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
				dataIndex: 'rule_id',
				align: 'center',
				width: 80
			}, {
				header: '名称',
				dataIndex: 'name',
				align: 'center',
				width: 100
			}, {
				header: '价格',
				dataIndex: 'price',
				align: 'center',
				width: 80
			}, {
				header: '价格取值方式',
				dataIndex: 'price_rela',
				align: 'center',
				width: 60
			}, {
				header: '右侧价格',
				dataIndex: 'price_right',
				align: 'center',
				width: 80
			}, {
				header: '提供商ids',
				dataIndex: 'provider_ids',
				align: 'center',
				width: 80
			}, {
				header: '提供商取值方式',
				dataIndex: 'provider_rela',
				align: 'center',
				width: 60
			}, {
				header: '商铺ids',
				dataIndex: 'shop_ids',
				//renderer: 'parseProviderV',
				align: 'center',
				width: 80
			}, {
				header: '商铺取值方式',
				dataIndex: 'shop_rela',
				align: 'center',
				width: 60
			},{
				header: '分类',
				dataIndex:'category',
				align:'center',
				width: 80
			},{
				header:'分类取值方式',
				dataIndex:'category_rela',
				align: 'center',
				width: 60
			},{
				header:'生成子栏目',
				dataIndex:'category_new_menu',
				align: 'center',
				width: 80
			}],
			listeners: {
				scope: this,
		    	itemclick: function(view, record, item, index, e, eOpts) {
    				var editBtn = this.query('button[bTag=contrule_edit]')[0];
    				editBtn.record = record;
    				editBtn.setDisabled(false);
    				var delBtn = this.query('button[bTag=contrule_delete]')[0];
    				delBtn.record = record;
    				delBtn.setDisabled(false);
		    	},
		    	itemdblclick: function(view, record, item, index, e, eOpts) {
		    		record.raw.menu_id = this.menu_id;
		    		this.controller.onModifyContRule( store, record);
		    	}
		    },
		    tbar: [{
		    	text: '添加',
		    	iconCls: 'add',
				scope: this,
		    	handler: function() {
		    		this.controller.onAddContRule( store, this.menu_id);
		    	}
		    }, {
		    	text: '修改',
		    	bTag: 'contrule_edit',
		    	iconCls: 'edit',
		    	record: null,
		    	disabled: true,
				scope: this,
		    	handler: function(btn) {
		    		btn.record.raw.menu_id = this.menu_id;
		    		this.controller.onModifyContRule( store, btn.record);
		    	}
		    }, {
		    	text: '删除',
		    	bTag: 'contrule_delete',
		    	iconCls: 'delete',
		    	record: null,
		    	disabled: true,
				scope: this,
		    	handler: function(btn) {
		    		btn.record.raw.menu_id = this.menu_id;
		    		this.controller.onDeleteContRule(this.down('gridpanel'), btn.record);
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