Ext.define('app.view.main.MainController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.MessageBox',
        'Ext.window.Toast'
    ],
    
	uses: [
		'app.view.module.operator.Operator',
	    'app.view.module.role.Role',
	    'app.view.module.provider.ContProvider',
	    'app.view.module.software.SoftwareAsset',
	    'app.view.module.site.Site',
	    'app.view.module.stbprefix.StbPrefix',
	    'app.view.module.hotword.HotWord',
	    'app.view.module.testgroup.TestGroup',
	    "app.view.module.shop.ContShop",
	    "app.view.module.channel.ContChannel",
	    'app.view.module.column.Column',
	    'app.view.module.contsales.ContSales',
	    'app.view.module.contsalepaytype.PayType',
	    'app.view.module.contvideo.ContVideo',
	    'app.view.module.contapp.ContApp',
	    'app.view.module.customer.Customer',
	    'app.view.module.contcheck.ContCheck',
//  		'app.view.module.menurelacont.Main',
	    'app.view.oldview.clearMemCachePanel',
//	    'app.view.oldview.contSalesPayTypePanel',
	    'app.view.oldview.reports',
	    'app.view.oldview.menuAssetsRelaPanel',
//	    'app.view.oldview.customerPanel',
//	    'app.view.oldview.siteAdminPanel',
//	    'app.view.oldview.stbPrefix',
//	    'app.view.oldview.hotWordPanel',
//	    'app.view.oldview.userGroupPanel',
//	    'app.view.oldview.contVideoPanel',
//	    'app.view.oldview.contSalesPanel',
//	    'app.view.oldview.contVideoFilePanel',
//	    'app.view.oldview.contAppstorePanel',
	    "app.view.oldview.computeSandTowerUrl"
	],

    alias: 'controller.main',

    goHomePage: function() {
    	var maincenter = this.getView().down('maincenter');
    	var homePage = this.getView().down('homepage');
    	maincenter.setActiveTab(homePage);
    },
    
	/* 响应主菜单单击事件 */
	onMainMenuClick: function(menuItem) {
		/*console.log(menuItem);
		Ext.toast({
			html: 'Data Saved , hello  this is a meessage',
			title: menuItem.text,
			saveDelay: 10,
			align: 'tr',
			closable: true,
			width: 200,
			useXAxis: true,
			slideInDuration: 500
		});*/
		
		var isNewTab = true;
		
		if(isNewTab) {
			var maincenter = this.getView().down('maincenter');
			var tab = maincenter.down(menuItem.module);
			
			if(tab) {
				maincenter.setActiveTab(tab);
			} else {
				maincenter.setActiveTab(maincenter.add({
					xtype : menuItem.module,
					title: menuItem.text,
					iconCls: menuItem.iconCls,
					closable : true,
					reorderable : true
				}));
			}
		} else {
			this.addOldTab({
				id: menuItem.module,
				text: menuItem.text
			});
		}
		
		
	},
	
	addOldTab: function(node) {
		var maincenter = this.getView().down('maincenter');
		var n = maincenter.getComponent(node.id);
		var c = {
			'id':node.id,
			'title':node.text,
			closable:true
		};
		
		var testjsp = 'resources/jsp/'+node.id+'.jsp';
		
		if (n == undefined)
		{
	    	n = maincenter.add( Ext.create('Ext.panel.Panel',{
			  id:node.id,
			  title:node.text,
			
			  autoLoad:{url:testjsp, scripts:true,nocache:true},
			  //autoScroll:true,
			  iconCls:'tabIconCss',
			  layout: 'fit',
			  border:false,
			  closable:true,
			  autoDestroy: true
	    	}));
		}
		
		if(n){
			maincenter.setActiveTab(n);				
		} else {
			var pn = maincenter.findPanel(node.id);
			n=maincenter.add(pn ?new pn(c):Ext.apply(c,{html:'该页面尚未实现'}));
			n.show().doLayout();
		}
		
		if(n.ds){
			n.ds.load();
		}
	},
    
    /* 隐藏顶部的按钮事件 */
    hiddenMainTop: function() {
    	this.getView().down('maintop').hide();
		if (!this.showButton) {
			this.showButton = Ext.widget('component', {
				glyph: 0xf103,
				view: this.getView(),
				floating: true,
				x: document.body.clientWidth - 36,
				y: 0,
				height: 6,
				width: 28,
				style: 'background-color:#cde6c7',
				listeners: {
					el: {
						click: function(el) {
							/* 取得component的id值 */
							var c = Ext.getCmp(el.target.id);
							c.view.down('maintop').show();
							c.hide();
						}
					}
				}
			})
		};
		
		this.showButton.show();
	},
	
	/* 如果窗口的大小改变了，并且顶部和底部都隐藏了，就要调整显示顶和底的那个控件的位置 */
	onMainResize: function() {
		if (this.showButton && !this.showButton.hidden) {
			this.showButton.setX(document.body.clientWidth - 36);
		}
	},
	
	/* 显示Toolbar菜单条，隐藏左边菜单区域和顶部的按钮菜单。*/
	showToolbarMainMenu: function(button) {
		var viewModel = this.getView().getViewModel();
		if(viewModel.get('menuType.value') !== 'toolbar') {
			viewModel.set('menuType.value', 'toolbar');
		}
		viewModel = null;
	},
	
	/* 显示左边菜单区域，隐藏菜单条和顶部的按钮菜单。*/
	showLeftMainMenu: function(button) {
		var viewModel = this.getView().getViewModel();
		if(viewModel.get('menuType.value') !== 'tree') {
			viewModel.set('menuType.value', 'tree');
		}
		viewModel = null;
	},
	
	/* 显示顶部的按钮菜单，隐藏菜单条和左边菜单区域。*/
	showButtonMainMenu: function(button) {
		var viewModel = this.getView().getViewModel();
		if(viewModel.get('menuType.value') !== 'button') {
			viewModel.set('menuType.value', 'button');
		}
		viewModel = null;
	},
	
	showTheme: function(menu) {
		var viewModel = this.getView().getViewModel();
		var theme = menu.value;
		if(theme !== viewModel.get('appTheme.value')) {
			console.log(theme);
			
			var href = 'ext/packages/ext-theme-'+theme+'/build/resources/ext-theme-'+theme+'-all.css';
			var link = Ext.fly('theme');
			
			if(!link) {
				link = Ext.getHead().appendChild({
					tag:'link',
					id:'theme',
					rel:'stylesheet',
					href:''
				});
			};
			
			link.set({href:Ext.String.format(href, theme)});
			
			viewModel.set('appTheme.value', theme);
		}
		
		viewModel = null;
		theme = null;
	},
	
	onUnlogin: function() {
		Ext.Ajax.request({
    		url: 'operator/unlogin.do',
    		async: true,
    		scope: this,
    		success: 'onUnloginSuccess',
			failure: 'onUnloginFailure'
    	});
	},
	
	onUnloginSuccess: function(resp, opt) {
    	var respJson = Ext.JSON.decode(resp.responseText);
    	
    	if(respJson.issuc === true) {
    		this.getView().destroy();
			location.href = respJson.url;
		} else {
			Ext.Msg.alert('提示', respJson.msg);
		}
    },
    
    onUnloginFailure: function() {
    	Ext.getBody().unmask();
    }
});
