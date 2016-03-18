Ext.define('app.view.oldview.computeSandTowerUrl', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.computeSandTowerUrl',
    initComponent: function() {
	//加上其它功能
	var widthSize = document.body.clientWidth-207;
	var heightSize = document.body.clientHeight-53;
	var intervalHeight = 18;

    var button_clear_memcache = new Ext.Component({
		height: heightSize,
        html: "<iframe width='100%' height='100%' frameborder='0' src='computeSandTowerUrl.jsp'></iframe>"
    });

    var modify_form = new Ext.FormPanel({
    	//id : 'modify_form',
    	name : 'modify_form',
		//width: 115,
    	items : [button_clear_memcache]
    });
	
	this.items = [modify_form];
	this.callParent();
    }
});