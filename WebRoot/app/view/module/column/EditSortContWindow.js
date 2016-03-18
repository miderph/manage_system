Ext.define('app.view.module.column.EditSortContWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.editsort-cont-window',
	
	reference: 'editsort_cont_window',
	
	uses: [
	       'app.store.ShortcutConStore',
	       'app.store.ContTypeStore',
	       'app.store.ContStatusStore'],
	
    closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 820,
	height: 520,
	scrollable: true,
	title: '请在列表中选择一个内容资产作为栏目的快捷链接',
	glyph: 0xf007,
	layout: 'fit',
	selectIndex: -1,
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		var store = Ext.create('app.store.EditSortContStore');
		
		var pageSize = store.getPageSize();
		this.pageSize = pageSize;
		
		this.items = [{
			xtype: 'gridpanel',
			store: store,
			selModel: Ext.create('Ext.selection.CheckboxModel', {
				mode: "MULTI"
			}),
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
		        align: 'center',
				dataIndex: 'mar_s_contId',
				width: 100,
				locked: true
			}, {
				header: '名称',
				dataIndex: 'mar_s_contName',
				width: 240,
				locked: true
			}, {
				header: '类型',
				dataIndex: 'mar_s_contType',
				align: 'center',
				width: 60
			}, {
				header: '提供商',
				dataIndex: 'mar_s_contProvider',
				align: 'center',
				width: 100
			}, {
				header: '图片类型',
				dataIndex: 'is_url_used',
				align: 'center',
				width: 100
			}, {
				header: '默认图片类型',
				dataIndex: 'cont_is_url_used',
				align: 'center',
				width: 100
			}, {
				header: '角标',
				dataIndex: 'cont_superscript',
				align: 'center',
				width: 100
			}, {
				header: '描述',
				dataIndex: 'mar_s_contIntro',
				width: 200
			}],
			
			listeners: {
				select: function(rm, r, index) {
					this.up('editsort-cont-window').selectIndex = index;
				}
			}
		}];
		
		this.callParent();
	},
	
	tbar: [{
		text: '绑定',
		iconCls: 'bind'
	}, ' ', {
		text: '解绑'
	}, ' ', '-', ' ', {
		text: '上移',
    	iconCls: 'move_up',
    	tag: 'up',
    	handler: 'onMoveColCont'
	}, ' ', {
		text: '下移',
    	iconCls: 'move_down',
    	tag: 'down',
    	handler: 'onMoveColCont'
	}, ' ', {
		text: '置顶',
    	iconCls: 'move_up_first',
    	tag: 'top',
    	handler: 'onMoveColCont'
	}, ' ', {
		text: '置底',
    	iconCls: 'move_down_last',
    	tag: 'bottom',
    	handler: 'onMoveColCont'
	}, ' ', '-', ' ', {
		text: '图片设置',
    	tag: 'bottom',
    	handler: 'onEditColContImg'
	}, ' ', {
		text: '设置角标',
		iconCls: 'set'
	}, ' ', {
		text: '删除角标',
		iconCls: 'delete'
	}],
	
	iconCls: 'icon-grid'
});