Ext.define("app.store.AStore", {
    extend: "Ext.data.Store",
    autoDestroy: true,
    autoLoad: false,
    listeners: {
    	load: function(self, records, successful, operation, eOpts ) {
           console.log("currentPage="+self.currentPage+",records=" + records.length);
           if (records.length == 0 && self.currentPage>1){//解决最后一页在删除记录后会出现空白列表的问题
        	   self.currentPage = 1;
        	   self.load();
           }
    	}
    }
});