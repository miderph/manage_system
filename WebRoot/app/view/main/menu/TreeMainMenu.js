/**
 * 树状菜单，显示在主界面的左边
 */
Ext.define('app.view.main.menu.TreeMainMenu', {
	extend: 'Ext.tree.Panel',
	alias: 'widget.treemainmenu',
	
	rootVisible: false,
	lines: false,
	title: '导航菜单',
	glyph: 0xf0c9,
	width: 150,
    collapsible: true,
    
    tools: [/*{
		type: 'pin',
		tooltip: '层叠方式显示菜单',
		handler: 'showAccordionMainMenu'
	}, */{
		itemId: 'up',
		type: 'up',
		tooltip: '在上面显示菜单条',
		handler: 'showToolbarMainMenu'
	}],
	
	listeners: {
		itemclick: function(panel, record, item, index, e, eOpts) {
			panel.up('app-main').getController().onMainMenuClick({
				module: record.data.module,
				text: record.data.text,
				iconCls: record.data.iconCls,
				index: record.data.index
			});
		}
	},
	
	initComponent: function() {
		this.store = Ext.create('Ext.data.TreeStore', {
			root: {
				expanded: true,
				text: '系统菜单',
				leaf: false,
				children: []
			}
	    });
		
		this.refreshRoot();
		
		this.callParent(arguments);
	},
	
	refreshRoot: function() {
		var menus = this.up('app-main').getViewModel().get('systemMenu');
		var root = this.store.getRoot();
		root.removeAll();
		
		for (var i in menus) {
			var menugroup = menus[i];
			if(typeof menugroup !== 'object') {
				continue;
			}
			
			var menuitem = root.appendChild({
				text: menugroup.text,
				expanded: menugroup.expanded,
				icon: menugroup.icon,
				iconCls: menugroup.iconCls
			});
			
			var index = 0;
			for (var j in menugroup.items) {
				var menumodule = menugroup.items[j];
				if(typeof menumodule !== 'object') {
					continue;
				}
				var childnode = {
					moduleId: menumodule.text,
					module: menumodule.module,
					text: menumodule.text,
					iconCls: menumodule.iconCls,
					index: index,
					leaf: true
				};
				
				menuitem.appendChild(childnode);
				
				index++;
			}
		}
	}
});