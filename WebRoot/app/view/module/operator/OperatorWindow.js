Ext.define('app.view.module.operator.OperatorWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.operator-window',
	
	reference: 'operator_window',
	
	uses: [
	   'app.ux.form.MultiTextAreaField'
	],
	
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 390,
	scrollable: true,
	title: '新建用户',
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
			name: 'o_id',
			hidden: true,
			value: '-1'
		}, {
			name: 'name',
			allowBlank: false,
			fieldLabel: '*用户名称',
			emptyText: '输入用户名称'
		}, {
			name: 'password',
			allowBlank: false,
			fieldLabel: '*登录密码',
			emptyText: '输入用户密码'
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
				store: '{operatorStatusStore}'
			},
			fieldLabel: '*状 态'
		}, {
			xtype: 'multitextareafield',
			valueName: 'role_ids',
			textName: 'role_names',
			fieldLabel: '角 色',
			margin: '5, 0, 5, 0',
			allowBlank: true,
			onHandler: 'onChoiceRole'
		}, {
			name: 'phone',
			vtype: 'phonenum',
			fieldLabel: '手机号码',
			emptyText: '输入手机号码'
		}, {
			name: 'tel',
			vtype: 'telnum',
			fieldLabel: '电话号码',
			emptyText: '输入电话号码'
		}, {
			name: 'email',
			vtype: 'email',
			fieldLabel: '邮箱地址',
			emptyText: '输入邮箱地址'
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
			btn.up('operator-window').hide();
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