/**
 * 系统主页的顶部区域，主要放置系统Logo，名称，菜单，帮助，退出等待
 */
Ext.define('app.view.main.region.Top', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.maintop',
	
	uses: [
       'app.ux.button.TransparentButton',
       'app.view.main.menu.ButtonMainMenu',
       'app.view.main.menu.SettingMenu'
	],
	
	height: 50,
	
	defaults: {
		xtype: 'transparentbutton'
	},
	
	items: [{
		xtype: 'image',
		bind: {
			hidden: '{!system.iconUrl}',
			src: '{system.iconUrl}'
		},
		style: 'height: 40px; width: 40px;'
	}, {
		xtype: 'label',
		bind: {
			text: '{system.name}'
		},
		style: 'font-size: 30px;'
	}, {
		xtype: 'label',
		style: 'color:grey;',
		bind: {
			text: '({system.version})'
		}
	}, '->', {
		xtype: 'buttonmainmenu',
		hidden: true,
		bind: {
			hidden: '{!isButtonMenu}'
		}
	}, ' ', ' ', {
		text: '首页',
		glyph: 0xf015,
		handler: 'goHomePage'
	}, {
		xtype: 'settingmenu'
	}, '->', //'BoYiLeTV	','客服编号：	','1019','	姓名：	','侯震', '->',
	{
		text : '注销',
		glyph: 0xf011,
		handler: 'onUnlogin'
	}, {
		glyph: 0xf102,
		tooltip: '隐藏顶部区域',
		handler: 'hiddenMainTop',
		disableMouseOver: true
	}]
});