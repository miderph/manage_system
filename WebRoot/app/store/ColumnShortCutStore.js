Ext.define("app.store.ColumnShortCutStore", {
    extend: "Ext.data.Store",
    alias: 'store.column-shortcut-store',
    
    autoDestroy: true,
    fields: ['is_shortcut_id', 'is_shortcut_name'],
    data: [
        ['0', '否'], 
        ['1', '内容链接'], 
        ['2', '栏目链接'],
        ['3', '分渠道活动广告栏目']
    ]
});