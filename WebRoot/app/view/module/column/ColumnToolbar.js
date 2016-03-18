Ext.define('app.view.module.column.ColumnToolbar', {
	extend: 'Ext.toolbar.Toolbar',
    
	alias: 'widget.column-toolbar',
	
	items: [{
		xtype : 'panel',
		baseCls : 'x-plain'
	},'按条件搜索栏目：',{
				xtype: 'combobox',editable: false,

		name: 'c_t_site',
		labelWidth: 70,
		maxWidth: 150,
		labelAlign: 'right',
		displayField: 's_name',
		valueField:'s_id',
		queryMode: 'local',
		flex: 1,
		bind: {
			store: '{siteAllStore}'
		},
		fieldLabel: '站点',
		hideLabel : true,
		emptyText : '请选择站点...',
		listeners: {
			select: 'onSiteSelect'
		}
	}, {
				xtype: 'combobox',editable: false,

		name: 'c_t_status',
		labelWidth: 70,
		maxWidth: 100,
		labelAlign: 'right',
		displayField: 's_name',
		valueField:'s_id',
		queryMode: 'local',
		flex: 1,
		bind: {
			store: '{columnStatusStore}'
		},
		fieldLabel: '状态',
		hideLabel : true,
		emptyText : '请选择状态...',		
		listeners: {
			select: 'onStatusSelect'
		}
	}, {
		xtype: 'button',
		text: '搜索栏目',
		glyph: 0xf002,
		width: 80,
		margin: '0, 0, 0, 50',
		disabled: false,
		bind: {
			disabled: '{!canSearch}'
		},
		handler: 'onSearchColumn'
	}]
});