Ext.define('app.view.module.contsales.ContHotInfoAddModWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.conthotinfo-addmod-window',
	
	reference: 'conthotinfo_addmod_window',
	
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 500,
	scrollable: true,
	title: '新建',
	glyph: 0xf007,
	initComponent: function() {
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.items = [{
			xtype: 'form',
			bodyPadding: 20,
			fieldDefaults: {
		        labelWidth: 68,
				labelAlign: 'right',
		        anchor: '100%'
		    },
		    defaultType: 'textfield',
			items: [{
				name: 'id',
				hidden: true,
				value: '-1'
			}, {
				name: 'c_id',
				allowBlank: true,
				//hidden: true,
				readOnly: true,
				fieldLabel: '归属ID',
				value: '-1'
			}, {
				name: 'type',
				allowBlank: true,
				hidden: true,
				readOnly: true,
				fieldLabel: '类型（供应商、商铺、商品）',
				value: ''
			}, {
						xtype: 'combobox',editable: false,

				name: 'channel',
				allowBlank: false,
				displayField: 'name',
				valueField:'channel',
				queryMode: 'local',
				anchor: '100%',
				store: this.videoFileDistinctStore,
				fieldLabel: '*渠道'/*,
	    		listeners: {
	    			select: function(combo, record) {
	    				var contTypeId = record.raw.contTypeId;
	    				var win = combo.up('contvideofile-addmod-window');
	    				
	    				var rateTag = win.query('textfield[name=rate_tag]')[0];
	    				rateTag.setValue(record.raw.contTypeName);
	    				
	    				var bitRate = win.query('textfield[name=bit_rate]')[0]
	    				
						var value = combo.getValue();
						var bit_rate = "2200";
						if (value == 'gd') {
							bit_rate = "3800";
						} else if (value == 'hd') {
							bit_rate = "2200";
						} else {
							bit_rate = "1300";
						}

						bitRate.setValue(bit_rate);
	    			}
	    		}*/
			}, {
				xtype: 'textareafield',
				name: 'hot_info',
				allowBlank: false,
				fieldLabel: '*促销信息',
				value: ''
			}, {
				xtype: 'textareafield',
				name: 'description',
				allowBlank: true,
				fieldLabel: '*备注'
			}]
		}];
		
		this.buttons = [{
			text: '提交',
			scope: this,
			handler: function() {
				var win = this;
				var form = this.down('form');
		    	var values = form.getValues();
		    	var store = this.gridstore;

		    	var url = 'hotinfos/update.do';
		    	if(values.id === "-1") {
		    		url = 'hotinfos/save.do';
		    	}
		    	
		    	if (form.isValid()){
		    		win.mask('正在保存...');
			    	form.submit({
			    		clientValidation: true,
			    	    url: url,
			    		params: values,
			    		submitEmptyText: false,
			    		success: function(form, action) {
			    			win.unmask();
			    			if(action.result.issuc) {
			    				store.reload();
			    				form.reset();
			    				win.hide();
			    			}
			    			Ext.MessageBox.alert('警告', action.result.msg);
			    		}
			    	});
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