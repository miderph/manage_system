Ext.define('app.view.module.column.ContRuleFormWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.contruleform-window',
	
	reference: 'contruleform_window',
	
	uses: ['app.ux.form.MultiTextAreaField'],
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 460,
	scrollable: true,
	title: '新建',
	glyph: 0xf007,
	contRuleStore: null,
	
	initComponent: function() {
		this.maxHeight = Ext.getBody().getHeight() - 20;
		var store = Ext.create('app.store.RuleRelaTypeStore');
		var storeSimple = Ext.create('app.store.RuleRelaTypeSimpleStore');
		this.items = [{
			xtype: 'form',
			bodyPadding: 20,
			fieldDefaults: {
		        labelWidth: 108,
				labelAlign: 'right',
		        anchor: '100%'
		    },
		    defaultType: 'textfield',
			items: [{
				name: 'menu_id',
				hidden: true,
				value : '-1'
			},{
				name: 'rule_id',
				hidden: true,
				value: '-1'
			}, {
				name: 'name',
				fieldLabel : '名称',
				allowBlank: false,
				value: ''
			}, {
				name: 'price',
				fieldLabel: '价格',
				allowBlank: true,
				value: ''
			}, {
				name: 'price_rela',
				fieldLabel: '价格取值关系',
				xtype : 'combobox',
				store: store,
				queryMode: 'local',
				displayField: 'name',
				valueField: 'value',
				allowBlank: true,
				value: '',
				listeners: {
					'select' :  'onPriceRelaSelect'
				}
			},{
				name: 'price_right',
				fieldLabel: '右侧价格',
				allowBlank: true,
				hidden: true,
				value: ''
			},{
				xtype: 'multitextareafield',
				valueName: 'provider_ids',
				textName: 'provider_names',
				fieldLabel: '提供商',
				emptyText: '选择提供商',
				margin: '5, 0, 0, 0',
				allowBlank: true,
				onHandler: 'onChoiceProvider'
			},{
				name: 'provider_rela',
				fieldLabel: '提供商取值关系',
				xtype : 'combobox',
				store: storeSimple,
				queryMode: 'local',
				displayField: 'name',
				valueField: 'value',
				allowBlank: true,
				value: ''
			},{
				xtype: 'multitextareafield',
				valueName: 'shop_ids',
				textName: 'shop_names',
				fieldLabel: '商铺',
				emptyText: '选择商铺',
				margin: '5, 0, 0, 0',
				allowBlank: true,
				onHandler: 'onChoiceShop'
			},{
				name: 'shop_rela',
				fieldLabel: '商铺取值关系',
				allowBlank: true,
						xtype: 'combobox',editable: false,

				store:  storeSimple,
				queryModel: 'local',
				displayField: 'name',
				valueField: 'value',
				value : ''
			},{
				name: 'category',
				fieldLabel: '分类',
				allowBlank: true,
				value: ''
			},{
				name : 'category_rela',
				fieldLabel: '分类取值关系',
				allowBlank: true,
				xtype : 'combobox',
				store: storeSimple,
				queryMode: 'local',
				displayField: 'name',
				valueField: 'value',
				value: ''
			}, {
				name: 'category_new_menu',
				fieldLabel: '是否创建子栏目',
				xtype : 'checkboxfield',
				value: 0,
				inputValue:1,
				flex: 1
			}]
		}];
		
		this.buttons = [{
			text: '提交',
			scope: this,
			handler: function() {
				var win = this;
				var form = this.down('form');
		    	var values = form.getValues();
		    	
		    	var url = 'contrule/update.do';
		    	if(values.rule_id === "-1") {
		    		url = 'contrule/save.do';
		    	}
		    	
		    	if (form.isValid()){
		    		win.mask('正在保存...');
			    	form.submit({
			    		clientValidation: true,
			    	    url: url,
			    		params: values,
			    		submitEmptyText: false,
			    		success: function(form, action) {
			    			if(action.result.issuc) {
			    				form.reset();
			    				win.unmask();
			    				win.contRuleStore.reload();
			    				win.hide();
			    			}
			    			
			    			Ext.toast({
			     				html: action.result.msg,
			     				title: '提示',
			     				saveDelay: 10,
			     				align: 'tr',
			     				closable: true,
			     				width: 200,
			     				useXAxis: true,
			     				slideInDuration: 500
			     			});
			    		}
			    	});
		    	}
			}
		}, {
			text: '取消',
			scope: this,
			handler: function() {
				this.hide();
			}
		}];
		
		this.callParent();
	},
	buttonAlign: 'center',
	listeners: {
		resize: function(win, width, height) {
			var bodyH = Ext.getBody().getHeight();
			var y = (bodyH-height)/2;
			win.setY(y);
			win.setMaxHeight(bodyH-20);
		}
	}
});