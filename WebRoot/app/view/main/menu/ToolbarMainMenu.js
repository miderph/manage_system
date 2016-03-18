/**
 * Toolbar菜单条， 显示在主界面的顶部
 */
Ext.define('app.view.main.menu.ToolbarMainMenu', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.toolbarmainmenu',
	
	defaults: {
		xtype: 'transparentbutton'
	},
	
	items: [{
		glyph: 0xf100,
		tooltip: '在左边栏中显示树状菜单',
		disableMouseOver: true,
		margin: '0 -5 0 0',
		handler: 'showLeftMainMenu'
	}, {
		glyph: 0xf102,
		tooltip: '在顶部区域显示菜单',
		disableMouseOver: true,
		handler: 'showButtonMainMenu'
	}],
	
	initComponent : function() {
//		this.items = this.items.concat(this.up('app-main').getViewModel().getMenus());
		this.callParent();
	}
});