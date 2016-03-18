Ext.define('app.view.module.site.SiteWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.site-window',
	
	reference: 'site_window',
	
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 390,
	scrollable: true,
	title: '添加',
	glyph: 0xf007,
	initComponent: function() {
		this.maxHeight = Ext.getBody().getHeight() - 20;
		this.callParent();
	},
	items:[{
		xtype: 'form',
		bodyPadding: 20,
		fieldDefaults: {
	        labelWidth: 68,
			labelAlign: 'right',
	        anchor: '100%'
	    },
	    defaultType: 'textfield',
		items: [{
			name: 's_id',
			hidden: true,
			value: '-1'
		}, {
			name: 'name',
			allowBlank: false,
			fieldLabel: '*站点名称',
			emptyText: '输入站点名称'
		}, {
					xtype: 'combobox',editable: false,

			name: 'status',
			allowBlank: false,
			displayField: 's_name',
			valueField:'s_id',
			queryMode: 'local',
			flex: 1,
			value: '1',
			bind: {
				store: '{siteStatusStore}'
			},
			fieldLabel: '*状 态'
		}, {
			xtype: 'datefield',
			name: 'active_time',
			allowBlank: false,
			flex: 1,
			fieldLabel: '*生效时间',
			value: new Date(),
			format: 'Y-m-d H:i:s'
		}, {
			xtype: 'datefield',
			name: 'deactive_time',
			allowBlank: false,
			flex: 1,
			fieldLabel: '*失效时间',
			value: Ext.Date.add(new Date(), Ext.Date.YEAR, 50),
			format: 'Y-m-d H:i:s'
		}, {
			xtype: 'textareafield',
			name: 'intro',
			fieldLabel: '用户简介',
			emptyText: '输入用户简介'
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('site-window').hide();
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