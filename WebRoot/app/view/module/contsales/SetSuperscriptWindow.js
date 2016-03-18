Ext.define('app.view.module.contsales.SetSuperscriptWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.setsuperscript-window',
	
	reference: 'setsuperscript_window',
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	/*width: 720,
	height: 520,*/
	scrollable: true,
	title: '设置角标',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.controller = this.onController;
		
		this.items = [{
			xtype: 'form',
			bodyPadding: 20,
			items: [{
						xtype: 'combobox',editable: false,

				name: 'superscript_id',
	    		labelWidth: 60,
	    		maxWidth: 260,
	    		labelAlign: 'right',
				displayField: 'mar_s_contName',
				valueField:'mar_s_contId',
				queryMode: 'local',
	    		flex: 1,
				bind: {
					store: '{contSuperscriptStore}'
				},
				fieldLabel: '角标内容',
				listConfig: {
					itemTpl: ['<div data-qtip="{mar_s_contIntro}">{mar_s_contId} {mar_s_contName} {mar_s_contType} {mar_s_contProvider}</div>']
				},
	    		listeners: {
	    			select: function(combo, record) {
	    				var mar_s_contId = record.raw.mar_s_contId;
	    				var win = combo.up('setsuperscript-window');
	    				var submitBtn = win.query('button[name=setsuperscriptbtn]')[0]
	    				if(mar_s_contId > -1) {
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
			name: 'setsuperscriptbtn',
			disabled: true,
			scope: this,
			handler: function() {
				var me = this;
				var mar_s_contId = this.down('combobox').getValue();
				console.log(mar_s_contId);
				var c_id = this.getViewModel().get('c_id');
				console.log(c_id);
				var grid = this.grid;

				var isRequest = true;
				var errmsg = '';
				if(mar_s_contId < 0) {
					errmsg = '角标不能为空';
					isRequest = false;
				}
				
				if(c_id < 0) {
					errmsg = '资产不能为空';
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
				me.mask('设置角标...');
				
				Ext.Ajax.request({
					async : true,
					timeout : 1800000,
					url : 'contvideo/superscript_cont_video.do',
					params : {
						ids: c_id,
						contId: mar_s_contId
					},
					success : function(response, options) {
						me.unmask();
						var txt = Ext.decode(response.responseText);
						var msg = "设置失败";
						if (txt.success) {
							msg = "设置成功";
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
						grid.getStore().reload();
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
	buttonAlign: 'center'
});