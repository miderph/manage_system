Ext.define('app.view.main.Main', {
    extend: 'Ext.container.Viewport',
    requires: [
        'app.view.main.MainController',
        'app.view.main.MainModel'
    ],

    xtype: 'app-main',
    
    uses: [
       'app.view.main.region.Top',
       'app.view.main.region.Left',
       'app.view.main.region.Center',
       'app.view.main.menu.TreeMainMenu',
       'app.view.main.menu.AccordionMainMenu',
       'app.view.main.menu.ToolbarMainMenu'
	],
    
    controller: 'main',
    viewModel: {
        type: 'main'
    },

    layout: {
        type: 'border'
    },
    
    initComponent: function() {
    	Ext.setGlyphFontFamily('FontAwesome');
    	
    	this.callParent();
		Ext.MessageBox.wait('载入中，请稍后...');
    	Ext.Ajax.request({
    		timeout:2*60*1000,
    		url: 'module/query_with_auth.do',
    		async: true,
    		scope: this,
    		success: function(resp, opt) {
    			Ext.MessageBox.hide();
    			var respText = Ext.JSON.decode(resp.responseText);
//    			console.log(resp.responseText);
    			
    			//var sysInfo = {//在Main.js设置版本信息
		        //	name: 'OTT业务管理平台',
		        //	version: '1.0.0.0',
		        //	iconUrl: 'app/resources/icons/duolebo.png'
		        //};
    			
    			if(respText == null || respText == undefined) {
    				return;
    			}
    			
    			var menu = [{
					text: '菜单组',
		        	icon: '',
		        	iconCls: '',
		        	expanded: true,
		        	items: []
    			}];
    			
    			var moduleMenu = respText.moduleMenu;
    			Ext.Array.each(moduleMenu, function(name, index) {
    			    menu[0].items.push({
    			    	text: name.text,
    	            	icon: '',
    	            	iconCls: '',
    	            	module: name.id
    			    });
    			});
    			
    			this.getViewModel().set('username', respText.username);
    			//this.getViewModel().set('system', respText.sysInfo);
    			this.getViewModel().set('systemMenu', menu);
    			
    			/*this.down('buttonmainmenu').setMenu(this.getViewModel().getMenus());
    			this.down('toolbarmainmenu').add(this.getViewModel().getMenus());
    			
    			this.down('mainleft').removeAll();
    			this.down('mainleft').add({
    				xtype: 'treemainmenu'
    			});*/
    			this.down('buttonmainmenu').setMenu(this.getViewModel().getMenus());
    			this.down('toolbarmainmenu').add(this.getViewModel().getMenus());
    			
    			this.down('treemainmenu').refreshRoot();
    			
    			respText = null;
    		}
    	});
    	
    },
    
    listeners: {
		resize: function(container) {
			container.getController().onMainResize();
		}
	},

    items: [{
    	xtype: 'maintop',
    	region: 'north'
    }, {
    	xtype: 'toolbarmainmenu',
    	region: 'north',
    	hidden: true,
    	bind: {
			hidden: '{!isToolbarMenu}'
		}
    }, {
    	xtype: 'treemainmenu',
        region: 'west',
        //split: true,
        hidden: true,
        bind: {
        	hidden: '{!isTreeMenu}'
        }
    }/*, {
        xtype: 'mainleft',
        region: 'west',
        title: '导航菜单',
        width: 150,
        collapsible: true,
        //split: true,
        hidden: true,
        bind: {
        	hidden: '{!isTreeMenu}'
        }
    }*/, {
        region: 'center',
        xtype: 'maincenter'
    }]
});
