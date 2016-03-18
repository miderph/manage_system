Ext.define("app.model.ColumnImgModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'c_img_locked', type: 'string'},
        {name: 'c_img_id', type: 'string'},
		{name: 'c_img_rec_postion', type: 'string'},
		{name: 'c_img_plat_group', type: 'string'},
		{name: 'c_img_intro', type: 'string'},
		{name: 'c_img_url', type: 'string'},
		{name: 'c_img_little_url', type: 'string'},
		{name: 'c_img_icon_url', type: 'string'},
		{name: 'c_img_4_squares_url', type: 'string'},
		{name: 'c_img_6_squares_url', type: 'string'},
		{name: 'c_img_active_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
		{name: 'c_img_deactive_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});