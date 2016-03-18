Ext.define('app.ux.window.UpdateExcelWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.update-excle-window',
	
	reference: 'update_excle_window',
	
	closable: true,
	closeAction: 'destroy',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 520,
	scrollable: true,
	title: '批量更新',
	glyph: 0xf007,
	initComponent: function() {
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.items = [{
			xtype: 'form',
			bodyPadding: 20,
			items: [{
				xtype: 'container',
				layout: 'hbox',
		        anchor: '100%',
				items: [{
					xtype: 'filefield',
					name: 'excel_file',
			        buttonText: '选择Excel文件',
			        labelWidth: 0,
			        msgTarget: 'side',
			        allowBlank: true,
			        regex: new RegExp("\\.(xls|xlsx)\\b"),
			        regexText: '文件格式不对',
			        flex: 1
				}, {
					margin: '0, 0, 0, 5',
					xtype: 'button',
					width: 70,
					text: '下载模板',
					scope: this,
					handler: function() {
						window.location.href = this.excelTemplate;
					}
				}]
				
			}, {
				margin: '5, 0, 0, 0',
				xtype: 'textareafield',
				name: 'excel_update_info',
				height: 160,
		        anchor: '100%'
			}]
		}];
		
		this.buttons = [{
			text: '上传',
			scope: this,
			handler: function() {
				var win = this;
    			win.down('textareafield').setValue("");//清除日志

				console.log(this.reqUrl);
				console.log(this.reloadGrid);
		    	var form = this.down('form');
		    	var values = form.getValues();
		    	
		    	var url = this.reqUrl;
		    	var reloadGrid = this.reloadGrid;
		    	console.log('url: ' + url);
		    	
		    	if (form.isValid()){
		    		win.mask('正在上传...');
			    	form.submit({
			    		clientValidation: true,
			    	    url: url,
			    		params: values,
			    		submitEmptyText: false,
			    		timeout: 5*60,
			    		success: function(form, action) {
			    			if(action.result.issuc && reloadGrid) {
			    				reloadGrid.reload();
			    			}
			    			
			    			win.unmask();
			    			
			    			win.down('textareafield').setValue(action.result.msg);
			    		},
			    		failure: function(form,action){
			    			win.unmask();
			    			
			    			win.down('textareafield').setValue(action.result.msg);
			    		}
			    		
			    	});
		    	}
			}
		}, {
			text: '取消',
			scope: this,
			handler: function() {
				this.destroy();
			}
		}];
		
		this.callParent();
	},
	buttonAlign: 'center',
	listeners: {
		resize: function(win, width, height) {
			var bodyH = Ext.getBody().getHeight();
			var y = (bodyH-height)/2;
			win.setY(y);
			win.setMaxHeight(bodyH-20);
		}
	}
});