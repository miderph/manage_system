Ext.define("app.model.CallRecordModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'call_from', type: 'string'},
        {name: 'call_to', type: 'string'},
        {name: 'oper_id', type: 'string'},
        {name: 'call_length', type: 'string'},
        {name: 'user_id', type: 'string'},
        {name: 'user_name', type: 'string'},
        {name: 'isnew', type: 'string'},
        {name: 'memo', type: 'string'},
		{name: 'call_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'call_in_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'call_off_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});