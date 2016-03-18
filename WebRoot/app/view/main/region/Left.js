/**
 * 系统主页的左边区域，可以放树形菜单或折叠菜单
 */
Ext.define('app.view.main.region.Left', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.mainleft',
	
	uses: [
       'app.view.main.menu.TreeMainMenu',
       'app.view.main.menu.AccordionMainMenu'
	],
	
	layout: {
		type : 'accordion',
		animate : true
	},
	
	glyph: 0xf0c9,
	tools: [{
		type: 'pin',
		tooltip: '层叠方式显示菜单',
		callback: function(panel, tool, event) {
			panel.insert(0, {
				xtype: 'accordionmainmenu'
			});
			
			panel.items.items[0].expand();
			panel.remove(panel.down('treemainmenu'), true);
			
			tool.hide();
			tool.nextSibling().show();
		}
	}, {
		type: 'unpin',
		tooltip: '树状方式显示菜单',
		hidden: true,
		callback: function(panel, tool, event) {
			panel.insert(0, {
				xtype: 'treemainmenu'
			});
			
			panel.items.items[0].expand();
			Ext.each(panel.query('accordionmainmenu'), function(accordion) {
				panel.remove(accordion, true)
			});
			
			tool.hide();
			tool.previousSibling().show();
		}
	}, {
		itemId: 'up',
		type: 'up',
		tooltip: '在上面显示菜单条',
		handler: 'showToolbarMainMenu'
	}],
	
	initComponent: function() {
		this.items = [];
		
		this.callParent();
	}
});