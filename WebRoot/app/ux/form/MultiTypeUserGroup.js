Ext.define('app.ux.form.MultiTypeUserGroup', {
	extend: 'Ext.container.Container',
	alias: 'widget.multitypeusergroup',
	uses: [
       'app.ux.form.MultiTextFieldTwoColumn'
    ],
	initComponent: function() {
		
		this.items = [{
				xtype: 'fieldset',
		        title: '数据过滤（需选择测试组）',
		        collapsible: true,
		        defaultType: 'textfield',
		        fieldName: 'usergroupinfo',
				margin: '10, 2, 0, 0',
		        items: [{
					xtype: 'multitextfieldtwocolumn',
					valueName: 'usergroup_ids_mac',
					textName: 'usergroup_names_mac',
					fieldLabel: '网卡地址白名单',
					emptyText: '选择网卡地址测试组',
					valueName2: 'usergroup_ids_mac2',
					textName2: 'usergroup_names_mac2',
					fieldLabel2: '网卡地址黑名单',
					emptyText2: '选择网卡地址测试组',
					margin: '10, 0, 0, 0',
					onHandler: 'onChoiceMac'
				}, {
					xtype: 'multitextfieldtwocolumn',
					valueName: 'usergroup_ids_zone',
					textName: 'usergroup_names_zone',
					fieldLabel: '地区白名单',
					emptyText: '选择地区测试组',
					valueName2: 'usergroup_ids_zone2',
					textName2: 'usergroup_names_zone2',
					fieldLabel2: '地区黑名单',
					emptyText2: '选择地区测试组',
					margin: '10, 0, 0, 0',
					onHandler: 'onChoiceZone'
				}, {
					xtype: 'multitextfieldtwocolumn',
					valueName: 'usergroup_ids_model',
					textName: 'usergroup_names_model',
					fieldLabel: '型号白名单',
					emptyText: '选择型号测试组',
					valueName2: 'usergroup_ids_model2',
					textName2: 'usergroup_names_model2',
					fieldLabel2: '型号黑名单',
					emptyText2: '选择型号测试组',
					margin: '10, 0, 10, 0',
					onHandler: 'onChoiceModel'
				}, {
					xtype: 'multitextfieldtwocolumn',
					valueName: 'usergroup_ids_channel',
					textName: 'usergroup_names_channel',
					fieldLabel: '渠道白名单',
					emptyText: '选择渠道测试组',
					valueName2: 'usergroup_ids_channel2',
					textName2: 'usergroup_names_channel2',
					fieldLabel2: '渠道黑名单',
					emptyText2: '选择渠道测试组',
					margin: '10, 0, 0, 0',
					onHandler: 'onChoiceChannel'
				}]
			}];
		
		this.callParent();
	}
});