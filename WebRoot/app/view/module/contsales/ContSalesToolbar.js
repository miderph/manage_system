Ext.define('app.view.module.contsales.ContSalesToolbar', {
	extend: 'Ext.toolbar.Toolbar',
    
	alias: 'widget.cont-sales-toolbar',
	
	items: [{
		xtype : 'panel',
		baseCls : 'x-plain'
	},'按条件搜索购物资产：',{
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
		fieldLabel: '提供商',
		hideLabel : true,
		emptyText : '请选择提供商...',
		listeners: {
			select: 'onProviderSelect'
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
		fieldLabel: '状态',
		hideLabel : true,
		emptyText : '请选择状态',
		listeners: {
			select: 'onStatusSelect'
		}
	}, {
    	xtype: 'textfield',
    	name: 'search_name',
		fieldLabel: '资产名称',
		emptyText : '商品编号(模糊)、ID(精确)或名称(模糊)',
    	tooltip: '商品编号(模糊)、ID(精确)或名称(模糊)',
		hideLabel : true,
		labelWidth: 70,
		maxWidth: 190,
		flex: 1,
		labelAlign: 'right',
		listeners: {
			change: 'onTitleChange'
		}
	}, {
    	xtype: 'textfield',
    	name: 'search_price_from',
		fieldLabel: '价格从：',
		emptyText : '最小价格',
    	tooltip: '最小价格',
		hideLabel : true,
		labelWidth: 40,
		maxWidth: 60,
		flex: 1,
		labelAlign: 'right',
		listeners: {
			change: 'onPriceFromChange'
		}
	}, {
    	xtype: 'textfield',
    	name: 'search_price_to',
		fieldLabel: '价格到：',
		emptyText : '最大价格',
    	tooltip: '最大价格',
		hideLabel : true,
		labelWidth: 40,
		maxWidth: 60,
		flex: 1,
		labelAlign: 'right',
		listeners: {
			change: 'onPriceToChange'
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