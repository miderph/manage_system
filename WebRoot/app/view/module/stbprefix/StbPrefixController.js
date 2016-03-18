Ext.define('app.view.module.stbprefix.StbPrefixController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.stbprefix',
    
    onAddBtn: function(btn, e) {
    	var win = this.lookupReference('stbprefix_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.stbprefix.StbPrefixWindow');
            this.getView().add(win);
        }
    	
    	win.setTitle('添加');
    	win.down('form').reset();
    	
    	win.show();
    },
    
    onModify: function(grid, row, col, item, e, record) {
    	var win = this.lookupReference('stbprefix_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.stbprefix.StbPrefixWindow');
            this.getView().add(win);
        }
    	
    	win.setTitle('修改：' + record.raw.code);
    	
    	win.down('form').loadRecord(record);
    	
    	win.show();
    },

    onRemove: function(grid, row, col, item, e, record) {
    	
    	Ext.Msg.show({
		    title:'删除',
		    message: '前缀名称：' + record.raw.code,
		    buttons: Ext.Msg.YESNO,
		    icon: Ext.Msg.QUESTION,
		    scope: this,
		    fn: function(btn) {
		        if (btn === 'yes') {
		        	this.del(grid, record);
		        }
		    }
		});
    	
    },
    
    del: function(grid, record) {
    	var p = this.getView();
    	p.mask('删除中...');
    	
    	Ext.Ajax.request({
    		url: 'stbprefixe/del.do',
    		async: true,
    		params: {
    			sp_id: record.raw.sp_id
    		},
    		scope: this,
    		success: function(resp, opt) {
    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				grid.getStore().remove(record);
    			}
    			
    			p.unmask();
    			Ext.Msg.alert('提示', respJson.msg);
    		}
    	});
    },
    
    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('stbprefix_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	
    	var url = 'stbprefixe/update.do';
    	if(values.sp_id === "-1") {
    		url = 'stbprefixe/save.do';
    	}
    	
    	if (form.isValid()){
    		win.mask('正在保存...');
    		
	    	form.submit({
	    		clientValidation: true,
	    	    url: url,
	    		params: values,
	    		submitEmptyText: false,
	    		success: function(form, action) {
	    			if(action.result.issuc) {
	    				form.reset();
	    				win.hide();
	//    				win.destroy();
	    				
	    				grid.getStore().reload();
	    			}
	    			win.unmask();
	    			
	    			Ext.Msg.alert('提示', action.result.msg);
	    		}
	    	});
    	}
    }
});
