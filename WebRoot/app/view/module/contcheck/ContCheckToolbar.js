Ext.define('app.view.module.contcheck.ContCheckToolbar', {
	extend: 'Ext.toolbar.Toolbar',
    
	alias: 'widget.cont-check-toolbar',
	
	items: [{
		xtype : 'panel',
		baseCls : 'x-plain'
	},'搜索：',{
		xtype: 'combobox',
		editable: false,
		name: 'c_s_status',
		labelWidth: 40,
		maxWidth: 100,
		labelAlign: 'right',
		displayField: 's_name',
		valueField:'s_id',
		queryMode: 'local',
		flex: 1,
		bind: {
			store: '{contCheckStatusStore}'
		},
		fieldLabel: '状态',
		hideLabel : true,
		emptyText : '请选择状态',
		listeners: {
			change: 'onStatusChange'
		}
	}, {
    	xtype: 'textfield',
    	name: 'search_name',
		fieldLabel: '资产名称',
		emptyText : '名称、编号或分类(模糊)、ID(精确)',
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
	},{
		xtype: 'datefield',
		name: 'search_start_time',
		allowBlank: false,
		fieldLabel: '开始时间：',
		emptyText : '开始时间',
		tooltip: '开始时间',
		hideLabel : true,
		labelWidth: 40,
		maxWidth: 150,
		flex: 1,
		labelAlign: 'right',
		value: Ext.Date.add(Ext.Date.clearTime(new Date()), Ext.Date.MONTH, -1),
		format: 'Y-m-d H:i:s',
		listeners: {
			change: 'onStartTimeChange'
		}
	}, {
		xtype: 'datefield',
		name: 'search_end_time',
		allowBlank: false,
		fieldLabel: '截止时间：',
		emptyText : '截止时间',
		tooltip: '截止时间',
		hideLabel : true,
		labelWidth: 40,
		maxWidth: 150,
		flex: 1,
		labelAlign: 'right',
		value: new Date(),
		format: 'Y-m-d H:i:s',
		listeners: {
			change: 'onEndTimeChange'
		}
	},{
		xtype: 'button',
		text: '搜索',
		glyph: 0xf002,
		width: 60,
		margin: '0, 0, 0, 20',/*
		disabled: false,
		bind: {
			disabled: '{!canSearch}'
		},*/
		handler: 'onSearch'
	}, {
		xtype: 'button',
		text: '重置',
		width: 60,
		margin: '0, 0, 0, 20',
		handler: 'onSearchReset'
	}]
});