Ext.define('app.view.module.shop.ContShopWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cont-shop-window',
	
	reference: 'cont_shop_window',
	uses: [
	       'app.ux.form.PreviewFileField'
	],

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
			fieldLabel: '*商铺名称',
			emptyText: '输入商铺名称'
		}, {
			name: 'credit',
			allowBlank: false,
			fieldLabel: '商铺信用度',
			emptyText: '输入商铺信用度'
		}, {
			xtype: 'textareafield',
			name: 'hot_info',
			fieldLabel: '促销信息(将显示在客户端商品详情页面)',
			emptyText: '输入促销信息'
		}, {
			xtype: 'textareafield',
			name: 'intro',
			fieldLabel: '商铺简介',
			emptyText: '输入商铺简介'
		}, {
			xtype: 'previewfilefield',
			margin: '10, 0, 10, 0',
			textName: 'c_img_icon_file',
			valueName: 'c_img_icon_url',
			btnText: '选择',
			fieldLabel: '图标',
	        labelWidth: 120,
	        msgTarget: 'side',
	        anchor: '100%'
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('cont-shop-window').hide();
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