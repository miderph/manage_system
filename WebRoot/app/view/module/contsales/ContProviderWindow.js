Ext.define('app.view.module.contsales.ContProviderWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.contprovider-window',
	
	reference: 'contprovider_window',
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	/*width: 720,
	height: 520,*/
	scrollable: true,
	title: '选择供应商',
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

	    		name: 'c_s_provider',
	    		labelWidth: 50,
	    		maxWidth: 180,
	    		labelAlign: 'right',
	    		displayField: 'cp_name',
	    		valueField:'cp_id',
	    		queryMode: 'local',
	    		flex: 1,
	    		bind: {
	    			store: '{contProviderByAuthStore}'
	    		},
				fieldLabel: '*提供商',
				emptyText: '选择提供商',
	    		listeners: {
	    			select: function(combo, record) {
	    				var providerId = record.raw.cp_id;
	    				var win = combo.up('contprovider-window');
	    				var submitBtn = win.query('button[name=submitbtn]')[0]
	    				if(providerId > -1) {
	    					submitBtn.setDisabled(false);
	    				} else {
	    					submitBtn.setDisabled(true);
	    				}
	    			}
	    		}
	    	}]
		}];
		
		this.buttons = [{
			text: '确定',
			name: 'submitbtn',
			disabled: true,
			scope: this,
			handler: function() {
				var me = this;
				var providerId = this.down('combobox').getValue();
				console.log(providerId);
				
				var isRequest = true;
				var errmsg = '';
				if(providerId < 0) {
					errmsg = '供应商不能为空';
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
				
				this.onController.updateFromExcel(providerId);
				this.hide();
			}
		}];
		
		this.callParent();
	},
	
	items:[],
	buttonAlign: 'center'
});