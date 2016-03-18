Ext.define('app.view.module.contsalepaytype.PayTypeWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.contsale-paytype-window',
	
	reference: 'contsale_paytype_window',
	uses: [
	       'app.ux.form.PreviewFileField'
	],

	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
    width : 400,
	scrollable: true,
	title: '添加',
	glyph: 0xf007,
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;

		var id_mod = new Ext.form.TextField({
			layout:'form',
			bodyStyle:'padding:6 0 0 100',
			xtype: 'textfield',
//			id: 'id',
			name:'id',
			value:'-1',
			width: 255,
			fieldLabel: 'id',
			disabled:false,
			readOnly:false,
			hidden: false,
			hideLabel:false
		});
		var name_mod = new Ext.form.TextField({
			layout:'form',
			bodyStyle:'padding:6 0 0 100',
			xtype: 'textfield',
			name:'name',
			width: 255,
			fieldLabel: '名称'
		});

		var pay_type_status_store = new Ext.data.JsonStore({
			fields:['contTypeId', 'contTypeName'],
			autoLoad: true,
			proxy: {
				type:'ajax',
				url: 'paytypes/queryStatus.do',
				reader: { root:'contType_data'}
			}
		});

		var pay_type_mod = new Ext.form.ComboBox({
			fieldLabel: '支付类型',
			emptyText: '请选择支付类型...',
			xtype:'combo',
			name: 'pay_type',
			hiddenName: 'pay_type',
			//width:120,
			maxHeight:180,
			hideLabel:false,
			displayField:'contTypeName',
			valueField:'contTypeId',
			//readOnly:true,
			editable: false,
			queryMode:'remote',
			triggerAction:'all',
			store:pay_type_status_store
		});

		var has_qrcode_mod=new Ext.form.Checkbox({
//			id:'has_qrcode',
			name:'has_qrcode',
			width:160,//宽度
//			height:compHeight,
			align:'left',
			fieldLabel:'支持二维码',
			value: 0,
			inputValue:1
		});
		var service_hotline_mod = new Ext.form.TextField({
			layout:'form',
			bodyStyle:'padding:6 0 0 100',
			xtype: 'textfield',
			name:'service_hotline',
			width: 255,
			fieldLabel: '订购电话'
		});
		var description_mod = new Ext.form.TextField({
			layout:'form',
			bodyStyle:'padding:6 0 0 100',
			xtype: 'textfield',
			name:'description',
			width: 255,
			fieldLabel: '说明'
		});
	    var modifyForm = new Ext.FormPanel({
			xtype: 'form',
			bodyPadding: 20,
			fieldDefaults: {
		        labelWidth: 68,
				labelAlign: 'right',
		        anchor: '100%'
		    },
	        items:[id_mod,name_mod,pay_type_mod
	        	,has_qrcode_mod,service_hotline_mod,
	        	description_mod
	        	, {
	    			xtype: 'previewfilefield',
	    			margin: '10, 0, 10, 0',
	    			textName: 'c_img_icon_file',
	    			valueName: 'c_img_icon_url',
	    			btnText: '选择',
	    			fieldLabel: '图标',
	    	        labelWidth: 68,
	    	        msgTarget: 'side',
	    	        anchor: '100%'
	    		}, {
					bodyStyle:'padding:12 0 0 0',
					layout:'form',
					style:'text-align:center',
					items:[{xtype:'label',text:'注意：当不支持二维码时，订购电话不能为空！当支付类型是电话时，图标是服务电话图;其他支付类型，图标对应的是支付类型对应的图标，如微信、淘宝等',style:'background-color:#ffff00;font-size: 13px;'}]
				}
	        ]
	    });

	    this.items = [modifyForm];
		this.callParent();
	},
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('contsale-paytype-window').hide();
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