/**
 * 折叠式菜单，显示在主界面的左边
 */
Ext.define('app.view.main.menu.AccordionMainMenu', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.accordionmainmenu',
	
	title: '导航菜单',
	glyph: 0xf0c9,
	width: 220,
    collapsible: true,
	scrollable: 'y',

	layout: {
		type: 'accordion',
		animate: true
	},
    
    tools: [{
		type: 'unpin',
		tooltip: '树状方式显示菜单',
		handler: 'showTreeMainMenu'
	}, {
		itemId: 'up',
		type: 'up',
		tooltip: '在上面显示菜单条',
		handler: 'showToolbarMainMenu'
	}],

	initComponent: function() {
		this.items = [];
		this.refreshRoot();
		this.callParent(arguments);
	},
	
	refreshRoot: function() {
		var menus = this.up('app-main').getViewModel().get('systemMenu');
		
		for (var i in menus) {
			var menugroup = menus[i];
			if(typeof menugroup !== 'object') {
				continue;
			}
			var accpanel = {
				menuAccordion: true,
				xtype: 'panel',
				title: menugroup.text,
				bodyStyle: {
					padding : '10px'
				},
				layout: 'fit',
				dockedItems: [{
					dock: 'left',
					xtype: 'toolbar',
					items: []
				}],
				
				iconCls: menugroup.iconCls
			};
			
			var index = 0;
			for (var j in menugroup.items) {
				var menumodule = menugroup.items[j];
				if(typeof menumodule !== 'object') {
					continue;
				}
				accpanel.dockedItems[0].items.push({
					xtype: 'transparentbutton',
					textAlign: 'left',
					text: menumodule.text,
					module: menumodule.module,
					index: index,
					iconCls: menumodule.iconCls,
					handler: function(btn, event) {
						btn.up('app-main').getController().onMainMenuClick({
							module: btn.module,
							text: btn.text,
							iconCls: btn.iconCls,
							index: btn.index
						});
					}
				});
				
				index++;
			}
			
			this.items.push(accpanel);
		}
	}

});