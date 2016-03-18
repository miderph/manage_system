Ext.define("app.store.ChannelTypeStore", {
    extend: "Ext.data.Store",
    alias: 'store.cont-channel-store',
    
    autoDestroy: true,
    fields: ['s_id', 's_name'],
    data: [
        ['apk', 'apk发布渠道']
        //,['cont', '内容接入渠道']
    ]
});