create or replace view v_searchcontentlist_client as
select
     ct.c_id, ct.name, ct.description, ct.publishtime, ct.play_url, ct.tv_play_url
   , ct.play_num, ct.focus_count, ct.comment_count, ct.book_count, ct.rating
   , ct.rating_count, ct.watching_count, ct.seen_count, ct.wanted_count
   , ct.is_hd, ct.is_hot, ct.is_new
   , ct.type, ct.has_volume
   , ct.provider_id, ct.provider_name,ct.isp_cont_code,ct.isp_menu_code
   , ct.pinyin,ct.package_name
   ,ct.USERGROUP_IDS_MAC,ct.USERGROUP_IDS_ZONE,ct.USERGROUP_IDS_MODEL,ct.USERGROUP_IDS_CHANNEL
   ,ct.USERGROUP_IDS_MAC2,ct.USERGROUP_IDS_ZONE2,ct.USERGROUP_IDS_MODEL2,ct.USERGROUP_IDS_CHANNEL2
from v_base_content_client ct
;