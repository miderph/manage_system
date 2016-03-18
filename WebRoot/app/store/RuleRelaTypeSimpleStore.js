/**
 * Created by finlay.yan on 2015/6/29.
 */
Ext.define('app.store.RuleRelaTypeSimpleStore',{
    extend: 'Ext.data.Store',
    autoDestory: true,
    fields:['name','value'],
    data: [
        {name:'=',value:'='},
        {name:'in',value : 'in'}
    ]
})
