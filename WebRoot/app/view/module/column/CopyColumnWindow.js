Ext.define('app.view.module.column.CopyColumnWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.copycolumn-window',
	
	reference: 'copycolumn_window',
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	height: 520,
	scrollable: true,
	title: '复制到以下指定栏目',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		var store = Ext.create('app.store.CopyColumnStore', {
			autoLoad: false,
		});
		
		this.controller = this.onController;
		
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
        		header: '快捷方式',
        		align: 'center',
        		dataIndex: 'is_shortcut',
        		renderer: function(val) {
        			var str = '否';
        			if(val == 1) {
        				str = '内容链接';
        			} else if(val == 2) {
        				str = '栏目链接';
        			}
        			
        			return str
        		},
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
        	}],
			listeners: {
				select: 'onColumnSelect_copy'
			}
		}];
		
		this.buttons = [{
			text: '提交',
			scope: this,
			disabled: true,
			bind: {
				disabled: '{!canSubmitColumn_copy}'
			},
			handler: function() {
				var me = this;
				var newcolumnname = this.query('textfield[name=newcolumnname]')[0].getValue();
				var viewModel = this.getViewModel();
				var targetSiteId = viewModel.get('column_site_copy');
				var targetParentId = viewModel.get('column_cid_copy');
				var sourceMenuId = viewModel.get('column_c_id');
				
				me.mask('正在复制...');
				
				Ext.Ajax.request({
					async : true,
					timeout : 1800000,
					url : 'menus/copyMenu.do',
					params : {
						name : newcolumnname,
						sourceSiteId: '-1',
						sourceMenuId : sourceMenuId,
						targetSiteId : targetSiteId,
						targetParentId : targetParentId
					},
					success : function(response, options) {
						me.unmask();
						var txt = Ext.decode(response.responseText);
						var msg = "复制失败";
						if (txt.success) {
							msg = "复制成功";
							me.hide();
							if(viewModel.get('column_site') == viewModel.get('column_site_copy')) {
								me.columnStore.load();
								viewModel.set('column_c_id', '-1');
								viewModel.set('column_cid_copy', '-1');
							}
						}
						
						Ext.toast({
		     				html: msg,
		     				title: '提示',
		     				saveDelay: 10,
		     				align: 'tr',
		     				closable: true,
		     				width: 200,
		     				useXAxis: true,
		     				slideInDuration: 500
		     			});
					},
					failure : function() {
						me.unmask();
						Ext.toast({
		     				html: '复制失败',
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
        items: [{
    				xtype: 'combobox',editable: false,

    		name: 'c_t_site',
    		labelWidth: 80,
    		maxWidth: 180,
    		labelAlign: 'right',
    		displayField: 's_name',
    		valueField:'s_id',
    		queryMode: 'local',
    		flex: 1,
    		bind: {
    			store: '{siteAllStore}'
    		},
    		fieldLabel: '站点',
    		listeners: {
    			select: 'onSiteSelect_copy'
    		}
    	}, {
    				xtype: 'combobox',editable: false,

    		name: 'c_t_status',
    		labelWidth: 56,
    		maxWidth: 200,
    		labelAlign: 'right',
    		displayField: 's_name',
    		valueField:'s_id',
    		queryMode: 'local',
    		flex: 1,
    		bind: {
    			store: '{columnStatusStore}'
    		},
    		fieldLabel: '状态',
    		listeners: {
    			select: 'onStatusSelect_copy'
    		}
    	}, {
    		xtype: 'button',
    		text: '搜索',
    		glyph: 0xf002,
    		width: 80,
    		margin: '0, 0, 0, 50',
    		disabled: false,
    		bind: {
    			disabled: '{!canSearch_copy}'
    		},
    		handler: 'onSearchColumn_copy'
    	}]
    }, {
        xtype: 'toolbar',
        dock: 'top',
        layout: 'hbox',
        items: [{
        	xtype: 'textfield',
        	labelWidth: 80,
    		labelAlign: 'right',
        	name: 'newcolumnname',
			fieldLabel: '新栏目名称',
			emptyText: '输入新栏目名称'
        }]
    }, {
        xtype: 'toolbar',
        dock: 'bottom',
        layout: 'hbox',
        items: [{
			xtype: 'displayfield',
			name: 'c_t_msg1',
	        value: '<font color="#FF0000">1. 栏目快捷方式不能复制到其他栏目快捷方式下；' 
	        	+ '<br/>2. 栏目、链接栏目不能复制到栏目快捷方式下；'
	        	+ '<br/>3. 栏目、链接栏目不能复制到自身及自身子栏目下；'
	        	+ '<br/>4. 栏目树中碰到不能复制的栏目时跳过，继续复制下一个栏目；</font>'
		}]
    }]
});