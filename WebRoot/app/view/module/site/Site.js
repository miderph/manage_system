Ext.define('app.view.module.site.Site', {
	extend: 'Ext.grid.Panel',
	requires: [
       'app.view.module.site.SiteController'
    ],
    
	alias: 'widget.siteAdmin',
	
	title: '管理员用户',
	controller: 'site',
	
	frame: true,
    columnLines: true,
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('siteStatusStore') == null) {
			mainViewModel.set('siteStatusStore', Ext.create('app.store.SiteStatusStore'));
		}
		
		var store = Ext.create('app.store.SiteStore', {
			autoLoad: true
		});
		
		var pageSize = store.getPageSize();
		
		Ext.apply(this, {
            store: store,
            bbar: {
				xtype: 'pagingtoolbar',
		        pageSize: pageSize,
		        store: store,
		        displayInfo: true,
		        plugins: Ext.create('app.ux.ProgressBarPager')
            },
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            }
        });
		
        this.callParent();
	},
	
	columns: [{
		header: '名称',
		dataIndex: 'name',
		flex: 1,
		width: 200,
		locked: true
	}, {
		header: '状态',
		dataIndex: 'status',
		renderer: 'parseStatusV',
		align: 'center',
		width: 60
	}, {
		header: 'ID',
		dataIndex: 's_id',
		width: 80
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '起始时间',
		dataIndex: 'active_time',
		align: 'center',
		width: 150
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '结束时间',
		dataIndex: 'deactive_time',
		align: 'center',
		width: 150
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '创建时间',
		dataIndex: 'create_time',
		align: 'center',
		width: 150
	}, {
		xtype: 'datecolumn',
		format: 'Y-m-d H:i:s',
		header: '修改时间',
		dataIndex: 'modify_time',
		align: 'center',
		width: 150
	}, {
		header: '简介',
		dataIndex: 'intro',
		flex: 1
	}, {
        xtype: 'actioncolumn',
		header: '',
        locked: true,
        width: 70,
        sortable: false,
        menuDisabled: true,
        align: 'center',
        items: [{
        	iconCls: 'edit',
        	tooltip: '修改',
            handler: 'onModify'
        }, ' ', {
        	iconCls:'delete',
        	tooltip: '删除',
            handler: 'onRemove'
        }]
    }],
    
    listeners: {
//    	itemdblclick: function(grid, record, item, index, e, eOpts) {
//    		this.getController().onModify(this, -1, -1, null, e, record);
//    		
//    	}
    },
    
    tbar: [{
    	text: '添加',
    	iconCls: 'add',
    	handler: 'onAddBtn'
    }, ' ', '-', ' ', { 
        text: '索引',
        handler : function(){ 
				Ext.MessageBox.confirm('提示框', '您确定要进行该操作？',function(btn){ 
					if(btn=='yes'){
						Ext.getBody().mask("处理中，请稍等");
				   		Ext.Ajax.request({
				   					async : true,
				   					timeout: 1800000,
									url:'site/update_index.do',
									success:function(obj,action) {
				   						Ext.getBody().unmask();
										var jsonData = Ext.decode(obj.responseText)
										if(jsonData.success){
											Ext.MessageBox.show({
												title:"提示",
												msg:jsonData.msg,
												width:110,
												buttons:Ext.Msg.OK
											});
										}else{
											Ext.MessageBox.show({
												title:"提示",
												msg:jsonData.msg,
												width:110,
												buttons:Ext.Msg.OK
											});
										}
									},
									failure:function(){
										Ext.getBody().unmask();
										Ext.MessageBox.show({
											title:"提示",
											msg:jsonData.msg,
											width:110,
											buttons:Ext.Msg.OK
										});
									}
								});
						} 
				}); 
			}         
     
    }],
	
	iconCls: 'icon-grid'
});