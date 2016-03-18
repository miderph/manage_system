Ext.define('app.ux.form.MultiImgGroup', {
	extend: 'Ext.container.Container',
	alias: 'widget.multiimggroup',
	uses: [
       'app.ux.form.PreviewFileField'
    ],
	initComponent: function() {
		
		this.items = [{
			xtype: 'fieldset',
	        title: '图片信息',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'imginfo',
	        hidden: true,
	        bind: {
	        	hidden: '{!hasImgInfo}'
	        },
	        items: [{
				name: 'c_img_id',
				hidden: true,
				value: '-1'
			}, {
				xtype: 'container',
				layout: 'hbox',
				items: [{
					xtype: 'checkboxfield',
					name: 'c_img_locked',
					fieldLabel: '图片信息锁定',
					value: 0,
					inputValue:1,
					flex: 1
				}, {
					xtype: 'datefield',
					name: 'c_img_active_time',
					allowBlank: false,
					flex: 1,
					fieldLabel: '*生效时间',
					value: new Date(),
					format: 'Y-m-d H:i:s'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'c_img_plat_group',
					allowBlank: false,
					displayField: 'plat_groupName',
					valueField:'plat_groupId',
					queryMode: 'local',
					flex: 1,
					value: '1',
					bind: {
						store: '{imgPlatStore}'
					},
					fieldLabel: '*所属平台',
					emptyText: '选择平台'
				}, {
					xtype: 'datefield',
					name: 'c_img_deactive_time',
					allowBlank: false,
					flex: 1,
					fieldLabel: '*失效时间',
					value: Ext.Date.add(new Date(), Ext.Date.YEAR, 50),
					format: 'Y-m-d H:i:s'
				}]
			}, {
				xtype: 'radiogroup',
				fieldLabel: '推荐位图片类型',
				margin: '10, 0, 0, 0',
				flex: 1,
				items: [
	                {boxLabel: '横图', name: 'c_img_rec_postion', inputValue:'1'},
				    {boxLabel: '竖图', name: 'c_img_rec_postion', inputValue:'0'},
	    			{boxLabel: '小方图', name: 'c_img_rec_postion', inputValue:'2'},
	    			{boxLabel: '四格图', name: 'c_img_rec_postion', inputValue:'3'},
	    			{boxLabel: '六格图', name: 'c_img_rec_postion', inputValue:'4'}
	            ]
			}, {
				xtype: 'previewfilefield',
				margin: '10, 0, 10, 0',
				textName: 'c_img_file',
				valueName: 'c_img_url',
				btnText: '选择',
				fieldLabel: '横图',
		        labelWidth: 120,
		        msgTarget: 'side',
		        anchor: '100%'
			}, {
				xtype: 'previewfilefield',
				margin: '10, 0, 10, 0',
				textName: 'c_img_little_file',
				valueName: 'c_img_little_url',
				btnText: '选择',
				fieldLabel: '竖图',
		        labelWidth: 120,
		        msgTarget: 'side',
		        anchor: '100%'
			}, {
				xtype: 'previewfilefield',
				margin: '10, 0, 10, 0',
				textName: 'c_img_icon_file',
				valueName: 'c_img_icon_url',
				btnText: '选择',
				fieldLabel: '小方图',
		        labelWidth: 120,
		        msgTarget: 'side',
		        anchor: '100%'
			}, {
				xtype: 'previewfilefield',
				margin: '10, 0, 10, 0',
				textName: 'c_img_4_squares_file',
				valueName: 'c_img_4_squares_url',
				btnText: '选择',
				fieldLabel: '四格图',
		        labelWidth: 120,
		        msgTarget: 'side',
		        anchor: '100%'
			}, {
				xtype: 'previewfilefield',
				margin: '10, 0, 10, 0',
				textName: 'c_img_6_squares_file',
				valueName: 'c_img_6_squares_url',
				btnText: '选择',
				fieldLabel: '六格图',
		        labelWidth: 120,
		        msgTarget: 'side',
		        anchor: '100%'
			}, {
				xtype: 'textareafield',
				margin: '10, 0, 10, 0',
				anchor: '100%',
				name: 'c_img_intro',
				fieldLabel: '图片描述',
				emptyText: '输入图片信息及其用途'
			}]
		}];
		
		this.callParent();
	}
});