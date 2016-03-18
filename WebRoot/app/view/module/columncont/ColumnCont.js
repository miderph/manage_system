Ext.define('app.view.module.columncont.ColumnCont', {
	extend: 'Ext.container.Container',
	requires: [
       'app.view.module.columncont.ColumnContController',
       'app.view.module.columncont.ColumnContModel'
    ],
    
//    uses: ['app.view.module.column.ColumnToolbar'],
    
	alias: 'widget.menuAssetsRelaPanel',
	
	title: '栏目管理',
	controller: 'columncont',
    viewModel: {
        type: 'columncont'
    },
    
    /*viewType: 'tableview',

    lockable: false,
    
    flex: 1,*/
//    frame: true,
    
    layout: {
        type: 'border'
    },
//	layout: 'fit',
	frame: true,
	height: 500,
	
	initComponent: function() {
		
		/*var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('siteAllStore') == null) {
			mainViewModel.set('siteAllStore', Ext.create('app.store.SiteAllStore'));
		}
		
		if(mainViewModel.get('columnStatusStore') == null) {
			mainViewModel.set('columnStatusStore', Ext.create('app.store.ColumnStatusStore'));
		}
		
		var column = Ext.create('Ext.tree.Panel', {
			title: '栏目结构树',
			region: 'west',
//			frame: true,
			rootVisible: true,
		    columnLines: true,
		    width: 300,
		    height: 100,
		    columns: [{
				xtype: 'treecolumn',
				header: '栏目名称',
				dataIndex: 'title',
				flex: 1,
				locked: true
			}],
			
			listeners: {
		    	itemclick: function(view, record, item, index, e, eOpts) {
		    		console.log(record);
		    	}
		    }
		});
		
		var columnStore = Ext.create('app.store.ColumnStore');
		
		Ext.apply(column, {
            store: columnStore,
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            }
        });
		
		this.item = [column];
		
		console.log('---------------------panel');*/
		
        this.callParent();
	},
	
//	html: 'sfsfsdflkjwekfr',
	
	item: [{
		xtype: 'panel',
		html: 'panel1',
		width: 300,
	    height: 100
	}, {
		xtype: 'panel',
		html: 'panel2',
		flex: 1
	}],
	
	iconCls: 'icon-grid'
});