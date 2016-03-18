Ext.define('app.view.module.column.ShortcutConWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.shortcut-con-window',
	
	reference: 'shortcut_con_window',
	
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
	width: 720,
	height: 520,
	scrollable: true,
	title: '请在列表中选择一个内容资产作为栏目的快捷链接',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		var store = Ext.create('app.store.ShortcutConStore', {
//			autoLoad: true
		});
		
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
				header: '描述',
				dataIndex: 'mar_s_contIntro',
				width: 200
			}],
		}];
		
		this.buttons = [{
			text: '提交',
			scope: this,
			handler: function() {
				var r = this.down('gridpanel').getSelectionModel().lastSelected;
				
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
					this.onSubmit(r.data.mar_s_contId, r.data.mar_s_contName);
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
	buttonAlign: 'center',
	
	dockedItems: [{
        xtype: 'toolbar',
        dock: 'top',
        layout: 'hbox',
		defaultType: 'combobox',
        items: [{
			name: 'c_sc_w_provider',
			displayField: 'cp_name',
			valueField:'cp_id',
			queryMode: 'local',
			labelWidth: 56,
			labelAlign: 'right',
			flex: 5,
			bind: {
				store: '{contProviderByAuthStore}'
			},
			fieldLabel: '提供商',
			emptyText: '选择提供商',
			value:'-10000',
			listeners: {
				select: function() {
					
				}
			}
		}, {
			name: 'c_sc_w_conttype',
			displayField: 's_name',
			valueField:'s_id',
			queryMode: 'local',
			labelWidth: 56,
			labelAlign: 'right',
			margin: '0, 0, 0, 10',
			flex: 5,
			bind: {
				store: '{contTypeStore}'
            },
    		value:'-10000',
			fieldLabel: '资产类型',
			emptyText: '选择资产类型'
		}, {
			name: 'c_sc_w_contstatus',
			displayField: 's_name',
			valueField:'s_id',
			queryMode: 'local',
			labelWidth: 56,
			labelAlign: 'right',
			margin: '0, 20, 0, 10',
			value: '-10000',
			flex: 6,
			bind: {
				store: '{contStatusStore}'
            },
    		value:'-10000',
			fieldLabel: '资产状态',
			emptyText: '选择资产状态'
		}]
    }, {
        xtype: 'toolbar',
        dock: 'top',
        layout: 'hbox',
        items: [{
			xtype: 'textfield',
			name: 'c_sc_w_key',
			labelWidth: 56,
			labelAlign: 'right',
			flex: 5,
			fieldLabel: '关键字',
			emptyText: '输入关键字'
		}, {
			xtype: 'datefield',
			name: 'c_sc_w_starttime',
			labelWidth: 56,
			labelAlign: 'right',
			margin: '0, 0, 0, 10',
			flex: 5,
			fieldLabel: '创建时间',
			value: Ext.Date.add(new Date(), Ext.Date.YEAR, -5),
			format: 'Y-m-d H:i:s'
		}, {
			xtype: 'container',
			layout: 'hbox',
			margin: '0, 20, 0, 10',
			flex: 6,
			items: [{
				xtype: 'displayfield',
				name: 'c_sc_w_space',
		        value: '至'
			}, {
				xtype: 'datefield',
				name: 'c_sc_w_endtime',
				margin: '0, 0, 0, 10',
				labelAlign: 'right',
				flex: 1,
				value: new Date(),
				format: 'Y-m-d H:i:s'
			}, {
				xtype: 'button',
				text: '搜索',
				margin: '0, 0, 0, 10',
				glyph: 0xf002,
				width: 70,
				handler: function() {
					var win = this.up('shortcut-con-window');
					var store = win.down('gridpanel').getStore();
					
					var comp = win.query('combobox[name=c_sc_w_provider]')[0];
					store.proxy.extraParams.contProvider = comp.getValue();
					
					comp = win.query('combobox[name=c_sc_w_contstatus]')[0];
			    	store.proxy.extraParams.contStatus = comp.getValue();
			    	
			    	comp = win.query('combobox[name=c_sc_w_conttype]')[0];
			    	store.proxy.extraParams.contType = comp.getValue();
			    	
			    	comp = win.query('textfield[name=c_sc_w_key]')[0];
			    	store.proxy.extraParams.keyWord = comp.getValue();
			    	
			    	comp = win.query('datefield[name=c_sc_w_starttime]')[0];
			    	store.proxy.extraParams.startTime = comp.getValue();
			    	
			    	comp = win.query('datefield[name=c_sc_w_endtime]')[0];
			    	store.proxy.extraParams.endTime = comp.getValue();
			    	
					store.load();
				}
			}]
		}]
    }]
});