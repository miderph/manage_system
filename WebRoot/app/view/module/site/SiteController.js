Ext.define('app.view.module.site.SiteController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.site',
    
    refreshBaseStore: function(){
		var mainViewModel = this.getView().up('app-main').getViewModel();
		if(mainViewModel.get('siteAllStore') != null) {
			mainViewModel.get('siteAllStore').reload();
		}
    	console.log("--refreshBaseStore:siteArr");
    	if (AppUtil.ChoiceData["siteArr"]){
    		AppUtil.ChoiceData["siteArr"] = null;
    	}
		if(mainViewModel.get('roleStore_for_refresh') != null) {
			mainViewModel.get('roleStore_for_refresh').reload();
		}
    },
    onAddBtn: function(btn, e) {
    	var win = this.lookupReference('site_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.site.SiteWindow', {
            	viewModel: this.getView().getViewModel()
            });
            this.getView().add(win);
        }
    	
    	win.setTitle('添加');
    	win.down('form').reset();
    	
    	win.show();
    },
    
    onModify: function(grid, row, col, item, e, record) {
    	var win = this.lookupReference('site_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.site.SiteWindow', {
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
		    message: '站点名称：' + record.raw.name,
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
    		url: 'site/del.do',
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
    			self.refreshBaseStore();
    		}
    	});
    },
    
    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('site_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	var self = this;

    	var url = 'site/update.do';
    	if(values.s_id === "-1") {
    		url = 'site/save.do';
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
    
    mainViewModel: null,
    
    parseStatusV: function(v) {
    	
    	var str = '';
    	var store = 'siteStatusStore';
    	
    	var vm = this.mainViewModel;
    	
    	if(vm != null && vm.get(store) != null) {
    		str = vm.parseValue(v, store, 's_id', 's_name');
    	}
    	
    	return str;
    }
});
