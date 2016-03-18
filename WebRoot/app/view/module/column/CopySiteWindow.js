Ext.define('app.view.module.column.CopySiteWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.copysite-window',
	
	reference: 'copysite_window',
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	/*width: 720,
	height: 520,*/
	scrollable: true,
	title: '复制到以下指定站点',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.controller = this.onController;
		
		this.items = [{
			xtype: 'form',
			bodyPadding: 10,
			items: [{
				xtype: 'displayfield',
				name: 'c_t_msg1',
		        value: '<font color="#FF0000">1. 栏目快捷方式不能复制到链接栏目站点下；<br/>2. 栏目树中碰到不能复制的栏目时跳过，继续复制下一个栏目；</font>'
			}, {
	    				xtype: 'combobox',editable: false,

	    		name: 'c_t_site',
				margin: '10, 0, 0, 0',
	    		labelWidth: 30,
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
	    			select: function(combo, record) {
	    				var siteId = record.raw.s_id;
	    				var win = combo.up('copysite-window');
	    				var submitBtn = win.query('button[name=copysitebtn]')[0]
	    				if(siteId > -1 && win.getViewModel().get('column_site') != siteId) {
	    					submitBtn.setDisabled(false);
	    				} else {
	    					submitBtn.setDisabled(true);
	    				}
	    			}
	    		}
	    	}]
		}];
		
		this.buttons = [{
			text: '提交',
			name: 'copysitebtn',
			disabled: true,
			scope: this,
			handler: function() {
				var me = this;
				var targetSiteId = this.down('combobox').getValue();
				console.log(targetSiteId);
				var sourceSiteId = this.getViewModel().get('column_site');
				console.log(sourceSiteId);
				
				var isRequest = true;
				var errmsg = '';
				if(sourceSiteId < 0) {
					errmsg = '源站点不能为空';
					isRequest = false;
				}
				
				if(targetSiteId < 0) {
					errmsg = '目标站点不能为空';
					isRequest = false;
				}
				
				if(!isRequest) {
					Ext.toast({
	     				html: errmsg,
	     				title: '提示',
	     				saveDelay: 10,
	     				align: 'tr',
	     				closable: true,
	     				width: 200,
	     				useXAxis: true,
	     				slideInDuration: 500
	     			});
					
					return;
				}
				
				me.mask('正在复制...');
				
				Ext.Ajax.request({
					async : true,
					timeout : 1800000,
					url : 'menus/copyMenu.do',
					params : {
						name : '',
						sourceSiteId: sourceSiteId,
						sourceMenuId : '-1',
						targetSiteId : targetSiteId,
						targetParentId : '0'
					},
					success : function(response, options) {
						me.unmask();
						var txt = Ext.decode(response.responseText);
						var msg = "复制失败";
						if (txt.success) {
							msg = "复制成功";
							me.hide();
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
	listeners: {
		show: function(win) {
			var siteId = this.down('combobox').getValue();
			var column_site = this.getViewModel().get('column_site');
			var submitBtn = win.query('button[name=copysitebtn]')[0];
			
			if(siteId && siteId > -1 && column_site != siteId) {
				submitBtn.setDisabled(false);
			} else {
				submitBtn.setDisabled(true);
			}
		}
	}
});