Ext.define('app.view.module.shop.ContShopController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.cont-shop',

    parseIconUrl: function(v) {
    	if (v == null || v == "")
    		return "";
    	else
    		return "<img src='"+v+"' height='20'>";
    },

    onAddBtn: function(btn, e) {
    	var win = this.lookupReference('cont_shop_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.shop.ContShopWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('添加');
    	win.down('form').reset();
    	
    	win.show();
    },
    
    onModify: function(grid, row, col, item, e, record) {
    	var win = this.lookupReference('cont_shop_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.shop.ContShopWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('修改：' + record.raw.name);
    	
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
    		url: 'contshop/del.do',
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
    	var win = this.lookupReference('cont_shop_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	
    	var url = 'contshop/update.do';
    	if(values.s_id === "-1") {
    		url = 'contshop/save.do';
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
    onHotInfoByChannel: function(btn, e) {
    	var view = this.getView();
		var rows = view.getSelectionModel().getCount();// 返回值为Record数组
        var cp_id = -1;
        var cp_name = '';
		if(rows == 1) {
			var record = view.getSelectionModel().lastSelected;
	        cp_id = record.raw.s_id;
	        cp_name = record.raw.name;
		}
		//console.log("------shop s_id="+cp_id);
		
		AppUtil.crudHotInfo(this, cp_id, cp_name, 'shop');
		
    },
    mainViewModel: null
});
