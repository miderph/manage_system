Ext.define('app.view.module.contsales.ContVideoFileAddModWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.contvideofile-addmod-window',
	
	reference: 'contvideofile_addmod_window',
	
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 360,
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
				name: 'vf_id',
				hidden: true,
				value: '-1'
			}, {
				name: 'c_id',
				hidden: true,
				value: '-1'
			}, {
				name: 'provider_id',
				hidden: true,
				value: '-1'
			}, {
				name: 'rate_tag',
				hidden: true,
				value: ''
			}, {
				name: 'order_num',
				allowBlank: false,
				fieldLabel: '*第几集',
				value: '1'
			}, {
				name: 'title',
				allowBlank: true,
				fieldLabel: '标题',
				value: '1'
			}, {
						xtype: 'combobox',editable: false,

				name: 'rate_tag_eng',
				allowBlank: false,
				displayField: 'contTypeName',
				valueField:'contTypeId',
				queryMode: 'local',
				anchor: '100%',
				store: this.videoFileDistinctStore,
				fieldLabel: '*清晰度',
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
	    		}
			}, {
				name: 'bit_rate',
				fieldLabel: '比特率',
				value: '2200'
			}, {
				name: 'play_url',
				allowBlank: false,
				fieldLabel: '*播放地址'
			}]
		}];
		
		this.buttons = [{
			text: '提交',
			scope: this,
			handler: function() {
				var win = this;
				var form = this.down('form');
		    	var values = form.getValues();
		    	
		    	var url = 'videofiles/update.do';
		    	if(values.vf_id === "-1") {
		    		url = 'videofiles/save.do';
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
			    				form.reset();
			    				win.csviStore.reload();
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