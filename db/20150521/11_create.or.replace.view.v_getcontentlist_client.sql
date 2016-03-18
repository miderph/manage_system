create or replace view v_getcontentlist_client as
select
   mc.is_url_used rela_is_url_used,ct.is_autoplay,
   mc.menu_id,mc.order_num,mc.date_time rela_time,menu.isp_menu_code,menu.root_menu_path
   , ct.c_id, ct.name/*, ct.description*/, ct.publishtime, ct.play_url, ct.tv_play_url
   , ct.play_num, ct.focus_count, ct.comment_count, ct.book_count, ct.rating
   , ct.rating_count, ct.watching_count, ct.seen_count, ct.wanted_count
   , ct.is_hd, ct.is_hot, ct.is_new
   , ct.provider_id, ct.provider_name,ct.isp_cont_code, ct.isp_pubtime
   , ct.type, ct.ad_type, ct.package_name,ct.superscript_id
   , menu.site_id
   --, epg.id epgid, epg.title epgtitle, epg.duration, epg.channel_id, to_char(epg.begin_time,'yyyy-mm-dd hh24:mi:ss') begin_time, to_char(epg.end_time,'yyyy-mm-dd hh24:mi:ss') end_time
   , null epgid, null epgtitle, null duration, null channel_id, null begin_time, null end_time
   ,ct.USERGROUP_IDS_MAC,ct.USERGROUP_IDS_ZONE,ct.USERGROUP_IDS_MODEL,ct.USERGROUP_IDS_CHANNEL
   ,ct.USERGROUP_IDS_MAC2,ct.USERGROUP_IDS_ZONE2,ct.USERGROUP_IDS_MODEL2,ct.USERGROUP_IDS_CHANNEL2
from v_base_content_client ct
     join zl_rela_menu_cont mc on (mc.c_id = ct.c_id)
     join zl_menu menu on (menu.id = mc.menu_id)
;