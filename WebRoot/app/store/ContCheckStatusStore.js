Ext.define("app.store.ContCheckStatusStore", {
    extend: "Ext.data.Store",
    alias: 'store.cont-check-status-store',
    
    autoDestroy: true,
    fields: ['s_id', 's_name'],
    data: [
        ['1', '正常'],
        ['-1','禁用']
    ]
});