//Ext.define('app.ux.form.SiteCombo', {
//	extend: 'Ext.form.field.ComboBox',
//	alias: 'widget.site-combobox',
//	
//	initComponent: function() {
//		
//		Ext.apply(this, {
//			displayField: 'siteName',
//			valueField:'siteId',
//			queryMode: 'local',
//			editable: true,
//			store: new Ext.data.Store({
//				autoDestroy: true,
//				autoLoad: true,
//				fields: ['siteId', 'siteName'],
//			    proxy: {
//			    	type: 'ajax',
//			    	url: 'query_site_combox.do',
//			    	reader: {
//			    		type: 'json',
//			    		root: 'site_data'
//			    	},
//			    	timeout:3000000
//			    },
//			    sorters: [{
//			    	property: 'siteId',
//			        direction:'ASC'
//			    }]
//			})
//		});
//		
//		this.callParent();
//	}
//});