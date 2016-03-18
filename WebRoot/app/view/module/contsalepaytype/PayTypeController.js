Ext.define('app.view.module.contsalepaytype.PayTypeController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.cont-sale-paytype',

    refreshBaseStore: function(){
		var mainViewModel = this.getView().up('app-main').getViewModel();
		
		/*
    	console.log("--refreshBaseStore:paytypeArr");
    	if (AppUtil.ChoiceData["paytypeArr"]){
    		AppUtil.ChoiceData["paytypeArr"] = null;
    	}
		if(mainViewModel.get('contSalesStore_for_refresh') != null) {
			mainViewModel.get('contSalesStore_for_refresh').reload();
		}
		*/
    },

    onAddBtn: function(btn, e) {
    	var win = this.lookupReference('contsale_paytype_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.contsalepaytype.PayTypeWindow', {
            	viewModel: this.getView().getViewModel(),
            	oper:'add'
            });
            this.getView().add(win);
        }
    	win.oper = "add";
    	win.setTitle('添加');
    	win.down('form').reset();
    	
    	win.show();
    },
    
    onModify: function(grid, row, col, item, e, record) {
    	var win = this.lookupReference('contsale_paytype_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.contsalepaytype.PayTypeWindow', {
            	viewModel: this.getView().getViewModel(),
            	oper:'edit'
            });
            this.getView().add(win);
        }

    	win.oper = "edit";
    	win.setTitle('修改：' + record.raw.name);
    	win.down('form').reset();
    	win.down('form').loadRecord(record);
    	
    	win.show();
    },

    onRemove: function(grid, row, col, item, e, record) {
    	
    	Ext.Msg.show({
		    title:'删除',
		    message: '支付类型：' + record.raw.name,
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
    	var self = this;

    	Ext.Ajax.request({
    		url: 'paytypes/delete.do',
    		async: true,
    		params: {
    			id: record.raw.id
    		},
    		scope: this,
    		success: function(resp, opt) {
    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				grid.getStore().remove(record);
    			}
    			
    			p.unmask();
    			Ext.Msg.alert('提示', respJson.msg);
    			self.refreshBaseStore();
    		}
    	});
    },
    parseIconUrl: function(v) {
    	if (v == null || v == "")
    		return "";
    	else
    		return "<img src='"+v+"' height='20'>";
    },
    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('contsale_paytype_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	var self = this;

    	var url = 'paytypes/update.do';
    	if(win.oper === "add") {
    		url = 'paytypes/save.do';
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
	    			self.refreshBaseStore();
	    		}
	    	});
    	}
    },
    
    mainViewModel: null
});
