Ext.define('app.ux.form.ColumnShortcutTextField', {
	extend: 'Ext.container.Container',
	alias: 'widget.column-shortcut-textfield',
	
	initComponent: function() {
		
		this.items = [{
			xtype: 'textfield',
			name: this.valueName,
			reference: 'r_' + this.valueName,
			hidden: true,
			value: '-1'
		}, {
			xtype: 'container',
			layout: 'hbox',
			items: [{
				xtype: 'textfield',
				name: this.textName,
				reference: 'r_' + this.textName,
				readOnly: true,
				value: '',
				flex: 1,
				fieldLabel: this.fieldLabel,
				emptyText: this.emptyText
			}, {
				xtype: 'button',
				text: '选择',
				width: 120,
				margin: '0, 0, 0, 10',
				disabled: false,
				rTag: 'no',
				rValueName: 'r_' + this.valueName,
				rTextName: 'r_' + this.textName,
				handler: this.onHandler
			}]
		}];
		
		this.callParent();
	}
});