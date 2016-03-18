Ext.define('app.view.main.MainModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.main',

    data: {
        name: 'OTT业务管理平台',
        
        operatorStatusStore: null,
        siteStatusStore: null,
        siteAllStore: null,
        contProviderByAuthStore: null,
        contShopStore: null,
        moduleAllStore: null,
        testGroupStatusStore: null,
        stbModelStore: null,
        softwareFileTypeStatusStore: null,
        testGroupAllStore: null,
        columnStatusStore: null,
        columnTypeStore: null,
        columnActTypeStore: null,
        columnResTypeStore: null,
        //contStatusStore: null,
        contStatusStore: Ext.create('app.store.ContStatusStore'),
        contSuperscriptStore: null,
        //contTypeStore: null,
        contTypeStore: Ext.create('app.store.ContTypeStore'),
        imgPlatStore: Ext.create('app.store.ColumnImgPlatStore'),

        roleStore_for_refresh: null,
        contSalesStore_for_refresh: null,

        system: {
        	name: 'OTT业务管理平台',
        	version: 'v2.8',
        	iconUrl: 'app/resources/icons/duolebo.png'
        },
        
        systemMenu: [/*{
        	text: '菜单组',
        	icon: '',
        	iconCls: '',
        	expanded: true,
        	items: [{
        		text: '管理员用户',
            	icon: '',
            	iconCls: 'icon-user-md',
            	module: 'adminuser'
        	}]
        }*/],
        
        menuType: {
			value: 'tree' /* 'button' , 'toolbar' , 'tree' */
		},
		
		appTheme: {
			value: 'classic'
		},
		
		username: ''
    },
    
    formulas: {
		isButtonMenu: function(get) {
			return get('menuType.value') == 'button';
		},
		
		isToolbarMenu: function(get) {
			return get('menuType.value') == 'toolbar';
		},
		
		isTreeMenu: function(get) {
			return get('menuType.value') == 'tree';
		},
		
		isAdmin: function(get) {
			return get('username') == 'admin';
		}
	},
	
	isAdmin: function() {
		return this.get('username') == 'admin';
	},
    
	/* 根据data.systemMenu生成菜单数据 */
	getMenus: function() {
		
		var items = [];
		var menuData = this.get('systemMenu');
		
		/* 遍历menuData数组 */
		Ext.Array.each(menuData, function(group) {
			var submenu = [];
			
			var index = 0;
			/* 遍历菜单项的数组 */
			Ext.Array.each(group.items, function(menuitem) {
				submenu.push({
					text : menuitem.text,
					icon : menuitem.icon,
					iconCls : menuitem.iconCls,
					mainmenu : 'true',
					module : menuitem.module,
					index: index,
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
			});
			
			var item = {
				text: group.text,
				menu: submenu,
				icon: group.icon,
				glyph: group.iconCls
			};
			
			items.push(item);
		});
		
		return items;
	},
	
	kvData: {
    	
    },
    
    parseValue: function(key, store, k, v) {
    	if(!this.kvData[store]) {
    		this.kvData[store] = {};
    	}
    	
    	var kv = this.kvData[store];
    	
    	if(!kv['k' + key]) {
    		var items = this.get(store).getData().items;
    		
    		Ext.Array.each(items, function(item, index) {
    			kv['k' + item.raw[k]] = item.raw[v];
			});
    		
    		this.kvData[store] = kv;
    	}
    	
    	return kv['k' + key];
    }
	
});