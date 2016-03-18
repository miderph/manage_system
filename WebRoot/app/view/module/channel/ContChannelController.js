Ext.define('app.view.module.channel.ContChannelController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.cont-channel',

    onAddBtn: function(btn, e) {
    	var win = this.lookupReference('cont_channel_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.channel.ContChannelWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('添加');
    	win.down('form').reset();
    	
    	win.show();
    },
    
    onModify: function(grid, row, col, item, e, record) {
    	
    	if(!this.getViewModel().isAdmin()) {
    		return;
    	}
    	
    	var win = this.lookupReference('cont_channel_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.channel.ContChannelWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('修改：' + record.raw.name);
    	win.down('form').reset();
    	win.down('form').loadRecord(record);
    	
    	win.show();
    },

    onRemove: function(grid, row, col, item, e, record) {
    	
    	Ext.Msg.show({
		    title:'删除',
		    message: '商铺名称：' + record.raw.name,
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
    		url: 'contchannel/del.do',
    		async: true,
    		params: {
    			s_id: record.raw.s_id
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
    	var win = this.lookupReference('cont_channel_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	
    	var url = 'contchannel/update.do';
    	if(values.s_id === "-1") {
    		url = 'contchannel/save.do';
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
    },
    
    mainViewModel: null
});
