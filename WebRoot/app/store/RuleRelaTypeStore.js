/**
 * Created by finlay.yan on 2015/6/29.
 */
Ext.define('app.store.RuleRelaTypeStore',{
    extend: 'Ext.data.Store',
    autoDestory: true,
    fields:['name','value'],
    data: [
        {name:'=',value:'='},
        {name:'>',value:'>'},
        {name:'>=',value:'>='},
        {name:'<',value:'<'},
        {name:'<=',value:'<='},
        {name:'in',value : 'in'},
        {name:'between',value : 'between'}
    ]
})
