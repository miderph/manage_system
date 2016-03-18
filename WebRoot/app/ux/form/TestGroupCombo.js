//Ext.define('app.ux.form.TestGroupCombo', {
//	extend: 'Ext.form.field.ComboBox',
//	alias: 'widget.test-group-combobox',
//	
//	initComponent: function() {
//		
//		Ext.apply(this, {
//			displayField: 'userGroupName',
//			valueField:'userGroupId',
//			queryMode: 'local',
//			editable: true,
//			store: new Ext.data.Store({
//				autoDestroy: true,
//				autoLoad: true,
//				fields: ['userGroupId', 'userGroupName'],
//			    proxy: {
//			    	type: 'ajax',
//			    	url: 'user_group_menu.do?categoryID=1',
//			    	reader: {
//			    		type: 'json',
//			    		totalProperty: "results",
//			    		root: 'userGroup_data'
//			    	},
//			    	timeout:3000000
//			    }/*,
//			    sorters: [{
//			    	property: 'status_id',
//			        direction:'ASC'
//			    }]*/
//			})
//		});
//		
//		this.callParent();
//	}
//});