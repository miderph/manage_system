Ext.define('app.ux.form.MultiTextAreaField', {
	extend: 'Ext.container.Container',
	alias: 'widget.multitextareafield',
	
	initComponent: function() {
		
		this.items = [{
			xtype: 'textfield',
			name: this.valueName,
			reference: 'r_' + this.valueName,
			hidden: true,
			value: ''
		}, {
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'middle'
			},
			items: [{
				xtype: 'textareafield',
				name: this.textName,
				reference: 'r_' + this.textName,
				readOnly: true,
				allowBlank: this.allowBlank ? true:false,
				flex: 1,
				fieldLabel: this.fieldLabel,
				emptyText: this.emptyText
			}, {
				xtype: 'button',
				text: '选择',
				width: 60,
				margin: '0, 0, 0, 10',
				rValueName: 'r_' + this.valueName,
				rTextName: 'r_' + this.textName,
				handler: this.onHandler
			}]
		}];
		
		this.callParent();
	}
});