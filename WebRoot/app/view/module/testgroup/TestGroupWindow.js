Ext.define('app.view.module.testgroup.TestGroupWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.testgroup-window',
	
	reference: 'testgroup_window',
	
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 500,
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
			name: 'ug_id',
			hidden: true,
			value: '-1'
		}, {
			name: 'name',
			allowBlank: false,
			fieldLabel: '*测试组名'
		}, {
					xtype: 'combobox',editable: false,

			name: 'type',
			margin: '10, 0, 0, 0',
			allowBlank: false,
			displayField: 's_name',
			valueField:'s_id',
			queryMode: 'local',
			flex: 1,
			value: '',
			bind: {
				store: '{testGroupStatusStore}'
			},
			fieldLabel: '*类 型',
			listeners: {
				change: function(combo, newValue, oldValue, eOpts ) {
					var form = this.up('form');
					var choiceBtn = form.query('button[name=choice_btn]')[0];
					var channelF = form.query("textareafield[name=raw_value]")[0];
				    channelF.setReadOnly(newValue == "channel");

					if (newValue == "zone" || newValue == "model" || newValue == "channel") {
			    		choiceBtn.setHidden(false);
					} else {
						choiceBtn.setHidden(true);
					}
				}
			}
		}, {
			name: 'ids_value',
			reference: 'r_ids_value',
			hidden: true,
			value: ''
		}, {
			xtype: 'container',
			margin: '10, 0, 0, 0',
			layout: {
				type: 'hbox',
				align: 'middle'
			},
			items: [{
				xtype: 'textareafield',
				name: 'raw_value',
				reference: 'r_raw_value',
				allowBlank: true,
				flex: 1,
				fieldLabel: '*值得集合'
			}, {
				xtype: 'button',
				text: '选择',
				name: 'choice_btn',
				width: 60,
				margin: '0, 0, 0, 10',
				rValueName: 'r_ids_value',
				rTextName: 'r_raw_value',
				handler: 'onChoiceRaw'
			}]
		}, {
			xtype: 'textareafield',
			margin: '10, 0, 0, 0',
			name: 'intro',
			fieldLabel: '描述'
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('testgroup-window').hide();
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