Ext.define("app.store.SoftwareForceStore", {
    extend: "Ext.data.Store",
    alias: 'store.software-force-store',
    
    autoDestroy: true,
    fields: ['force_id', 'force_name'],
    data: [
        ['-10000', '请选择'],
        ['0', '不强制'],
        ['1','强制']
    ]
});