Ext.define('app.view.login.LoginController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.login',
    
    onLoginBtnClick: function() {
    	var form = this.lookupReference('form');
    	if (form.isValid()){
    		this.login({
    			data: form.getValues()
    		});
    	}
    },
    
    onLoginFailure: function() {
    	Ext.getBody().unmask();
    	Ext.MessageBox.hide();
    },
    
    onLoginSuccess: function(resp, opt) {
    	Ext.MessageBox.hide();
    	var respJson = Ext.JSON.decode(resp.responseText);
    	
    	if(respJson.issuc === true) {
    		this.getView().destroy();
			location.href = respJson.url;
		} else {
			Ext.Msg.alert('提示', respJson.msg);
		}
    },
    
    login: function(options) {
		Ext.MessageBox.wait('处理中，请稍后...');
    	Ext.Ajax.request({
    		url: 'operator/login.do',
    		async: true,
    		params: options.data,
    		scope: this,
    		success: 'onLoginSuccess',
			failure: 'onLoginFailure'
    	});
    }
});
