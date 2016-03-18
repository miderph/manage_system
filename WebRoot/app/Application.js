Ext.define('app.Application', {
    extend: 'Ext.app.Application',
    
    name: 'app',
    
    uses:[],
    
    controllers: [],

    stores: [
        // TODO: add global / shared stores here
    ],
    
    launch: function () {
    	Ext.Ajax.timeout = 300000;
    	
    	Ext.getDoc().on('contextmenu', function(e, t, eOpts) {
    		e.stopEvent();
    	});
    	
    	Ext.apply(Ext.form.field.VTypes, {
    		intfloat: function (v) {
    	        return /^[-]?([0-9]*\.?[0-9]+|[0-9]+\.?[0-9]*)([eE][+-]?[0-9]+)?$/.test(v);
    	    },
    	    intfloatText: '只能输入整数或浮点数',
    	    intfloatMask: /[\d.]/i
    	});
    	
    	Ext.apply(Ext.form.field.VTypes, {
    		phonenum: function (v) {
    	        return /^1\d{10}$/.test(v);
    	    },
    	    phonenumText: '只能输入手机号码',
    	    phonenumMask: /[\d]/i
    	});
    	
    	Ext.apply(Ext.form.field.VTypes, {
    		telnum: function (v) {
    	        return /^\d{2,4}\-\d{6,9}$/.test(v);
    	    },
    	    telnumText: '只能输入电话号码',
    	    telnumMask: /[\d\-]/i
    	});
    	
    	Ext.apply(Ext.form.field.VTypes, {
    		naturalnum: function (v) {
    	        return /^[1-9]\d*|0$/.test(v);
    	    },
    	    naturalnumText: '只能正整数',
    	    naturalnumMask: /[\d]/i
    	});
    	
    	Ext.Ajax.on('requestcomplete',function(conn,response,options) {
    	   var href = window.location.href;
    	   href = href.substring(0,href.lastIndexOf("/")+1);
		   if(response && response.getResponseHeader && response.getResponseHeader('_timeout')){  
		       Ext.Msg.alert('提示', '会话超时，请重新登录!', function(){  
		           window.location = href;   
		       });  
		   }
		});
    }
});