Ext.define('app.ux.form.GoodsComboGridBox', {
	extend: 'Ext.form.field.ComboBox',
	alias: 'widget.goods-combo-gridbox',
	
	multiSelect: false,
	hideTrigger: true,
	triggerAction: 'query', 
	mode: 'local',
	pageSize: 3,
	maxHeight: 150,
	
	createPicker: function() {
		var me = this;
		var picker = Ext.create('Ext.grid.Panel', {  
            title : '下拉表格',  
            store: me.store,  
            frame : true,  
            resizable : true,  
            columns : [{  
                text : '#ID',  
                dataIndex : 'c_id'  
            },{  
                text : '名称' ,  
                dataIndex : 'c_name'  
            }],  
            selModel: {  
                mode: me.multiSelect ? 'SIMPLE' : 'SINGLE'  
            },  
            floating: true,  
            hidden: true,  
            width : 300,  
            columnLines : true,  
            focusOnToFront: false  
        });
		
		me.mon(picker, {  
            itemclick: me.onItemClick,  
//            refresh: me.onListRefresh,  
            scope: me  
        });
		
		/*me.mon(picker.getSelectionModel(), {  
            beforeselect: me.onBeforeSelect,  
            beforedeselect: me.onBeforeDeselect,  
            selectionchange: me.onListSelectionChange,  
            scope: me  
        });*/
		
		this.picker = picker;  
        return picker;
	},
	
	onItemClick: function(picker, record){
		console.log("----------------iyadi onItemClick");
		var me = this,  
        selection = me.picker.getSelectionModel().getSelection(),  
        valueField = me.valueField;  

	    if (!me.multiSelect && selection.length) {  
	        if (record.get(valueField) === selection[0].get(valueField)) {  
	            // Make sure we also update the display value if it's only partial  
	            me.displayTplData = [record.data];  
	            me.setRawValue(me.getDisplayValue());  
	            me.collapse();  
	        }  
	    }
	},
	
	initComponent: function() {
		Ext.apply(this, {
			displayField: 'c_name',
			valueField:'c_id',
			queryMode: 'local',
			editable: true,
			autoLoad: true,
			store: new Ext.data.Store({
				autoDestroy: true,
				autoLoad: true,
				fields: ['c_id', 'c_name'],
			    proxy: {
			    	type: 'ajax',
			    	url: 'orders/query_cont.do?auth=1',
			    	reader: {
			    		type: 'json',
			    		root: 'data'
			    	},
			    	timeout:3000000
			    },
			    sorters: [{
			    	property: 'c_id',
			        direction:'ASC'
			    }]
			})
		});
			
		this.callParent();
	},
	
	enableKeyEvents: true,
	listeners: {
		keyup: function(field, e) {
			console.log('------------iyadi keyup');
			Ext.apply(this.store.baseParams, {
				c_name: this.getRawValue()
			});
			console.log('------------iyadi e.getKey(): ' + e.getKey());
			if(e.getKey() == 13 && field.getRawValue().length > 0) {
				this.reset();
				this.store.load();
				this.expand();
			}
			
			/*if(e.getKey() == 8 && field.getRawValue().length > 0) {
				this.store.load();
			}*/
		},
		change: function(field, newValue, oldValue, eOpts) {
			console.log('------------iyadi newValue: ' + newValue);
			console.log('------------iyadi oldValue: ' + oldValue);
		}
	}
});