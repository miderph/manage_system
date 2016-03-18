Ext.define('app.view.module.column.EditColContImgWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.editcolcont-window',
	
	reference: 'editcolcontimg_window',
	
    closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 180,
	/*height: 520,*/
	scrollable: true,
	title: '设置图片',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.items = [{
			xtype: 'form',
			bodyPadding: 10,
			fieldDefaults: {
		        labelWidth: 60,
				labelAlign: 'right'
		    },
		    items: [{
		    	xtype: 'radiogroup',
	            fieldLabel: '图片类型',
	            columns: 1,
	            items: [
	                {boxLabel: '不设置', name: 'c_c_i_postion', inputValue:'-1', checked: true},
	                {boxLabel: '竖图', name: 'c_c_i_postion', inputValue:'0'},
	                {boxLabel: '横图', name: 'c_c_i_postion', inputValue:'1'},
	    			{boxLabel: '小方图', name: 'c_c_i_postion', inputValue:'2'},
	    			{boxLabel: '四格图', name: 'c_c_i_postion', inputValue:'3'},
	    			{boxLabel: '六格图', name: 'c_c_i_postion', inputValue:'4'}
	            ]
		    }]
		}];
		
		this.buttons = [{
			text: '提交',
			scope: this,
			handler: function() {
				var me = this;
				var v = me.down('radiogroup').getValue();
				var postion = -1;
				if(v.c_c_i_postion) {
					postion = v.c_c_i_postion;
				}
				
				me.mask('移动中...');
				Ext.Ajax.request({
					url: 'is_url_used_rela_menu_cont.do',
					params: {
						ids: me.eids,
						comboBox: postion,
						menuId: me.menuId
					},
					success:function(resp) {
						me.unmask();
						var respJson = Ext.JSON.decode(resp.responseText);
						var msg = "设置失败";
						if(respJson.success) {
							me.gridStore.load();
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
					},
					failure:function(){
						me.unmask();
						Ext.toast({
			 				html: '设置失败',
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
	
	buttonAlign: 'center'
});