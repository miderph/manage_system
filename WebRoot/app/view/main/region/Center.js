/**
 * 系统界面的主区域,是一个tabpanel,可以有多个tab页面，用来放置各个模块。
 */
Ext.define('app.view.main.region.Center', {
	extend: 'Ext.tab.Panel',
	alias: 'widget.maincenter',

	uses: ['app.view.main.region.HomePage'],

	closeAction: 'hide',
	autoDestroy: false,
	tabPosition: 'top',

	initComponent: function() {

		this.items = [{
			xtype : 'homepage',
			border : true,
			frame : false,
			reorderable : false
		}];
		
		this.callParent();
	}

})