//Backstage.Manager.siteAdminPanel = function(config){
//	Backstage.Manager.siteAdminPanel.superclass.constructor.call(this, config);
Ext.define('app.view.oldview.clearMemCachePanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.clearMemCache',
    initComponent: function() {
	//加上其它功能
	var widthSize = document.body.clientWidth-207;
	var heightSize = document.body.clientHeight-53;
	var intervalHeight = 18;


	var button_clear_memcache = new Ext.Button({
		xtype: 'button',
		text: '清除服务端协议缓存',
		handler : function(){
    	 modify_form.getForm().submit({
	        url : 'clearMemCache.do',
	        method : 'post',
	        success : function(f, a) {
               Ext.Msg.alert('Success', '清除成功');
		        },
		        failure : function(f, a) {
               Ext.Msg.alert('Warning', '清除失败');
	        	}
	        });
		}
	});
    var modify_form = new Ext.FormPanel({
    	//id : 'modify_form',
    	name : 'modify_form',
		width: 115,
    	items : [button_clear_memcache]
    });
	/*var siteAdminPanel = new Ext.Panel({
		plain:true,
		frame:true,
		width:widthSize,
		height:heightSize,
		autoScroll:true,
		renderTo:'clearMemCache-basic',
		items: modify_form
	}).show();*/
	
	this.items = [modify_form];
	this.callParent();
    }
});