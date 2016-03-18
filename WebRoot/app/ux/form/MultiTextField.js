Ext.define('app.ux.form.MultiTextField', {
	extend: 'Ext.container.Container',
	alias: 'widget.multitextfield',
	
	initComponent: function() {
		
		this.items = [{
			xtype: 'textfield',
			name: this.valueName,
			reference: 'r_' + this.valueName,
			hidden: true,
			value: ''
		}, {
			xtype: 'container',
			layout: 'hbox',
			items: [{
				xtype: 'textfield',
				name: this.textName,
				reference: 'r_' + this.textName,
				readOnly: true,
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