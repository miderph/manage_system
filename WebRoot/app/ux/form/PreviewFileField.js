Ext.define('app.ux.form.PreviewFileField', {
	extend: 'Ext.container.Container',
	alias: 'widget.previewfilefield',
	
	initComponent: function() {
		
		if(Ext.isEmpty(this.fieldLabel)) {
			this.fieldLabel = '';
		}
		
		if(Ext.isEmpty(this.allowBlank)) {
			this.allowBlank = true;
		}
		
		if(Ext.isEmpty(this.labelWidth)) {
			this.labelWidth = 0;
		}
		/* 设置事件监听 */
		this.listeners = {
				afterrender: function() {
					//console.log("--:here 11111,afterrender");
					var url = this.down('textfield').getValue();
					if(Ext.isEmpty(url)) {
						var img = this.down('image');
						img.setSrc(Ext.BLANK_IMAGE_URL);
					}
				}
		}
		
		this.items = [{
			xtype: 'textfield',
			name: this.valueName,
			hidden: true,
			value: '',
			listeners: {
				change: function(self, newValue, oldValue) {
					//console.log("--:url on change,newValue="+newValue);
					var img = this.down('image');
					if(Ext.isEmpty(newValue)) {
						img.setSrc(Ext.BLANK_IMAGE_URL);
	            	}
					else{
						img.setSrc(newValue);
					}
				},
				scope: this
			}
		}, {
			xtype: 'container',
			layout: 'hbox',
			items: [{
				xtype: 'filefield',
				name: this.textName,
				fieldLabel: this.fieldLabel,
		        buttonText: this.btnText,
		        labelWidth: this.labelWidth,
		        msgTarget: 'side',
		        allowBlank: this.allowBlank,
				flex: 1,
				listeners: {
					change: function(self, v) {
						var file = self.fileInputEl.dom.files[0];
						var url = URL.createObjectURL(file);
						var img = this.up('container').down('image');
						img.setSrc(url);
					}
				}
			}, {
				xtype: 'image',
				src: Ext.BLANK_IMAGE_URL,
				//style: 'height: 24px; width: 24px;',
				margin: '0, 0, 0, 5',
        		width: 24,
				win: null,
				listeners: {
					mouseout: {
						element: 'el',
			            fn: function(e, t) {
			            	var img = this.down('image');
							if(img.win) {
								img.win.destroy();
								img.win = null;
							}
			            }
					},
					mouseover: {
						element: 'el',
			            fn: function(e, t) {
			            	var img = this.down('image');
			            	if(img.win) {
			            		return;
			            	}
			            	
			            	var file = this.down('filefield').fileInputEl.dom.files[0];
			            	var url = this.down('textfield').getValue();
			            	if(file) {
			            		url = URL.createObjectURL(file);
			            	}
			            	
			            	if(Ext.isEmpty(url) || url == Ext.BLANK_IMAGE_URL) {
			            		return;
			            	}
			            	
			            	var win = new Ext.Window({
			            		layout:'fit',
			            		width: 300,
			            		//height: 300,
			            		closable: false,
			            		header: false,
			            		items: [{
									xtype: 'image',
									src: Ext.BLANK_IMAGE_URL,
									listeners: {
										click: {
											element: 'el',
											fn: function() {
												var img = this.down('image');
												if(img.win) {
													img.win.destroy();
													img.win = null;
												}
											}
										},
										scope: this
									}
								}]
			            			
			            	});
			            	
			            	win.down('image').setSrc(url);
			            	
			            	img.win = win;
			            	win.show();
			            }
					},
					scope: this
				}
			}]
		}];
		
		this.setValue = function(v) {
			this.down('textfield').setValue(v);
		};
		
		this.callParent();
	}
});