Ext.define('app.view.module.provider.ContProviderToolbar', {
	extend: 'Ext.toolbar.Toolbar',
    
	alias: 'widget.cont-provider-toolbar',
	
	items: [{
		xtype : 'panel',
		baseCls : 'x-plain'
	},'按条件搜索供应商：', {
    	xtype: 'textfield',
    	name: 'search_name',
		fieldLabel: '资产名称',
		emptyText : 'ID(精确)或名称(模糊)',
    	tooltip: 'ID(精确)或名称(模糊)',
		hideLabel : true,
		labelWidth: 70,
		maxWidth: 190,
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
		handler: 'onSearchProvider'
	}, {
		xtype: 'button',
		text: '重置',
		width: 60,
		margin: '0, 0, 0, 20',
		handler: 'onSearchReset'
	}]
});