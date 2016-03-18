Ext.define("app.store.SoftwareStatusStore", {
    extend: "Ext.data.Store",
    alias: 'store.software-status-store',
    
    autoDestroy: true,
    fields: ['status_id', 'status_name'],
    data: [
        ['-10000', '请选择'],
        ['-1', '失效'],
        ['0', '待审核'],
        ['1', '生效']
    ]
});