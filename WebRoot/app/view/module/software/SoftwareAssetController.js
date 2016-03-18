Ext.define('app.view.module.software.SoftwareAssetController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'Ext.window.Toast'
    ],

    alias: 'controller.software-asset',
    
    onAddClick: function(btn, e) {
    	var win = this.lookupReference('software_asset_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.software.SoftwareAssetWindow');
            this.getView().add(win);
        }
    	
    	win.setTitle('新增');
    	win.down('form').reset();
    	
    	win.show();
    },
    
    onAddWithCopy: function(record) {
    	var win = this.lookupReference('software_asset_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.software.SoftwareAssetWindow');
            this.getView().add(win);
        }
    	
    	win.setTitle('复制新增');
    	
    	record.raw.id = '-1';
    	
    	win.down('form').loadRecord(record);
    	
    	win.show();
    },
    
    onAddFromExcel: function() {
    	var win = this.lookupReference('update_excle_window');
    	
    	if(win) {
    		win.destroy();
    	}
    	
    	win = Ext.create('app.ux.window.UpdateExcelWindow', {
        	title: '批量添加软件升级版本',
        	reqUrl: 'software/save_from_excel.do',
        	reloadGrid: this.getView().getStore(),
        	excelTemplate: 'template/software_batch_insert_template.xlsx'
        });
        this.getView().add(win);
    	
    	win.down('form').reset();
    	
    	win.show();
    },
    
    onModifyClick: function(grid, row, col, item, e, record) {
    	var win = this.lookupReference('software_asset_window');
    	
    	if (!win) {
            win = Ext.create('app.view.module.software.SoftwareAssetWindow');
            this.getView().add(win);
        }
    	
    	win.setTitle('修改');
    	var winForm = win.down('form');
    	winForm.reset();

    	winForm.loadRecord(record);
    	
    	win.show();
    },

    onRemoveClick: function(grid, row, col, item, e, record) {
    	
    	Ext.Msg.show({
		    title:'删除',
		    message: '&nbsp;&nbsp;ID：' + record.raw.id + '<br> &nbsp;&nbsp;版本号：' + record.raw.version_number,
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
    	Ext.Ajax.request({
    		url: 'software/del.do',
    		async: true,
    		params: {
    			id_del: record.raw.id,
    			version_number_del: record.raw.version_number,
    			software_info_del: record.raw.software_info,
    			plat_del: record.raw.add_plat
    		},
			
    		scope: this,
    		success: function(resp, opt) {
    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				grid.getStore().remove(record);
    			}
    			
    			Ext.toast({
     				html: respJson.msg,
     				title: '提示',
     				saveDelay: 10,
     				align: 'tr',
     				closable: true,
     				width: 200,
     				useXAxis: true,
     				slideInDuration: 500
     			});
    		}
    	});
    },
    
    onChoiceMac: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('software_asset_window'),
			//viewModel: this.mainViewModel,
			//storeRef: 'testGroupByMacStore',
			//store: 'app.store.TestGroupByMacStore',
			win_title: '选择网卡地址测试组',
			url: 'usergroup/query_all.do?type=mac',
			d_key: 'ugByMacArr',
			d_id: 'ug_id',
			d_name: 'ug_name'
		});
    	
    },
    
    onChoiceZone: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('software_asset_window'),
			//viewModel: this.mainViewModel,
			//storeRef: 'testGroupByZoneStore',
			//store: 'app.store.TestGroupByZoneStore',
			win_title: '选择地区测试组',
			url: 'usergroup/query_all.do?type=zone',
			d_key: 'ugByZoneArr',
			d_id: 'ug_id',
			d_name: 'ug_name'
		});
    },
    
    onChoiceModel: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('software_asset_window'),
			//viewModel: this.mainViewModel,
			//storeRef: 'testGroupByModelStore',
			//store: 'app.store.TestGroupByModelStore',
			win_title: '选择型号测试组',
			url: 'usergroup/query_all.do?type=model',
			d_key: 'ugByModelArr',
			d_id: 'ug_id',
			d_name: 'ug_name'
		});
    },
    
    onChoiceChannel: function(btn, e) {
    	
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('software_asset_window'),
			//viewModel: this.mainViewModel,
			//storeRef: 'testGroupByChannelStore',
			//store: 'app.store.TestGroupByChannelStore',
			win_title: '选择渠道测试组',
			url: 'usergroup/query_all.do?type=channel',
			d_key: 'ugByChannelArr',
			d_id: 'ug_id',
			d_name: 'ug_name'
		});
    },
    onSubmit: function() {
    	var grid = this.getView();
    	var win = this.lookupReference('software_asset_window');
    	var form = win.down('form');
    	var values = form.getValues();
    	
    	var url = 'software/update.do';
    	
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
	    			
	    			Ext.toast({
	     				html: action.result.msg,
	     				title: '提示',
	     				saveDelay: 10,
	     				align: 'tr',
	     				closable: true,
	     				width: 200,
	     				useXAxis: true,
	     				slideInDuration: 500
	     			});
	    		}
	    	});
    	}
    },
    
    mainViewModel: null
});
