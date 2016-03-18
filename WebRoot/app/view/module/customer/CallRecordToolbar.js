Ext.define('app.view.module.customer.CallRecordToolbar', {
	extend: 'Ext.toolbar.Toolbar',
    
	alias: 'widget.call-record-toolbar',
	
	items: ['<div style="font-weight: bolder; font-size: 18px; color:#00F;">通话记录</div>', '->', '根据客户号码搜索：', {
    	xtype: 'textfield',
    	name: 'search_name',
		hideLabel : true,
		fieldLabel: '客户号码',
		emptyText : '客户号码',
    	tooltip: '客户号码',
		labelWidth: 70,
		maxWidth: 120,
		flex: 1,
		labelAlign: 'right',
		listeners: {
			change: 'onCustomerNumChange',
			specialkey: 'onCustomerNumEnter'
		}
	}, {
		xtype: 'button',
		text: '搜索',
		glyph: 0xf002,
		width: 60,
		margin: '0, 20, 0, 10',
		handler: 'onSearchCallRecordByNum'
	}, '|', {
		xtype: 'button',
		text: '搜索正在拨入的客户',
		glyph: 0xf002,
		width: 150,
		margin: '0, 0, 0, 20',
		handler: 'onSearchCallRecording'
	}, '->']
});