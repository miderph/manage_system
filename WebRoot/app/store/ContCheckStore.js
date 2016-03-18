Ext.define("app.store.ContCheckStore",
		{
			extend : "Ext.data.Store",
			alias : 'store.contCheck-store',
			autoDestroy : true,
			autoLoad : true,
			fields : [ 'id', 'code', 'name', 'icon', 'item_url', 'shop',
					'price', 'sales_num', 'bate', 'wangwang',
					'taobaoke_url_short', 'taobaoke_url', 'classify', 'status',
					'create_time', 'update_time' ],
			proxy : {
				type : 'ajax',
				url : 'contChecks/query.do',
				reader : {
					totalProperty : "totalSize",
					root : "list"
				},
				actionMethods : {
					read : 'POST'
				},
				timeout : 3000000
			},
			sorters : [ {
				property : 'id',
				direction : 'DESC'
			} ]
		});