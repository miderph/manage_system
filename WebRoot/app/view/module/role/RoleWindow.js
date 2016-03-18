Ext.define('app.view.module.role.RoleWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.role-window',
	
	reference: 'role_window',
	
	uses: ['app.ux.form.MultiTextAreaField'],
	
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 360,
	scrollable: true,
	title: '新建角色',
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
			name: 'r_id',
			hidden: true,
			value: '-1'
		}, {
			name: 'name',
			allowBlank: false,
			fieldLabel: '*角色名称',
			emptyText: '输入角色名称'
		}, {
			xtype: 'multitextareafield',
			valueName: 'site_ids',
			textName: 'site_names',
			fieldLabel: '站点',
			emptyText: '选择站点',
			allowBlank: true,
			onHandler: 'onChoiceSite'
		}, {
			xtype: 'multitextareafield',
			valueName: 'provider_ids',
			textName: 'provider_names',
			fieldLabel: '提供商',
			emptyText: '选择提供商',
			margin: '5, 0, 0, 0',
			allowBlank: true,
			onHandler: 'onChoiceProvider'
		}, {
			xtype:'label',margin: '10, 0, 0, 0',text:'注意：模块【角色管理、站点管理、供应商管理和前缀管理】只能由admin管理！',style:'background-color:#ffff00;font-size: 13px; margin-left: 50px;'
		},{
			xtype: 'multitextareafield',
			valueName: 'module_ids',
			textName: 'module_names',
			fieldLabel: '模块',
			emptyText: '选择模块',
			margin: '5, 0, 0, 0',
			allowBlank: false,
			onHandler: 'onChoiceModule'
		}, {
			xtype: 'multitextareafield',
			valueName: 'menu_ids',
			textName: 'menu_names',
			fieldLabel: '链接栏目',
			emptyText: '选择链接栏目',
			margin: '5, 0, 0, 0',
			allowBlank: true,
			onHandler: 'onChoiceLinkMenu'
		}, {
			xtype: 'multitextareafield',
			valueName: 'group_ids',
			textName: 'group_names',
			fieldLabel: '测试组',
			emptyText: '选择测试组',
			margin: '5, 0, 0, 0',
			allowBlank: true,
			onHandler: 'onChoiceUserGroup'
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('role-window').hide();
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