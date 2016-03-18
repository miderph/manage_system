Ext.define('app.view.module.provider.ContProviderWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cont-provider-window',
	
	reference: 'cont_provider_window',
	
	uses: [
       'app.ux.form.MultiTextField',
       'app.ux.form.PreviewFileField'
    ],
	
    closable: true,
	closeAction: 'hide',
	resizable: true,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	scrollable: true,
	title: '添加提供商',
	glyph: 0xf007,
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.callParent();
		
	},
	items:[{
		xtype: 'form',
		bodyPadding: 20,
		fieldDefaults: {
	        labelWidth: 120,
			labelAlign: 'right'
	    },
		items: [{
			xtype: 'textfield',
			name: 'cp_id',
			hidden: true,
			value: '-1'
		}, {
			xtype: 'container',
			layout: 'hbox',
			defaultType: 'textfield',
			items: [{
				name: 'name',
				fieldLabel: '*提供商名称',
				allowBlank: false,
				flex: 1,
				emptyText: '输入提供商名称'
			}, {
				name: 'description',
				fieldLabel: '说明',
				flex: 1,
				emptyText: '输入说明'
			}]
		}, {
			xtype: 'container',
			layout: 'hbox',
			margin: '10, 0, 0, 0',
			items: [{
						xtype: 'combobox',editable: false,

				name: 'cont_provider_id',
				displayField: 'cp_name',
				valueField:'cp_id',
				queryMode: 'local',
				flex: 1,
				value: '-10000',
				bind: {
					store: '{contProviderByAuthStore}'
				},
				fieldLabel: '对应内容提供方',
				emptyText: '选择对应内容提供方'
			}, {
				xtype: 'radiogroup',
				fieldLabel: '默认站点',
				flex: 1,
				items: [
	                {boxLabel: '是', name: 'isdefault', inputValue: 1},
	                {boxLabel: '否', name: 'isdefault', inputValue: 0, checked: true}
	            ]
			}]
		}, {
			xtype: 'container',
			layout: 'hbox',
			margin: '10, 0, 0, 0',
			items: [{
						xtype: 'combobox',editable: false,

				name: 'chn_provider_id',
				displayField: 'cp_name',
				valueField:'cp_id',
				queryMode: 'local',
				flex: 1,
				value: '-10000',
				bind: {
					store: '{contProviderByAuthStore}'
				},
				fieldLabel: '频道提供方',
				emptyText: '选择频道提供方'
			}, {
				xtype: 'textfield',
				name: 'epg_priority',
				fieldLabel: 'EPG提供商优先级',
				flex: 1,
				value: 0,
				regex: /^\d{1,5}$/,
				regexText: '只能输入五位以内的数字',
				emptyText: '输入EPG提供商优先级'
			}]
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
		}, {
			xtype: 'multitextfield',
			valueName: 'pay_type_ids',
			textName: 'pay_type_names',
			fieldLabel: '支付方式',
			emptyText: '选择支付方式',
			margin: '10, 0, 10, 0',
			onHandler: 'onPayType'
		}, {
			xtype: 'textareafield',
			margin: '10, 0, 10, 0',
			anchor: '100%',
			name: 'hot_info',
			fieldLabel: '促销信息(将显示在客户端商品详情页面)',
			emptyText: '请输入促销信息'
		}, {
			xtype: 'radiogroup',
			fieldLabel: '提供商状态',
			margin: '10, 0, 0, 0',
			flex: 1,
			items: [
                {boxLabel: '禁用', name: 'p_status',inputValue:'-1'},
    			{boxLabel: '待审', name: 'p_status',inputValue:'0', checked : true},
    			{boxLabel: '正常-普通', name: 'p_status',inputValue:'1'},
    			{boxLabel: '正常-高级', name: 'p_status',inputValue:'2'}
            ]
		}, {
			xtype: 'container',
			layout: 'hbox',
			margin: '10, 0, 0, 0',
			items: [{
				xtype: 'radiogroup',
				fieldLabel: 'XMPP',
				flex: 1,
				items: [
	                {boxLabel: '智联TV', name: 'xmpp_index',inputValue:'1'},
					{boxLabel: '华数', name: 'xmpp_index',inputValue:'0'},
					{boxLabel: '空', name: 'xmpp_index',inputValue:'',checked : true }
	            ]
			}, {
				xtype: 'radiogroup',
				fieldLabel: 'UAP',
				flex: 1,
				items: [
	                {boxLabel: '需要', name: 'need_check_uap',inputValue:'1'},
	                {boxLabel: '不需要', name: 'need_check_uap',inputValue:'0', checked : true}
	            ]
			}]
		}, {
			xtype: 'container',
			layout: 'hbox',
			margin: '10, 0, 0, 0',
			items: [{
				xtype: 'radiogroup',
				fieldLabel: '直播切屏',
				flex: 1,
				items: [
	                {boxLabel: '能', name: 'can_switchtv',inputValue:'1'},
	                {boxLabel: '不能', name: 'can_switchtv',inputValue:'0', checked : true}
	            ]
			}, {
				xtype: 'radiogroup',
				fieldLabel: '直播节目录制',
				flex: 1,
				items: [
	                {boxLabel: '能', name: 'can_recording',inputValue:'1'},
	                {boxLabel: '不能', name: 'can_recording',inputValue:'0', checked : true}
	            ]
			}]
		}, {
			xtype: 'container',
			layout: 'hbox',
			margin: '10, 0, 0, 0',
			items: [{
				xtype: 'radiogroup',
				fieldLabel: '点播切屏',
				flex: 1,
				items: [
	                {boxLabel: '能', name: 'can_playvideo',inputValue:'1'},
	                {boxLabel: '不能', name: 'can_playvideo',inputValue:'0', checked : true}
	            ]
			}, {
				xtype: 'radiogroup',
				fieldLabel: '直播节目时移',
				flex: 1,
				items: [
	                {boxLabel: '能', name: 'can_timeshift',inputValue:'1'},
	                {boxLabel: '不能', name: 'can_timeshift',inputValue:'0', checked : true}
	            ]
			}]
		}, {
			xtype: 'container',
			layout: 'hbox',
			margin: '10, 0, 0, 0',
			items: [{
				xtype: 'radiogroup',
				fieldLabel: '云盘下载',
				flex: 1,
				items: [
	                {boxLabel: '能', name: 'can_download',inputValue:'1'},
	                {boxLabel: '不能', name: 'can_download',inputValue:'0', checked : true}
	            ]
			}, {
				xtype: 'radiogroup',
				fieldLabel: '回放',
				flex: 1,
				items: [
					{boxLabel: '能', name: 'can_playback',inputValue:'1'},
					{boxLabel: '不能', name: 'can_playback',inputValue:'0', checked : true}
	            ]
			}]
		}, {
			xtype: 'container',
			layout: 'hbox',
			margin: '10, 0, 0, 0',
			items: [{
						xtype: 'combobox',editable: false,

				name: 'site_id',
				margin: '10, 0, 0, 0',
				displayField: 's_name',
				valueField:'s_id',
				queryMode: 'local',
				flex: 1,
				value: '',
				bind: {
					store: '{siteAllStore}'
				},
				fieldLabel: '对应站点',
				emptyText: '选择对应站点'
			}, {
				xtype: 'radiogroup',
				fieldLabel: '下载地址优先',
				flex: 1,
				items: [
	                {boxLabel: '是', name: 'is_apk_prior', inputValue: 1},
	                {boxLabel: '否', name: 'is_apk_prior', inputValue: 0, checked: true}
	            ]
			}]
		}, {
			xtype: 'multitextfield',
			valueName: 'stb_prefix_ids',
			textName: 'stb_prefix',
			fieldLabel: 'Prefix前缀',
			emptyText: '选择Prefix前缀',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoiceStbPrefix'
		}, {
			xtype: 'multitextfield',
			valueName: 'cont_provider_ids',
			textName: 'cont_provider_names',
			fieldLabel: '搜索cont提供商',
			emptyText: '选择搜索cont提供商',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoice'
		}, {
			xtype: 'multitextfield',
			valueName: 'epg_provider_ids',
			textName: 'epg_provider_names',
			fieldLabel: '搜索epg提供商',
			emptyText: '选择搜索epg提供商',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoice'
		}, {
			xtype: 'multitextfield',
			valueName: 'can_switchtv_pids',
			textName: 'can_switchtv_names',
			fieldLabel: '直播切屏播放提供商',
			emptyText: '选择直播切屏播放提供商',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoice'
		}, {
			xtype: 'multitextfield',
			valueName: 'can_playvideo_pids',
			textName: 'can_playvideo_names',
			fieldLabel: '点播切屏播放提供商',
			emptyText: '选择点播切屏播放提供商',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoice'
		}, {
			xtype: 'multitextfield',
			valueName: 'can_download_pids',
			textName: 'can_download_names',
			fieldLabel: '云盘下载提供商',
			emptyText: '选择云盘下载提供商',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoice'
		}, {
			xtype: 'multitextfield',
			valueName: 'can_recording_pids',
			textName: 'can_recording_names',
			fieldLabel: '直播节目录制提供商',
			emptyText: '选择直播节目录制提供商',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoice'
		}, {
			xtype: 'multitextfield',
			valueName: 'can_timeshift_pids',
			textName: 'can_timeshift_names',
			fieldLabel: '直播节目时移提供商',
			emptyText: '选择直播节目时移提供商',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoice'
		}, {
			xtype: 'multitextfield',
			valueName: 'can_playback_pids',
			textName: 'can_playback_names',
			fieldLabel: '直播节目回放提供商',
			emptyText: '选择直播节目回放提供商',
			margin: '10, 0, 0, 0',
			onHandler: 'onChoice'
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: function(btn) {
			btn.up('cont-provider-window').hide();
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