//Ext.define('app.ux.form.SoftwareFileTypeCombo', {
//	extend: 'Ext.form.field.ComboBox',
//	alias: 'widget.software-filetype-combobox',
//	
//	initComponent: function() {
//		
//		Ext.apply(this, {
//			displayField: 'statusName',
//			valueField:'statusId',
//			queryMode: 'local',
//			editable: true,
//			store: new Ext.data.Store({
//				autoDestroy: true,
//				autoLoad: true,
//				fields: ['statusId', 'statusName'],
//			    proxy: {
//			    	type: 'ajax',
//			    	url: 'query_fileType.do',
//			    	reader: {
//			    		type: 'json',
//			    		root: 'status_data'
//			    	},
//			    	timeout:3000000
//			    },
//			    sorters: [{
//			    	property: 'status_id',
//			        direction:'ASC'
//			    }]
//			})
//		});
//		
//		this.callParent();
//	}
//});