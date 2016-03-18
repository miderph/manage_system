Ext.define("app.store.ColumnImgPlatStore", {
    extend: "Ext.data.Store",
    alias: 'store.column-imgplat-store',
    autoDestroy: true,
	autoLoad: true,
    fields: ['plat_groupId', 'plat_groupName'],
    proxy: {
    	type: 'ajax',
    	url: 'contvideo/plat_group_cont_video.do?siteId=-1',
    	reader: {
    		type: 'json',
    		root: 'plat_group_data'
    	},
    	timeout:3000000
    },
    sorters: [{
    	property: 'plat_groupId',
        direction:'ASC'
    }]
});