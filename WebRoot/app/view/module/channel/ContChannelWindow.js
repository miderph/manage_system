Ext.define('app.view.module.channel.ContChannelWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cont-channel-window',
	
	reference: 'cont_channel_window',
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
			fieldLabel: '*渠道名称',
			emptyText: '输入渠道名称'
		}, {
					xtype: 'combobox',editable: false,

			name: 'type',
			allowBlank: false,
			displayField: 's_name',
			valueField:'s_id',
			queryMode: 'local',
			flex: 1,
			value: 'apk',
			bind: {
				store: '{channelTypeStore}'
			},
			fieldLabel: '*类型',
			listeners: {
				change: function(combo, newValue, oldValue, eOpts ) {
					var form = this.up('form');
					var channelF = form.query("textfield[name=channel]")[0];
                    if (newValue == "apk"){
                    	channelF.allowBlank = false;
                    	channelF.setDisabled(false);
                    }
                    else{
                    	channelF.allowBlank = true;
                    	channelF.setDisabled(true);
                    }
				}
			}
		}, {
			name: 'channel',
			allowBlank: true,
			fieldLabel: '*渠道号',
			emptyText: '输入渠道号'
		}, {
//			xtype: 'textareafield',
//			name: 'url_template',
//			fieldLabel: '二维码模板',
//			emptyText: '输入二维码模板'
//		}, {
			xtype: 'textareafield',
			name: 'intro',
			fieldLabel: '渠道简介',
			emptyText: '输入渠道简介'
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('cont-channel-window').hide();
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