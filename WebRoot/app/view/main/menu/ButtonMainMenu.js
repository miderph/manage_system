/**
 * 显示在系统主页顶部的菜单按钮
 */
Ext.define('app.view.main.menu.ButtonMainMenu', {
	extend: 'app.ux.button.TransparentButton',
	alias: 'widget.buttonmainmenu',
	
	text: '菜单',
	iconCls: 'icon-reorder',
	
	initComponent: function() {
		//this.menu = this.up('app-main').getViewModel().getMenus();
		this.menu = [];
		this.callParent();
	}
	
});