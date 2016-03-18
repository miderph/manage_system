Ext.define("app.model.ContProviderModel", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'cp_id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'description', type: 'string'},
        {name: 'site_id', type: 'string'},
        {name: 'isdefault', type: 'string'},
        {name: 'c_img_icon_url', type: 'string'},
        {name: 'hot_info', type: 'string'},

        {name: 'cont_provider_id', type: 'string'},
        {name: 'cont_provider_id_name', type: 'string'},
        {name: 'cont_provider_ids', type: 'string'},
        {name: 'cont_provider_names', type: 'string'},
        {name: 'chn_provider_id', type: 'string'},
        {name: 'chn_provider_name', type: 'string'},
        {name: 'epg_provider_ids', type: 'string'},
        {name: 'epg_provider_names', type: 'string'},
        
        {name: 'epg_priority', type: 'string'},
        {name: 'xmpp_index', type: 'string'},
        {name: 'need_check_uap', type: 'string'},
        {name: 'can_switchtv', type: 'string'},
        {name: 'can_playvideo', type: 'string'},
        
        {name: 'can_download', type: 'string'},
        {name: 'can_recording', type: 'string'},
        {name: 'can_timeshift', type: 'string'},
        {name: 'can_playback', type: 'string'},
        {name: 'p_status', type: 'string'},
        
        {name: 'can_switchtv_pids', type: 'string'},
        {name: 'can_switchtv_names', type: 'string'},
        {name: 'can_playvideo_pids', type: 'string'},
        {name: 'can_playvideo_names', type: 'string'},
        {name: 'can_download_pids', type: 'string'},
        {name: 'can_download_names', type: 'string'},
        {name: 'can_recording_pids', type: 'string'},
        {name: 'can_recording_names', type: 'string'},
        {name: 'can_timeshift_pids', type: 'string'},
        {name: 'can_timeshift_names', type: 'string'},
        {name: 'can_playback_pids', type: 'string'},
        {name: 'can_playback_names', type: 'string'},
        
        {name: 'stb_prefix_ids', type: 'string'},
        {name: 'stb_prefix_names', type: 'string'},
        {name: 'stb_prefix', type: 'string'},
        {name: 'create_time', type: 'date', dateFormat: 'Y-m-d H:i:s'},
        {name: 'modify_time', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});