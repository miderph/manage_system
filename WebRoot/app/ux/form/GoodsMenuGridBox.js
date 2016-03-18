Ext.define('app.ux.form.GoodsMenuGridBox', {
	extend: 'Ext.form.field.Text',
	alias: 'widget.goods-menu-gridbox',
	
	initComponent: function() {
		var me = this;
		me.isFoucsed = true;
		
		me.giStore = Ext.create('app.store.GoodsInfoStore');
		
		me.menugrid = Ext.create('Ext.grid.Panel', {
            store: me.giStore,
            frame: true,
            resizable: false,
            columnLines: true,
            width: me.width,
            height: 300,
            bbar: {
				xtype: 'pagingtoolbar',
		        pageSize: me.giStore.getPageSize(),
		        store: me.giStore,
		        displayInfo: true,
		        plugins: Ext.create('app.ux.ProgressBarPager')
            },
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection: true
            },
            columns: [{
				header: '商品编号',
				dataIndex: 'cs_sales_no',
				align: 'center',
				width: 100
			}, {
                text : '商品ID',
                dataIndex : 'c_id',
				align: 'center',
				width: 100
            }, {
                text : '<div style="text-align:center">商品名称</div>' ,
                dataIndex : 'c_name',
                flex: 1
            }, {
                text : '单价',
                dataIndex : 'cs_sale_price',
                renderer: function(v) {
        			return '¥'+v;
        		},
				align: 'center',
				width: 50
            }, {
                text : '数量',
                dataIndex : 'order_cont_amount',
				align: 'center',
				width: 50
            }],
            
            listeners: {
            	itemdblclick: function(view, record, item, index, e, eOpts) {
            		if(me.menu != null) {
            			me.isFoucsed = false;
            			me.menu.hide();
            		}
            	}
            }
        });
		
		me.menu = Ext.create('Ext.menu.Menu', {
			frame : true,  
            plain: true,
		    width: me.width,
		    height: 306,
		    margin: '0 0 10 0',
		    items: [me.menugrid]
		});
		
		this.listeners = {
			focus: function(sf, event, eOpts) {
				if(!sf.isFoucsed) {
					sf.isFoucsed = true;
					return;
				}
				
				if(!Ext.isEmpty(sf.getValue())) {
					sf.menu.showAt(sf.getX(), sf.getY() + sf.getHeight());
				}
				
			},
			change: function(sf, newValue, oldValue, eOpts) {
				if(Ext.isEmpty(newValue)) {
					sf.menu.hide();
				} else {
					sf.menu.showAt(sf.getX(), sf.getY() + sf.getHeight());
				}
			},
			specialkey: function(sf, e) {
		    	if (e.getKey() == e.ENTER) {
		    		if(!Ext.isEmpty(sf.getValue())) {
		    			sf.giStore.proxy.extraParams.c_name = sf.getValue();
			    		sf.giStore.load();
		    		}
		    	}
		    }
		};
		
		this.callParent();
	}
});