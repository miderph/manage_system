Ext.define('app.view.module.contvideo.ContVideoToolbar', {
	extend: 'Ext.toolbar.Toolbar',
    
	alias: 'widget.cont-video-toolbar',
	
	items: [{
		xtype : 'panel',
		baseCls : 'x-plain'
	},'按条件搜索资产：',{
				xtype: 'combobox',editable: false,

		name: 'c_s_provider',
		labelWidth: 40,
		maxWidth: 150,
		labelAlign: 'right',
		displayField: 'cp_name',
		valueField:'cp_id',
		queryMode: 'local',
		flex: 1,
		bind: {
			store: '{contProviderByAuthStore}'
		},
		hideLabel : true,
		fieldLabel: '提供商',
		emptyText : '请选择提供商...',
		listeners: {
			select: 'onProviderSelect'
		}
	}, {
				xtype: 'combobox',editable: false,

		name: 'c_s_type',
		labelWidth: 40,
		maxWidth: 100,
		labelAlign: 'right',
		displayField: 's_name',
		valueField:'s_id',
		queryMode: 'local',
		flex: 1,
		bind: {
			store: '{contTypeStore}'
		},
		hideLabel : true,
		fieldLabel: '类型',
		emptyText : '请选择类型',
		listeners: {
			select: 'onTypeSelect'
		}
	}, {
				xtype: 'combobox',editable: false,

		name: 'c_s_status',
		labelWidth: 40,
		maxWidth: 100,
		labelAlign: 'right',
		displayField: 's_name',
		valueField:'s_id',
		queryMode: 'local',
		flex: 1,
		bind: {
			store: '{contStatusStore}'
		},
		hideLabel : true,
		fieldLabel: '状态',
		emptyText : '请选择状态',
		listeners: {
			select: 'onStatusSelect'
		}
	}, {
    	xtype: 'textfield',
    	name: 'search_name',
		hideLabel : true,
		fieldLabel: '资产名称',
		emptyText : 'ID(精确)或资产名称(模糊)',
    	tooltip: 'ID(精确)或资产名称(模糊)',
		labelWidth: 70,
		maxWidth: 150,
		flex: 1,
		labelAlign: 'right',
		listeners: {
			change: 'onTitleChange'
		}
	}, {
		xtype: 'button',
		text: '搜索',
		glyph: 0xf002,
		width: 60,
		margin: '0, 0, 0, 20',/*
		disabled: false,
		bind: {
			disabled: '{!canSearch}'
		},*/
		handler: 'onSearchContSales'
	}, {
		xtype: 'button',
		text: '重置',
		width: 60,
		margin: '0, 0, 0, 20',
		handler: 'onSearchReset'
	}]
});