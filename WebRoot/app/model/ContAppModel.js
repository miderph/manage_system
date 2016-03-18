Ext.define("app.model.ContAppModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'c_id', type: 'string'},
		{name: 'c_name', type: 'string'},
		{name: 'c_type', type: 'int'},
		{name: 'c_type_str', type: 'string'},
		{name: 'ad_type', type: 'int'},
		{name: 'pinyin', type: 'string'},
		{name: 'c_status', type: 'int'},
		{name: 'is_locked', type: 'string'},
		{name: 'provider_id', type: 'string'},
		{name: 'cont_superscript', type: 'string'},
		
		{name: 'cv_alias', type: 'string'},
		{name: 'superscript_id', type: 'string'},
		{name: 'cv_play_url', type: 'string'},
		{name: 'cv_description', type: 'string'},
		{name: 'link_url', type: 'string'},
		
		{name: 'active_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'deactive_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});