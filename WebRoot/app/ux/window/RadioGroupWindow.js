Ext.define('app.ux.window.RadioGroupWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.radiogroup-window',
	
	reference: 'radiogroup_window',
	
	closable: false,
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	scrollable: true,
	width: 360,
	title: '',
	glyph: 0xf007,
	onSubmit: function(w, cArr){},
	onCancel: function(w){},
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.buttonAlign = 'center';
		this.buttons = [{
			text: '确定',
			scope: this,
			handler: function() {
				var checked = this.down('checkboxgroup').getChecked();
				/*var cArr = [];
				Ext.Object.each(checked, function(key, value, myself) {
					
					cArr.push(key);
				});*/
				var tArr = [];
				var vArr = [];
				Ext.Array.each(checked, function(item, index) {
					tArr.push(item.boxLabel);
					vArr.push(item.name);
				});
				
				this.onSubmit(this, vArr, tArr);
				
			}
		}, {
			text: '取消',
			scope: this,
			handler: function() {
				this.onCancel(this);
			}
		}];
		
		this.callParent();
		
		var allData = this.allData;
		var checkedData = this.checkedData;
		
		var group = this.down('checkboxgroup');
		
		Ext.Array.each(allData, function(item, index) {
			var checked = false;
			if(Ext.Array.contains(checkedData, item.value)) {
				checked = true;
			}
			
			group.add({
				boxLabel: item.text,
				name: item.value,
				checked: checked
			});
		});
		
		var cItems = this.down('form').items.getAt(0).items;
		cItems.getAt(0).setHandler(function() {
			var items = this.down('checkboxgroup').items;
			items.each(function(item){
				item.setValue(true);
			});
		}, this);
		
		cItems.getAt(1).setHandler(function() {
			var items = this.down('checkboxgroup').items;
			items.each(function(item){
				item.setValue(!item.getValue());
			});
		}, this);
		
		cItems.getAt(2).setHandler(function() {
			var checkedData = this.checkedData;
			var items = this.down('checkboxgroup').items;
			items.each(function(item){
				var checked = false;
				if(Ext.Array.contains(checkedData, item.name)) {
					checked = true;
				}
				
				item.setValue(checked);
			});
		}, this);
		
	},
	items:[{
		xtype: 'form',
		bodyPadding: 10,
		fieldDefaults: {
	        labelWidth: 68,
			labelAlign: 'right',
	        anchor: '100%'
	    },
	    defaultType: 'textfield',
		items: [{
			xtype: 'container',
			layout: 'hbox',
			items: [{
				xtype: 'button',
				text: '全选',
				width: 60
			}, {
				xtype: 'button',
				text: '反选',
				width: 60,
				margin: '0, 0, 0, 5'
			}, {
				xtype: 'button',
				text: '重置',
				width: 60,
				margin: '0, 0, 0, 5'
			}]
		}, {
			xtype: 'checkboxgroup',
			columns: 3,
            items: []
		}]
	}]
});