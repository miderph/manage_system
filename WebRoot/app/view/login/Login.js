Ext.define('app.view.login.Login', {
	extend: 'Ext.window.Window',

	requires: ['app.view.login.LoginController'],

	controller: 'login',
	
	initComponent: function() {
    	Ext.setGlyphFontFamily('FontAwesome');
    	
    	Ext.getBody().on('keyup', function(e, t) {
    		if(e.getKey() == Ext.event.Event.ENTER) {
    			this.getController().onLoginBtnClick();
    		}
    	}, this);
    	
    	this.callParent();
    },
	
	closable: false,
	resizable: false,
	modal: true,
	draggable: false,
	autoShow: true,
	title: '用户登录',
	glyph: 0xf007,
	items:[{
		xtype: 'form',
		reference: 'form',
		bodyPadding: 20,
		baseCls:'x-plain',
		items: [{
			xtype: 'textfield',
			name: 'username',
			labelWidth: 50,
			fieldLabel: '用户名',
			allowBlank: false,
			emptyText: '请输入用户名'
		}, {
			xtype: 'textfield',
			name: 'password',
			labelWidth: 50,
			inputType: 'password',
			fieldLabel: '密  码',
			allowBlank: false,
			emptyText: '请输入您的密码'
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		name: 'loginbutton',
		text: '用户登录',
		glyph: 0xf110,
		region: 'center',
		listeners: {
			click: 'onLoginBtnClick'
		}
	}]
});