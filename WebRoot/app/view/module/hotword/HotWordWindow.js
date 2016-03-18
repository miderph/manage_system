Ext.define('app.view.module.hotword.HotWordWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.hotword-window',
	
	reference: 'hotword_window',
	
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 260,
	scrollable: true,
	title: '添加',
	glyph: 0xf007,
	initComponent: function() {
		this.maxHeight = Ext.getBody().getHeight() - 20;
		this.callParent();
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
			name: 'hw_id',
			hidden: true,
			value: '-1'
		}, {
			name: 'hotword',
			allowBlank: false,
			fieldLabel: '*热词名称'
		}, {
			hidden: true,
					xtype: 'combobox',editable: false,

			name: 'site_id',
			allowBlank: false,
			displayField: 's_name',
			valueField:'s_id',
			queryMode: 'local',
			flex: 1,
			value: '',
			bind: {
				store: '{siteAllStore}'
			},
			fieldLabel: '*站 点'
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('hotword-window').hide();
		}
	}],
	listeners: {
		resize: function(win, width, height) {
			var bodyH = Ext.getBody().getHeight();
			var y = (bodyH-height)/2;
			win.setY(y);
			win.setMaxHeight(bodyH-20);
		}
	}
});