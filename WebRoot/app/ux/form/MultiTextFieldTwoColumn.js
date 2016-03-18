Ext.define('app.ux.form.MultiTextFieldTwoColumn', {
	extend: 'Ext.container.Container',
	alias: 'widget.multitextfieldtwocolumn',
	
	initComponent: function() {
		
		this.items = [{
			xtype: 'textfield',
			name: this.valueName,
			reference: 'r_' + this.valueName,
			hidden: true,
			value: ''
		},{
			xtype: 'textfield',
			name: this.valueName2,
			reference: 'r_' + this.valueName2,
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
			},{
				xtype: 'textfield',
				name: this.textName2,
				reference: 'r_' + this.textName2,
				readOnly: true,
				flex: 1,
				fieldLabel: this.fieldLabel2,
				emptyText: this.emptyText2
			}, {
				xtype: 'button',
				text: '选择',
				width: 60,
				margin: '0, 0, 0, 10',
				rValueName: 'r_' + this.valueName2,
				rTextName: 'r_' + this.textName2,
				handler: this.onHandler
			}]
		}];
		
		this.callParent();
	}
});